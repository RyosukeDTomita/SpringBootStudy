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
