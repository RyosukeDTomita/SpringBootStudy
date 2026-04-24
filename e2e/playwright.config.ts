import { defineConfig } from "@playwright/test";

export default defineConfig({
  testDir: ".",
  testMatch: "*.spec.ts",
  timeout: 30_000,
  use: {
    baseURL: "http://localhost:8080",
    headless: true,
  },
  projects: [
    {
      name: "chromium",
      use: { browserName: "chromium" },
    },
  ],
});
