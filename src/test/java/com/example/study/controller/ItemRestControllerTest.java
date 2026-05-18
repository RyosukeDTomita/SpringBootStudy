package com.example.study.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.study.config.AppConfig;
import com.example.study.config.SecurityConfig;
import com.example.study.converter.ItemConverterImpl;
import com.example.study.domain.Item;
import com.example.study.service.ItemService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest はコントローラ層だけを起動する Spring Boot のテストスライス。
// MockMvc が自動構成され、Service や Repository は読み込まれないので @MockitoBean で差し替える。
// SecurityConfig / AppConfig / ItemConverterImpl は @Import で明示的に読み込む。
@WebMvcTest(ItemRestController.class)
@Import({AppConfig.class, SecurityConfig.class, ItemConverterImpl.class})
class ItemRestControllerTest {

  @Autowired private MockMvc mockMvc;

  // @MockitoBean は Spring Framework 6.2 で導入された新しいモック注入アノテーション (旧 @MockBean の置き換え)。
  @MockitoBean private ItemService itemService;

  @Test
  @WithMockUser(roles = "ADMIN")
  void findAll_管理者は商品一覧を取得できる() throws Exception {
    when(itemService.findAll())
        .thenReturn(List.of(new Item(1, "りんご", 100, "青森産"), new Item(2, "みかん", 80, "愛媛産")));

    mockMvc
        .perform(get("/register/item"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].itemId").value(1))
        .andExpect(jsonPath("$[0].name").value("りんご"))
        .andExpect(jsonPath("$[1].name").value("みかん"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void findAll_一般ユーザは403() throws Exception {
    // /register/** は SecurityConfig で hasRole("ADMIN") に制限されている
    mockMvc.perform(get("/register/item")).andExpect(status().isForbidden());
  }

  @Test
  void findAll_未認証は401相当_認証エントリポイントへ() throws Exception {
    // formLogin 構成のため、未認証アクセスは 302 でログイン画面へリダイレクトされる
    mockMvc.perform(get("/register/item")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void findById_存在するアイテムを返す() throws Exception {
    when(itemService.findById(1)).thenReturn(new Item(1, "りんご", 100, "青森産"));

    mockMvc
        .perform(get("/register/item/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.itemId").value(1))
        .andExpect(jsonPath("$.price").value(100));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void create_新規アイテムを登録して201を返す() throws Exception {
    var saved = new Item(3, "ぶどう", 300, "山梨産");
    // 引数は MapStruct で変換されるため eq でなく any で受ける
    when(itemService.save(any(Item.class))).thenReturn(saved);

    mockMvc
        .perform(
            post("/register/item")
                .with(csrf()) // SecurityConfig で CSRF 有効なので POST には csrf トークンが必要
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"ぶどう","price":300,"description":"山梨産"}
                    """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.itemId").value(3))
        .andExpect(jsonPath("$.name").value("ぶどう"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void update_既存アイテムを更新する() throws Exception {
    var updated = new Item(1, "りんご(訳あり)", 80, "青森産");
    when(itemService.update(eq(1), any(Item.class))).thenReturn(updated);

    mockMvc
        .perform(
            put("/register/item/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {"name":"りんご(訳あり)","price":80,"description":"青森産"}
                    """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("りんご(訳あり)"))
        .andExpect(jsonPath("$.price").value(80));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void delete_既存アイテムを削除して204を返す() throws Exception {
    mockMvc
        .perform(delete("/register/item/1").with(csrf()))
        .andExpect(status().isNoContent());

    verify(itemService).deleteById(1);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void create_CSRFトークン無しは403() throws Exception {
    // csrf() を付けないと CsrfFilter で 403 になることを確認 (Security の振る舞いのテスト)
    mockMvc
        .perform(
            post("/register/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"x\",\"price\":0,\"description\":\"x\"}"))
        .andExpect(status().isForbidden());
  }
}
