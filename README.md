# ISY Tic-Tac-Toe

## Requirements

- JDK 21
- Git

Maven and JavaFX are provided through the Maven Wrapper and `pom.xml`; you do not need to install Maven or download a JavaFX SDK separately.

## Run

On macOS and Linux:

```sh
./mvnw javafx:run
```

On Windows:

```bat
mvnw.cmd javafx:run
```

In VS Code, install the Extension Pack for Java and open this repository. The project imports its dependencies from Maven. The `Main` launch configuration uses the Maven-managed JavaFX dependencies and does not require a local SDK path.

## Build

On macOS and Linux:

```sh
./mvnw package
```

On Windows:

```bat
mvnw.cmd package
```

Build output is written to `target/`.

## Optional Nix development shell

The Nix flake provides JDK 21, IntelliJ IDEA, and Linux desktop libraries for development on the project desktop. Enter the shell with:

```sh
nix develop
```

The flake is an optional environment setup; Maven remains the build and dependency definition on every platform.
