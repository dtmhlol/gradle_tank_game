# Gradle Tank Game (Processing)

A 2D turn-based tank game written in **Java** using the **Processing** library, built with **Gradle**.  
The game loads multiple levels from `config.json` (with map layouts in `level1.txt`, `level2.txt`, `level3.txt`), shows a start screen, runs the match, and displays a leaderboard at the end.

## Features
- Multiple levels defined via `config.json`
- Start screen + logo
- Turn-based tank gameplay (move, aim, fire)
- Wind affecting shots
- In-game shop (fuel / health / parachute)
- End-of-game leaderboard
- Debug mode shortcuts (if enabled in code)

## Tech stack
- Java (Gradle)
- [Processing](https://processing.org/) (`processing.core.PApplet`)
- JUnit (test dependencies configured)

## Project structure
- `build.gradle` — Gradle build config (application main class is `Tanks.App`)
- `config.json` — level list, backgrounds, optional tree sprites, player colours
- `level1.txt`, `level2.txt`, `level3.txt` — level layouts
- `src/main/java/Tanks/` — Java source (main entrypoint: `App.java`)
- `src/main/resources/Tanks/` — images (backgrounds, UI assets, sprites)

## Requirements
- Java (a modern JDK is recommended)
- Gradle (or use the Gradle wrapper if you add one)

## How to build and run

### Run with Gradle
From the repository root:

```bash
./gradlew run
```

If you’re on Windows:

```bat
gradlew.bat run
```

### Build a runnable JAR
```bash
./gradlew jar
```

The JAR will be produced under:

```text
build/libs/
```

## Controls

### Gameplay
- **Left / Right arrow**: move tank (when it’s your turn and you are not falling)
- **Up / Down arrow**: aim turret
- **W / S**: increase / decrease shot power
- **Space**: fire (also ends your turn)
- **R**: buy health (when shop is available)
- **F**: buy fuel (when shop is available)
- **P**: buy parachute (when shop is available)
- **C**: skip/advance to next level (forces level update)

### UI / Flow
- Click **Start** on the start screen to begin.
- Click the **shop icon** (bottom-right area) to open/close the shop overlay.

## Configuration

### Levels (`config.json`)
`config.json` contains an array of levels. Each level includes:
- `layout`: a `levelX.txt` file
- `background`: a PNG in `src/main/resources/Tanks/`
- `foreground-colour`: RGB string like `"255,255,255"`
- optional `trees`: a tree sprite PNG

Example (simplified):
```json
{
  "layout": "level1.txt",
  "background": "snow.png",
  "foreground-colour": "255,255,255",
  "trees": "tree2.png"
}
```

## Main entry point
The game runs from:

- `src/main/java/Tanks/App.java`
- main class: `Tanks.App`

## Assets / credits
This project includes various image and font assets under:
- `src/main/resources/Tanks/`
- `src/main/java/Tanks/*.ttf`
