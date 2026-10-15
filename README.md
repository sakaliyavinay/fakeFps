# Fake FPS Mod (Minecraft Java Edition)

A lightweight, fully customizable client-side Minecraft mod that displays a **fake, customizable FPS counter** on the player's HUD, matching Minecraft's vanilla UI aesthetic.

![Platform](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen)
![Modloader](https://img.shields.io/badge/Modloader-Fabric-blue)
![Java](https://img.shields.io/badge/Java-21-orange)

---

## ✨ Features

- **Customizable HUD Counter**: Displays FPS cleanly in the corner of your screen (default: top-left `FPS: 999`).
- **Fake FPS Modes**:
  - **Fixed Mode**: Set any custom static number (e.g. `500`, `999`, `1200`, `2400`, or `10000`).
  - **Random Mode**: Smoothly fluctuates between a configurable minimum and maximum range with natural jitter, mimicking authentic game performance without any erratic jumps.
  - **Real FPS Mode**: Instantly toggle to display Minecraft's genuine, real frame rate.
- **Pure Visual Mod**: Completely visual on the HUD. Does not touch or degrade actual game rendering performance.
- **Rich Style & Color Customization**:
  - Named colors: `green`, `white`, `yellow`, `red`, `aqua`, `gold`, `blue`, `purple`, `gray`.
  - Hex colors: `#RRGGBB` or `#AARRGGBB` (e.g., `#FF0055`, `#00FFAA`).
  - Animated Chroma / Rainbow wave: `rainbow` mode smoothly cycles through RGB colors!
  - Text shadow toggle.
  - Bold text toggle.
  - Vanilla-styled semi-transparent background box toggle.
- **HUD Positioning**: Fully customizable X and Y offsets.
- **Clean Vanilla Style**: Automatically hides when F1 (hide HUD) is toggled, and can automatically hide when the F3 debug screen is open.
- **In-Game Commands & Autocomplete**: Complete `/fakefps` command suite with clickable chat feedback and suggestions.
- **Persistent Configuration**: Settings are saved to `config/fakefps.json` and persist across game restarts.

---

## 🎮 Commands

All commands are client-side and work in singleplayer and multiplayer servers without needing server-side installation!

| Command | Description |
|---|---|
| `/fakefps` | View current status, settings summary, and interactive quick commands |
| `/fakefps on` | Enable the HUD FPS counter |
| `/fakefps off` | Disable the HUD FPS counter |
| `/fakefps toggle` | Quick toggle the counter on or off |
| `/fakefps set <number>` | Set a fixed fake FPS value (e.g. `/fakefps set 1440`) |
| `/fakefps random <min> <max>` | Set random mode with smooth range (e.g. `/fakefps random 800 1200`) |
| `/fakefps real` | Toggle to display the actual real game FPS |
| `/fakefps color <color>` | Change text color (`green`, `yellow`, `rainbow`, `#FF5500`, etc.) |
| `/fakefps pos <x> <y>` | Set custom screen position offsets (default: `4 4`) |
| `/fakefps prefix <text>` | Change the prefix (default: `FPS: `) |
| `/fakefps style shadow <true\|false>` | Toggle text drop-shadow |
| `/fakefps style bold <true\|false>` | Toggle bold font |
| `/fakefps style bg <true\|false>` | Toggle semi-transparent background box |
| `/fakefps reload` | Reload configuration from `config/fakefps.json` |

---

## ⚙️ Configuration File (`config/fakefps.json`)

The mod creates a configuration file in your `.minecraft/config/fakefps.json` upon launch:

```json
{
  "enabled": true,
  "mode": "FIXED",
  "fixedFps": 999,
  "randomMinFps": 800,
  "randomMaxFps": 1200,
  "prefix": "FPS: ",
  "suffix": "",
  "textColor": "green",
  "bold": false,
  "shadow": true,
  "background": false,
  "backgroundColor": "#60000000",
  "xOffset": 4,
  "yOffset": 4,
  "hideInF3": true,
  "smoothingSpeed": 0.15,
  "updateIntervalMs": 150
}
```

---

## 📦 Installation Guide

1. Ensure **Minecraft Java Edition 1.21.1** is installed.
2. Install **Fabric Loader** for 1.21.1 from [fabricmc.net](https://fabricmc.net/).
3. Download the **Fabric API** for 1.21.1 from Modrinth or CurseForge and place it into your `.minecraft/mods` folder.
4. Place `fakefps-1.0.0.jar` into your `.minecraft/mods` folder.
5. Launch Minecraft using your Fabric profile!

---

## 🛠️ Building from Source

### Prerequisites
- JDK 21 or higher installed (`java -version`)
- Internet connection (first build downloads Minecraft assets and mappings)

### Build Steps
```bash
# Windows
.\gradlew.bat build

# Linux / macOS
./gradlew build
```

The compiled mod JAR will be generated at:
`build/libs/fakefps-1.0.0.jar`
