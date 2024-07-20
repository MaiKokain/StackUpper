# StackUpper (NeoForge)
A minecraft mod that allows changing stacksize from 64 (or 99 (Mojang's max limit)) to 2^30 (1073741824)

Works from 1.20.5 upwards 1.21

> [!IMPORTANT]
> [Midnightlib](https://modrinth.com/mod/midnightlib) is a required dependency

> [!IMPORTANT]
> Tools that have the same component are also stackable (will be fixed in later updates)

### Possible future changes
- Patch `DataComponents` calling `ExtraCodecs.intRange` instead of patching `ExtraCodecs.intRange` directly
- Use PORTB BiggerStack's XML Util for item ruleset (or maybe custom instead)
- Move `yuria.transformerlib` to subproject instead of putting it here
- Add more mods compatibility

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
