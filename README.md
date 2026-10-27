# ISY Tic-Tac-Toe

===================== STARTEN OP MEERDERE LAPTOPS =====================

Laptop A = de server. Laptop B, C, ... = alleen clients.
Zet alle laptops op hetzelfde netwerk (schoolwifi blokkeert dit vaak: gebruik een telefoonhotspot).

--- Laptop A: server ---
java -jar newgameserver-release-V1.jar                (in de map met de jar, venster open laten)
ipconfig                                              (zoek IPv4 Address van de wifi-adapter)
netsh advfirewall firewall add rule name="GameServer" dir=in action=allow protocol=TCP localport=7789 profile=any
                                                       (PowerShell als administrator)
Netwerkprofiel van de wifi op "Private" zetten (Windows: Instellingen > Netwerk > Wi-Fi)

--- Laptop B, C: eerst testen ---
Test-NetConnection <IP van laptop A> -Port 7789       (PowerShell, je wilt TcpTestSucceeded : True)

--- Client starten ---
Laptop A:       Run Client (geen argumenten, gebruikt 127.0.0.1)
Laptop B, C:    Run Client met argumenten <IP van laptop A> 7789
                 in VS Code: .vscode/launch.json > "args": ["<IP van laptop A>", "7789"]

--- Typen in de client ---
login <naam>                    (elke laptop een eigen naam)
get gamelist
subscribe <spel>                (twee clients met hetzelfde spel krijgen automatisch een match)
move <0-8>                      (alleen als je Your Turn! ziet)
exit                            (afsluiten)

--- Als het niet werkt ---
Connection refused  = verkeerd IP, of de server draait niet op dat adres
Timeout             = netwerk of firewall (hotspot gebruiken of Tailscale)
IP veranderd na opnieuw verbinden? Doe ipconfig opnieuw op laptop A
======================================================================

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
