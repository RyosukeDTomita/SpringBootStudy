package com.example.study.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest はアプリ全体の Spring コンテナを起動する。MockMvc を併用すると
// HTTP リクエスト → Controller → Service → MyBatis Mapper → H2 まで通る結合テストになる。
// (実 HTTP は流れず MockMvc 内部のディスパッチャを通るため、サーブレットコンテナ起動は不要)
//
// @Transactional をクラスに付けて各テスト後に DB をロールバックする。これにより
// テスト間でデータが残らず、順序に依存しなくなる。
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ItemRestControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private JdbcTemplate jdbc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "ADMIN")
  void create_POSTで保存されGETで取得できる() throws Exception {
    String body =
        mockMvc
            .perform(
                post("/register/item")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {"name":"ぶどう","price":300,"description":"山梨産"}
                        """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.itemId").isNumber())
            .andExpect(jsonPath("$.name").value("ぶどう"))
            .andReturn()
            .getResponse()
            .getContentAsString();
    JsonNode created = objectMapper.readTree(body);
    int itemId = created.get("itemId").asInt();

    // DB に実際に書き込まれていることを生クエリで確認 (古典派的な観察可能な振る舞い検証)
    Map<String, Object> row =
        jdbc.queryForMap("SELECT name, price, description FROM items WHERE item_id = ?", itemId);
    assertThat(row).containsEntry("name", "ぶどう").containsEntry("price", 300);

    // GET でも同じ内容が取れる
    mockMvc
        .perform(get("/register/item/" + itemId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("ぶどう"))
        .andExpect(jsonPath("$.price").value(300));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void findAll_DBの全件が返る() throws Exception {
    jdbc.update("INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "A", 100, "a");
    jdbc.update("INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "B", 200, "b");

    mockMvc
        .perform(get("/register/item"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void update_PUTでDB側が書き換わる() throws Exception {
    jdbc.update("INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "りんご", 100, "青森産");
    Integer itemId =
        jdbc.queryForObject("SELECT item_id FROM items WHERE name = ?", Integer.class, "りんご");

    mockMvc
        .perform(
            put("/register/item/" + itemId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name":"りんご(訳あり)","price":80,"description":"青森産"}
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price").value(80));

    Integer priceAfter =
        jdbc.queryForObject(
            "SELECT price FROM items WHERE item_id = ?", Integer.class, itemId);
    assertThat(priceAfter).isEqualTo(80);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void delete_DELETEで行が消える() throws Exception {
    jdbc.update("INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "みかん", 80, "愛媛産");
    Integer itemId =
        jdbc.queryForObject("SELECT item_id FROM items WHERE name = ?", Integer.class, "みかん");

    mockMvc
        .perform(delete("/register/item/" + itemId).with(csrf()))
        .andExpect(status().isNoContent());

    List<Integer> remaining =
        jdbc.queryForList(
            "SELECT item_id FROM items WHERE item_id = ?", Integer.class, itemId);
    assertThat(remaining).isEmpty();
  }

  @Test
  @WithMockUser(roles = "USER")
  void 一般ユーザは403() throws Exception {
    // 結合テストでも SecurityConfig の制限が効くことを確認
    mockMvc.perform(get("/register/item")).andExpect(status().isForbidden());
  }
}
