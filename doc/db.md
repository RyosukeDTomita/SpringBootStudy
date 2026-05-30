# DB関連メモ

## コネクションはSpring Bootのauto-configurationが作成している

application.yaml に書いた spring.datasource.\* の設定を元に、Spring Bootのauto-configuration が自動的にDataSource（コネクションプール）を作成しています。

具体的には:

1. Spring Boot がクラスパス上の PostgreSQL ドライバを検出
1. application.yaml の url / username / password を読み取り
1. HikariCP（Spring Boot デフォルトのコネクションプールライブラリ）で DataSource Bean
   を生成
1. MyBatis Spring Boot Starter がその DataSource を受け取り、SqlSessionFactory を構築
1. @Mapper インターフェースが SqlSession 経由で SQL を実行

---

## `@Transactional`

### `@EnablingTransactionManagement`

`@EnablingTransactionManagement` を付けてトランザクション管理を有効化する必要がある。

Spring Boot の場合、`@SpringBootApplication` に `@EnableAutoConfiguration` が含まれているため、通常は明示的に `@EnablingTransactionManagement` を付けなくてもトランザクション管理が有効になる。

### Proxyオブジェクトは、メソッド呼び出しの「前後処理」を差し込む

`@Transactional` を付けたクラスは、Spring AOP によって Proxy オブジェクトに置き換えられる。
Proxyオブジェクトはもとのクラスを継承、もしくはインターフェースを実装しているため、同じメソッドを持っている。

1. **メソッド呼び出しをインターセプト**する
1. Spring の **`PlatformTransactionManager`** に「トランザクション開始を指示
1. 元のクラスのメソッドを実行する
1. 正常終了なら commit、例外なら rollback を依頼する

> [!NOTE]
> `PlatformTransactionManager`の具象クラスはSpring Bootのauto-configurationが自動で選択してくれる。

---

## コネクションは誰が持つ?

**`TransactionManager` → `DataSource`（HikariCP）→ JDBC `Connection`** という流れです。

```
Proxy
 └→ TransactionManager に「トランザクション開始」を依頼
      └→ DataSource (HikariCP) からコネクションを1本借りる
           └→ そのコネクションを ThreadLocal に紐づけて保持
                └→ MyBatis の Mapper が SQL 実行時、同じコネクションを使う
```

ポイントは **ThreadLocal** です。トランザクション中は、同じスレッド内のすべての DB 操作が同じコネクションを使うように Spring が管理しています。

## まとめ

| 概念 | 役割 |
|------|------|
| **Proxy** | メソッドの前後に begin/commit/rollback を差し込む |
| **TransactionManager** | トランザクションの状態管理、コネクションの取得・解放 |
| **DataSource (HikariCP)** | コネクションプール。コネクションの貸し出し |
| **ThreadLocal** | 同一トランザクション内で同じコネクションを共有する仕組み |

つまり Proxy は「**いつ**トランザクションを開始・終了するか」を制御する係で、コネクション自体は TransactionManager と DataSource が管理しています。
