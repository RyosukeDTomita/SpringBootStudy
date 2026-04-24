#!/bin/bash
set -e

# deploy.sh をバックグラウンドで起動
./deploy.sh &
DEPLOY_PID=$!

# アプリが起動するまで待機
echo "アプリの起動を待機中..."
until curl -s --max-time 2 http://localhost:8080 > /dev/null 2>&1; do
    # deploy.sh が異常終了していたら中断
    if ! kill -0 "$DEPLOY_PID" 2>/dev/null; then
        echo "deploy.sh が異常終了しました"
        exit 1
    fi
    sleep 1
done
echo "アプリ起動完了"

# E2E テスト実行
cd e2e
#bun run test:headed
bun run test
TEST_EXIT=$?
cd ..

# deploy.sh のプロセスを停止
kill "$DEPLOY_PID" 2>/dev/null || true

exit $TEST_EXIT
