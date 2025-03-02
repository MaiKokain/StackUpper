## Custom ruleset (or scripting) guide

Check the [wiki](https://github.com/MaiKokain/StackUpper/wiki/Scripting)

## Building StackUpper
### Prerequisite
- Java Development Kit (JDK) 21

> [!NOTE]
> Currently to build stackupper, you will also need to build [StackUpperConfigLib](https://github.com/MaiKokain/StackUpperConfigLib) and have it published to maven local

### Building
### Clone the repository
```
git clone https://github.com/MaiKokain/StackUpper.git
cd StackUpper
```

### Building the mod
```bash
./gradlew chiseledBuild

# Windows
gradlew.bat chiseledBuild
```
The build output is in `/versions/<minecraft_version>/build/libs/`