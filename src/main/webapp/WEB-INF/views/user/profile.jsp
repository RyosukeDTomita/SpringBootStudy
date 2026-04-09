<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>${user.userId} のプロフィール</title>
    <style>
        body { font-family: sans-serif; max-width: 600px; margin: 40px auto; padding: 0 20px; }
        h1 { color: #333; }
        .card { background: #f8f9fa; border: 1px solid #dee2e6; border-radius: 8px; padding: 24px; margin-top: 24px; }
        .label { font-size: 0.85rem; color: #666; margin-bottom: 4px; }
        .value { font-size: 1.1rem; color: #222; }
        .user-id { font-weight: bold; color: #4a90d9; }
        .back { display: inline-block; margin-top: 24px; color: #4a90d9; text-decoration: none; }
        .back:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>ユーザープロフィール</h1>
    <div class="card">
        <div class="label">ユーザーID</div>
        <div class="value user-id">${user.userId}</div>
        <br>
        <div class="label">プロフィール</div>
        <div class="value">${user.profile}</div>
    </div>
    <a class="back" href="/users">← 検索に戻る</a>
</body>
</html>
