CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    profile TEXT NOT NULL
);

INSERT INTO users (user_id, profile) VALUES
    ('user001', 'こんにちは！私はSpringBootを勉強中のエンジニアです。'),
    ('user002', 'Javaが大好きなバックエンドエンジニアです。趣味は読書と山登り。'),
    ('user003', 'フルスタックエンジニアを目指しています。最近はReactも勉強中です。'),
    ('alice',   'Hello! I am Alice, a software engineer passionate about open source.'),
    ('bob',     'Bob here. I specialize in database optimization and performance tuning.')
ON CONFLICT (user_id) DO NOTHING;

-- 商品テーブル
CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0
);

-- 注文テーブル
CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(product_id),
    quantity INTEGER NOT NULL,
    ordered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- りんごを5個だけ入荷
INSERT INTO products (name, price, stock) VALUES ('りんご', 150, 5)
ON CONFLICT DO NOTHING;

-- 商品登録テーブル (REST API用)
CREATE TABLE IF NOT EXISTS items (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    description TEXT
);

-- 認証ユーザテーブル (Spring Security 用)
-- パスワードは {noop}<plaintext> 形式で保存する。Spring Security の DelegatingPasswordEncoder が
-- {noop} プレフィックスを「ハッシュなし」として解釈するため、現状はそのまま比較される。
-- TODO: ユーザ登録機能の実装時に {bcrypt} 形式へ移行する。
CREATE TABLE IF NOT EXISTS auth_users (
    user_id  VARCHAR(50)  PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);

INSERT INTO auth_users (user_id, password, role) VALUES
    ('admin', '{noop}admin', 'ADMIN'),
    ('user',  '{noop}user',  'USER')
ON CONFLICT (user_id) DO NOTHING;
