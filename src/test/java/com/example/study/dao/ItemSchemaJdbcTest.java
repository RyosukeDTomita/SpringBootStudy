package com.example.study.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.jdbc.core.JdbcTemplate;

// @JdbcTest は DataSource / JdbcTemplate / TransactionManager だけを読み込む軽量スライス。
// MyBatis Mapper や Service / Controller は load されないため、生 SQL レベルで
// 「INSERT したら SELECT で見える」「UPDATE したら値が変わる」といった
// 観察可能な振る舞いをテストするのに使う (古典派スタイル)。
//
// Replace.NONE: src/test/resources/application.yaml で設定した
// H2 (MODE=PostgreSQL) と schema.sql をそのまま使う。
// デフォルトの Replace.ANY だと組み込み H2 を別個に立ち上げ、PostgreSQL モードが効かない。
//
// @JdbcTest は各テストを自動でロールバックするため、テスト間のデータ独立性が保たれる。
@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ItemSchemaJdbcTest {

  @Autowired private JdbcTemplate jdbc;

  @Test
  void items_INSERTしたレコードがSELECTで取得できる() {
    jdbc.update(
        "INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "ぶどう", 300, "山梨産");

    List<Map<String, Object>> rows =
        jdbc.queryForList("SELECT name, price, description FROM items WHERE name = ?", "ぶどう");

    assertThat(rows).hasSize(1);
    assertThat(rows.get(0))
        .containsEntry("name", "ぶどう")
        .containsEntry("price", 300)
        .containsEntry("description", "山梨産");
  }

  @Test
  void items_UPDATEで値が書き換わる() {
    jdbc.update("INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "りんご", 100, "青森産");
    Integer itemId =
        jdbc.queryForObject("SELECT item_id FROM items WHERE name = ?", Integer.class, "りんご");

    int updated = jdbc.update("UPDATE items SET price = ? WHERE item_id = ?", 80, itemId);

    assertThat(updated).isEqualTo(1);
    Integer price =
        jdbc.queryForObject(
            "SELECT price FROM items WHERE item_id = ?", Integer.class, itemId);
    assertThat(price).isEqualTo(80);
  }

  @Test
  void items_DELETEで行が消える() {
    jdbc.update("INSERT INTO items (name, price, description) VALUES (?, ?, ?)", "みかん", 80, "愛媛産");
    Integer itemId =
        jdbc.queryForObject("SELECT item_id FROM items WHERE name = ?", Integer.class, "みかん");

    int deleted = jdbc.update("DELETE FROM items WHERE item_id = ?", itemId);

    assertThat(deleted).isEqualTo(1);
    Integer count =
        jdbc.queryForObject(
            "SELECT COUNT(*) FROM items WHERE item_id = ?", Integer.class, itemId);
    assertThat(count).isZero();
  }

  @Test
  void products_SERIAL列で連番のIDが付く() {
    jdbc.update("INSERT INTO products (name, price, stock) VALUES (?, ?, ?)", "コーヒー", 500, 10);
    jdbc.update("INSERT INTO products (name, price, stock) VALUES (?, ?, ?)", "紅茶", 400, 8);

    List<Integer> ids =
        jdbc.queryForList(
            "SELECT product_id FROM products WHERE name IN ('コーヒー', '紅茶') ORDER BY product_id",
            Integer.class);

    assertThat(ids).hasSize(2);
    // 連番で発行されること (具体的な値は前後のテストに依存しないよう差分のみ検証)
    assertThat(ids.get(1) - ids.get(0)).isEqualTo(1);
  }
}
