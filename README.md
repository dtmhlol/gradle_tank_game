# Gradle Tank Game (Processing)

A **2D turn-based tank game** written in **Java** using the **Processing** library and built with **Gradle**.

The game loads multiple levels from `config.json`, uses map layouts defined in `level1.txt`, `level2.txt`, and `level3.txt`, and features a start screen, gameplay loop, in-game shop, and an end-of-game leaderboard.

---

# Features

* Multiple levels configured via `config.json`
* Start screen with logo
* Turn-based tank gameplay
* Adjustable turret aim and firing power
* Wind affecting projectile physics
* In-game shop for upgrades
* Multiple level backgrounds
* End-of-game leaderboard
* Optional debug shortcuts (if enabled in code)

---

# Tech Stack

* **Java**
* **Gradle** (build system)
* **Processing** (`processing.core.PApplet`)
* **JUnit** (test dependencies configured)

---

# Project Structure

```
project-root/
│
├─ build.gradle                 # Gradle build configuration
├─ config.json                  # Level configuration
├─ level1.txt                   # Level layout
├─ level2.txt
├─ level3.txt
│
├─ src/
│  ├─ main/
│  │  ├─ java/Tanks/
│  │  │     App.java            # Main entry point
│  │  │     *.java              # Game logic classes
│  │  │
│  │  └─ resources/Tanks/
│  │        *.png               # Backgrounds and sprites
│  │
│  └─ test/
│        *.java                 # Unit tests
```

Main application class:

```
Tanks.App
```

---

# Requirements

To run the project you must have:

* **Java 8 or later**
* **Gradle installed locally**

Check installations:

```
java -version
gradle -v
```

Note: This repository **does not include a Gradle wrapper**, so `./gradlew` or `gradlew.bat` will not work unless you generate the wrapper yourself.

---

# How to Build and Run

Run all commands from the **repository root directory**.

## Run the game

```
gradle run
```

Gradle will:

1. Compile the Java code
2. Resolve dependencies
3. Launch the Processing application

---
# Game Controls

## Gameplay Controls

| Key                | Action                           |
| ------------------ | -------------------------------- |
| Left / Right Arrow | Move tank                        |
| Up / Down Arrow    | Adjust turret angle              |
| W / S              | Increase / decrease firing power |
| Space              | Fire weapon and end turn         |
| C                  | Skip / advance to next level     |

Movement only works when:

* it is the player's turn
* the tank is not falling
* the tank has fuel remaining

---

## Shop Controls

The shop can be opened by clicking the **shop icon** in the bottom-right corner.

| Key | Action        |
| --- | ------------- |
| R   | Buy health    |
| F   | Buy fuel      |
| P   | Buy parachute |

---

# Configuration

## Levels (`config.json`)

Levels are defined in `config.json`.

Each level specifies:

* the terrain layout file
* the background image
* foreground colour
* optional tree sprites

Example:

```json
{
  "layout": "level1.txt",
  "background": "snow.png",
  "foreground-colour": "255,255,255",
  "trees": "tree2.png"
}
```

### Fields

| Field               | Description              |
| ------------------- | ------------------------ |
| `layout`            | Terrain layout text file |
| `background`        | Background image         |
| `foreground-colour` | RGB terrain colour       |
| `trees`             | Optional tree sprite     |

---

## Level Layout Files

Files such as:

```
level1.txt
level2.txt
level3.txt
```

define terrain shape and initial tank placements.

These files are read when a level is loaded.

---

# Assets

Game assets are located in:

```
src/main/resources/Tanks/
```

Examples include:

* background images
* tank sprites
* UI elements
* tree sprites

Fonts may also be included with the game source.

---

# Debug Features

If debug mode is enabled in the code, additional shortcuts may be available for:

* forcing level changes
* skipping turns
* testing shop behaviour

These shortcuts are intended for development and testing.

---

# Notes

* Run commands **from the project root**, otherwise resource files may not load correctly.
* Ensure `config.json` and level files remain in the repository root.
* Ensure all required images and assets are included in `src/main/resources/Tanks/`.

---

# Credits

This project uses the **Processing** framework for graphics and input handling.

Processing website:
[https://processing.org/](https://processing.org/)

---

