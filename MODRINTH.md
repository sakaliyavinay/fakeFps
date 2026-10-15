# ⚡ Fake FPS — Customizable HUD FPS Counter

> **Want to flex 1000+ FPS in screenshots and videos?**  
> **Fake FPS** is an ultra-lightweight, client-side Minecraft mod that displays a completely customizable, authentic-looking FPS counter directly on your HUD!

> [!IMPORTANT]
> **Performance Note**: This mod **does NOT increase** your actual FPS, nor does it **decrease** your performance. It is a purely visual HUD overlay that changes only the number displayed on your screen, with zero impact on game rendering, FPS, or tick rates.

---

## 📸 Short Description (for Modrinth Summary Field)
> *A lightweight, customizable client-side FPS counter. Show fake FPS values like 999 or 2400, simulate realistic fluctuations, or toggle real FPS! Purely visual — does not increase or decrease game performance.*

---

## 🧩 Required Dependencies

To use this mod, ensure you have the following installed:

| Requirement | Required Version | Link |
|---|---|---|
| **Minecraft** | Java Edition 1.21.x (1.21, 1.21.1, 1.21.11+) | Official Launcher / Prism / Modrinth |
| **Mod Loader** | **Fabric Loader** (v0.16.0 or newer) | [fabricmc.net](https://fabricmc.net/use/installer/) |
| **Required Mod** | **Fabric API** | [Download Fabric API on Modrinth](https://modrinth.com/mod/fabric-api) |
| **Java** | **Java 21** or higher | Bundled with modern Minecraft launchers |

*(No other third-party libraries required! Fully standalone and self-contained.)*

---

## ⚡ Real Performance Impact: None!

- **Does it increase FPS?**  
  **No.** This mod does not alter Minecraft's rendering pipeline, shaders, or engine. It will **not boost** your genuine hardware performance.
- **Does it decrease FPS or cause lag?**  
  **No.** The mod executes in `O(1)` constant time during standard HUD drawing. It creates **no background threads**, performs **no heavy computations**, and uses cached text rendering handles. Your real FPS and ping will remain 100% unaffected.

---

## ✨ Features

- 🎮 **Customizable Fake FPS**:
  - **Fixed Mode**: Display any custom number (e.g. `500`, `999`, `1440`, `2400`, `10000`).
  - **Smooth Random Mode**: Mimics realistic hardware fluctuations within your chosen minimum and maximum range with authentic micro-jitter—no sudden, unnatural jumps!
  - **Real FPS Mode**: Instantly toggle to show your genuine Minecraft frame rate.
- 🎨 **Deep Visual Customization**:
  - **Colors**: Built-in support for named colors (`green`, `white`, `yellow`, `red`, `aqua`, `gold`, `blue`, `purple`, `gray`).
  - **Hex Codes**: Any custom `#RRGGBB` hex color (e.g. `#FF007F`, `#00FFAA`).
  - **Rainbow / Chroma Mode**: Smoothly animated RGB rainbow hue cycle!
  - **Vanilla Style**: Optional text drop shadow, bold styling, or semi-transparent background box.
- 📐 **Custom HUD Positioning**:
  - Adjust the exact X and Y coordinates on your screen with `/fakefps pos <x> <y>`.
  - Automatically hides when the F3 debug screen or F1 HUD toggle is active.
- 💾 **Persistent Settings**:
  - All configurations auto-save to `.minecraft/config/fakefps.json` and persist across restarts.

---

## 🕹️ In-Game Commands

All commands are 100% client-side and can be run in singleplayer or on any multiplayer server!

| Command | Description | Example |
|---|---|---|
| `/fakefps` | View current settings & clickable quick commands | `/fakefps` |
| `/fakefps on` | Enable the FPS counter | `/fakefps on` |
| `/fakefps off` | Disable the counter | `/fakefps off` |
| `/fakefps toggle` | Quick toggle on or off | `/fakefps toggle` |
| `/fakefps set <number>` | Set a fixed FPS value | `/fakefps set 1200` |
| `/fakefps random <min> <max>` | Set smooth random range | `/fakefps random 800 1440` |
| `/fakefps real` | Display your actual game FPS | `/fakefps real` |
| `/fakefps color <color>` | Change text color (name or hex) | `/fakefps color rainbow` |
| `/fakefps pos <x> <y>` | Set screen position offsets | `/fakefps pos 10 10` |
| `/fakefps prefix <text>` | Change prefix label | `/fakefps prefix FPS: ` |
| `/fakefps style shadow <true\|false>` | Toggle text drop-shadow | `/fakefps style shadow true` |
| `/fakefps style bold <true\|false>` | Toggle bold text | `/fakefps style bold true` |
| `/fakefps style bg <true\|false>` | Toggle background box | `/fakefps style bg true` |
| `/fakefps reload` | Reload config from file | `/fakefps reload` |

---

## ⚙️ Configuration File (`config/fakefps.json`)

Settings can also be modified outside the game in `config/fakefps.json`:

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

## ❓ Frequently Asked Questions (FAQ)

### Does this mod increase or decrease my game's performance?
> **Neither.** This mod **does not increase** your real FPS, nor does it **decrease** your performance. It is purely cosmetic—it changes only the digits rendered on your HUD overlay.

### Can I get banned for using this on multiplayer servers?
> **No.** This mod is strictly client-side and visual-only. It sends no network packets, alters no physics, and provides no competitive advantage. It is completely safe to use anywhere.

### Does this require server installation?
> **No.** It runs entirely on your client and works on any server (Hypixel, SMPs, minigames, etc.).

---

## 📥 Installation

1. Install **Fabric Loader** for Minecraft 1.21.x.
2. Put **Fabric API** into your `.minecraft/mods` folder.
3. Download **Fake FPS** (`fakefps-1.0.0.jar`) and drop it into your `.minecraft/mods` folder.
4. Launch Minecraft and type `/fakefps` in chat!
