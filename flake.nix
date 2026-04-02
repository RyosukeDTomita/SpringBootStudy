{
  description = "Spring Boot 3 study project – JDK 21 + Gradle dev environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    treefmt-nix.url = "github:numtide/treefmt-nix";
  };

  outputs = { self, nixpkgs, flake-utils, treefmt-nix }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
        jarName = "spring-boot-study-0.0.1-SNAPSHOT.jar";
        jarPath = ./build/libs/${jarName};

        treefmtEval = treefmt-nix.lib.evalModule pkgs {
          projectRootFile = "flake.nix";

          # Markdown
          programs.mdformat.enable = true;

          # Java
          programs.google-java-format.enable = true;
        };
      in {
        # --- フォーマッタ（nix fmt で一括実行）---
        formatter = treefmtEval.config.build.wrapper;

        # --- コンテナイメージ ---
        # 使い方:
        #   gradle bootJar
        #   nix build .#container
        #   podman load < result
        #   podman run --rm -p 8080:8080 spring-boot-study:latest
        packages.container = pkgs.dockerTools.buildLayeredImage {
          name = "spring-boot-study";
          tag = "latest";

          contents = [
            pkgs.jdk21
            # /tmp など基本ディレクトリ
            pkgs.dockerTools.fakeNss
          ];

          extraCommands = ''
            mkdir -p app tmp
            cp ${jarPath} app/app.jar
          '';

          config = {
            Cmd = [
              "${pkgs.jdk21}/bin/java"
              "-jar"
              "/app/app.jar"
            ];
            ExposedPorts = { "8080/tcp" = {}; };
            WorkingDir = "/app";
          };
        };

        # --- 開発シェル ---
        devShells.default = pkgs.mkShell {
          name = "spring-boot-study";

          packages = with pkgs; [
            jdk21
            gradle
            git
            curl
            jq
            httpie
          ];

          JAVA_HOME = "${pkgs.jdk21}";

          # プロジェクト内にキャッシュ（~/.gradle を汚さない）
          GRADLE_USER_HOME = ".gradle-home";

          shellHook = ''
            echo "──────────────────────────────────────────────"
            echo " Spring Boot Study Shell"
            echo " Java  : $(java -version 2>&1 | head -1)"
            echo " Gradle: $(gradle --version 2>/dev/null | grep '^Gradle' || echo 'not found')"
            if ! command -v podman &>/dev/null; then
              echo " Podman: NOT FOUND – install via OS package manager"
            else
              echo " Podman: $(podman --version)"
            fi
            echo ""
            echo " コンテナビルド手順:"
            echo "   gradle bootJar"
            echo "   nix build .#container"
            echo "   podman load < result"
            echo "   podman run --rm -p 8080:8080 spring-boot-study:latest"
            echo "──────────────────────────────────────────────"
          '';
        };
      }
    );
}
