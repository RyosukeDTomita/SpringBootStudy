-- テスト用 DDL のみ (本番 db/init.sql と同じ構造)。
-- テスト側ではシードデータは固定しない (各テストで必要に応じて INSERT する)。

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    profile TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(product_id),
    quantity INTEGER NOT NULL,
    ordered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS items (
    item_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS auth_users (
    user_id  VARCHAR(50)  PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL
);
