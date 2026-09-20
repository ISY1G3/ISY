{
  description = "Development shell for the ISY JavaFX project";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = { nixpkgs, ... }:
    let
      supportedSystems = [
        "x86_64-linux"
        "aarch64-linux"
      ];
      forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
    in
    {
      devShells = forAllSystems (system:
        let
          pkgs = import nixpkgs { inherit system; };
          jdk = pkgs.jdk21.override { enableJavaFX = true; };
        in
        {
          default = pkgs.mkShell {
            packages = [ jdk ];

            JAVA_HOME = "${jdk}";

            shellHook = ''
              echo "Java development shell"
              echo "  Java:   $(java --version 2>&1 | head -n 1)"
              echo "  JavaFX: included in the JDK"
            '';
          };
        });
    };
}
