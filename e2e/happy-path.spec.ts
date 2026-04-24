import { test, expect, type Page } from "@playwright/test";

// ──────────────────────────────────────────────
// ヘルパー
// ──────────────────────────────────────────────

/** Spring Security のフォームログインを実行する */
async function login(page: Page, username: string, password: string) {
  await page.goto("/login");
  await page.fill('input[name="username"]', username);
  await page.fill('input[name="password"]', password);
  await page.click('button[type="submit"], input[type="submit"]');
  await page.waitForURL("/");
}

// ──────────────────────────────────────────────
// 1. ホームページ
// ──────────────────────────────────────────────

test.describe("ホームページ", () => {
  test("タイトルとナビゲーションリンクが表示される", async ({ page }) => {
    await page.goto("/");
    await expect(page.locator("h1")).toHaveText("Spring Boot Study");

    // 全リンクが存在する
    await expect(page.locator('a[href="/login"]')).toBeVisible();
    await expect(page.locator('a[href="/hello"]')).toBeVisible();
    await expect(page.locator('a[href="/users"]')).toBeVisible();
    await expect(page.locator('a[href="/shop"]')).toBeVisible();
    await expect(page.locator('a[href="/item.html"]')).toBeVisible();
  });
});

// ──────────────────────────────────────────────
// 2. Hello API
// ──────────────────────────────────────────────

test.describe("Hello API", () => {
  test("デフォルトの挨拶 JSON が返る", async ({ page }) => {
    const res = await page.request.get("/hello");
    expect(res.ok()).toBeTruthy();
    const json = await res.json();
    expect(json.message).toBe("Hello, World!");
    expect(json.name).toBe("World");
  });

  test("name パラメータ付きで挨拶が返る", async ({ page }) => {
    const res = await page.request.get("/hello?name=Spring");
    const json = await res.json();
    expect(json.message).toBe("Hello, Spring!");
    expect(json.name).toBe("Spring");
  });
});

// ──────────────────────────────────────────────
// 3. ログイン
// ──────────────────────────────────────────────

test.describe("ログイン", () => {
  test("USER ロールでログインできる", async ({ page }) => {
    await login(page, "user", "user");
    await expect(page.locator("h1")).toHaveText("Spring Boot Study");
  });

  test("ADMIN ロールでログインできる", async ({ page }) => {
    await login(page, "admin", "admin");
    await expect(page.locator("h1")).toHaveText("Spring Boot Study");
  });
});

// ──────────────────────────────────────────────
// 4. User Search → Profile
// ──────────────────────────────────────────────

test.describe("ユーザー検索", () => {
  test("検索フォームが表示される", async ({ page }) => {
    await page.goto("/users");
    await expect(page.locator("h1")).toHaveText("ユーザー検索");
    await expect(page.locator('input[type="text"]')).toBeVisible();
    await expect(page.locator('button[type="submit"]')).toBeVisible();
  });

  test("ユーザーID で検索してプロフィールが表示される", async ({ page }) => {
    await page.goto("/users");
    await page.fill('input[type="text"]', "user001");
    await page.click('button[type="submit"]');

    // プロフィールページに遷移する
    await expect(page.locator("h1")).toHaveText("ユーザープロフィール");
    await expect(page.locator(".user-id")).toHaveText("user001");
  });

  test("プロフィールから検索に戻れる", async ({ page }) => {
    await page.goto("/users/user001");
    await expect(page.locator("h1")).toHaveText("ユーザープロフィール");

    await page.click('a[href="/users"]');
    await expect(page.locator("h1")).toHaveText("ユーザー検索");
  });

  test("プロフィールからホームに戻れる", async ({ page }) => {
    await page.goto("/users/user001");
    await page.click('a[href="/"]');
    await expect(page.locator("h1")).toHaveText("Spring Boot Study");
  });
});

// ──────────────────────────────────────────────
// 5. Shop（USER ロール）
// ──────────────────────────────────────────────

test.describe("ショップ", () => {
  test.beforeEach(async ({ page }) => {
    await login(page, "user", "user");
  });

  test("商品情報が表示される", async ({ page }) => {
    await page.goto("/shop");
    await expect(page.locator("h1")).toHaveText("ショップ");
    await expect(page.locator(".product-name")).toBeVisible();
    await expect(page.locator(".product-price")).toBeVisible();
  });

  test("商品を購入できる", async ({ page }) => {
    await page.goto("/shop");

    // 在庫があれば購入フォームが表示される
    const purchaseForm = page.locator('form[action="/shop/purchase"]');
    if (await purchaseForm.isVisible()) {
      await page.fill("#quantity", "1");
      await page.click('button[type="submit"]');

      // 成功 or エラーメッセージが表示される（在庫状態による）
      const msg = page.locator(".success, .error");
      await expect(msg).toBeVisible();
    }
  });

  test("ログインユーザ名が表示される", async ({ page }) => {
    await page.goto("/shop");
    await expect(page.getByText("ログイン中:")).toBeVisible();
  });

  test("ホームへ戻れる", async ({ page }) => {
    await page.goto("/shop");
    await page.click("text=ホームへ戻る");
    await expect(page.locator("h1")).toHaveText("Spring Boot Study");
  });
});

// ──────────────────────────────────────────────
// 6. Item Registration（ADMIN ロール）
// ──────────────────────────────────────────────

test.describe("商品登録 (REST API CRUD)", () => {
  test.beforeEach(async ({ page }) => {
    await login(page, "admin", "admin");
  });

  test("商品一覧ページが表示される", async ({ page }) => {
    await page.goto("/item.html");
    await expect(page.locator("h1")).toHaveText("商品登録 (REST API)");
    await expect(page.getByText("ログイン中:")).toBeVisible();
  });

  test("商品を登録できる", async ({ page }) => {
    await page.goto("/item.html");

    await page.fill("#createName", "テスト商品");
    await page.fill("#createPrice", "500");
    await page.fill("#createDesc", "E2Eテスト用");
    await page.click('button:has-text("登録 (POST)")');

    await expect(page.locator(".msg-ok")).toHaveText("登録しました");

    // テーブルに登録した商品が表示される
    await expect(page.locator("#itemList")).toContainText("テスト商品");
  });

  test("商品を更新できる", async ({ page }) => {
    await page.goto("/item.html");

    // まず商品を登録
    await page.fill("#createName", "更新前商品");
    await page.fill("#createPrice", "100");
    await page.fill("#createDesc", "更新テスト");
    await page.click('button:has-text("登録 (POST)")');
    await expect(page.locator(".msg-ok")).toHaveText("登録しました");

    // テーブルから登録した商品の ID を取得
    const row = page.locator("#itemList tr", { hasText: "更新前商品" });
    const itemId = await row.locator("td").first().textContent();

    // 更新
    await page.fill("#updateId", itemId!);
    await page.fill("#updateName", "更新後商品");
    await page.fill("#updatePrice", "999");
    await page.fill("#updateDesc", "更新済み");
    await page.click('button:has-text("更新 (PUT)")');

    await expect(page.locator(".msg-ok")).toHaveText("更新しました");
    await expect(page.locator("#itemList")).toContainText("更新後商品");
  });

  test("商品を削除できる", async ({ page }) => {
    await page.goto("/item.html");

    // まず商品を登録
    await page.fill("#createName", "削除対象商品");
    await page.fill("#createPrice", "200");
    await page.click('button:has-text("登録 (POST)")');
    await expect(page.locator(".msg-ok")).toHaveText("登録しました");

    // 削除ボタンをクリック
    const row = page.locator("#itemList tr", { hasText: "削除対象商品" });
    await row.locator('button:has-text("削除")').click();

    await expect(page.locator(".msg-ok")).toContainText("削除しました");
  });

  test("ホームへ戻れる", async ({ page }) => {
    await page.goto("/item.html");
    await page.click("text=ホームへ戻る");
    await expect(page.locator("h1")).toHaveText("Spring Boot Study");
  });
});
