# StackUpper (NeoForge)
A minecraft mod that allows changing stacksize from 64 (or 99 (Mojang's max limit)) to 2^30 (1073741824)

> [!IMPORTANT]
> [Midnightlib](https://modrinth.com/mod/midnightlib) is a required dependency

### Downloads
[GitHub](https://github.com/MaiKokain/StackUpper/releases/latest) | [Modrinth](https://modrinth.com/mod/stackupper/version/latest)

### Possible future changes
- Use PORTB BiggerStack's XML Util for item ruleset (or maybe custom instead)
- Move `yuria.transformerlib` to subproject instead of putting it here
- Add more mods compatibility (will be a submod)

## Building StackUpper
#### Requiurements
- Java 21 (adoptium, zulu, etc...)

##### Run
- Unix
    -  ``` ./gradlew build ```
- Windows
  - ```./gradlew.bat build```

##### Using
- Open your file explorer or `cd` to `./build/libs`
- Move the file stackupper-VERSION-all.jar to your mods folder
