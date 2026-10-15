package com.fakefps.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles persistent configuration for the Fake FPS mod.
 * Saved to config/fakefps.json in Minecraft's working directory.
 */
public class FakeFPSConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("FakeFPS/Config");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "fakefps.json";

    private static FakeFPSConfig INSTANCE = new FakeFPSConfig();

    // Configuration Settings
    public boolean enabled = true;
    public FPSMode mode = FPSMode.FIXED;

    public int fixedFps = 999;
    public int randomMinFps = 800;
    public int randomMaxFps = 1200;

    public String prefix = "FPS: ";
    public String suffix = "";

    public String textColor = "green";
    public boolean bold = false;
    public boolean shadow = true;
    public boolean background = false;
    public String backgroundColor = "#60000000";

    public int xOffset = 4;
    public int yOffset = 4;

    public boolean hideInF3 = true;
    public double smoothingSpeed = 0.15;
    public int updateIntervalMs = 150;

    public static FakeFPSConfig getInstance() {
        return INSTANCE;
    }

    public static void setInstance(FakeFPSConfig instance) {
        if (instance != null) {
            INSTANCE = instance;
        }
    }

    private static Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);
    }

    /**
     * Loads configuration from disk, creating default file if missing.
     */
    public static void load() {
        Path path = getConfigPath();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                FakeFPSConfig loaded = GSON.fromJson(reader, FakeFPSConfig.class);
                if (loaded != null) {
                    INSTANCE = loaded;
                    INSTANCE.validate();
                    LOGGER.info("Fake FPS configuration loaded successfully.");
                    return;
                }
            } catch (Exception e) {
                LOGGER.error("Failed to load Fake FPS configuration, using defaults.", e);
            }
        }

        // If file doesn't exist or loading failed, save default
        INSTANCE = new FakeFPSConfig();
        save();
    }

    /**
     * Saves current configuration to disk.
     */
    public static void save() {
        Path path = getConfigPath();
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save Fake FPS configuration.", e);
        }
    }

    /**
     * Validates and normalizes settings.
     */
    public void validate() {
        if (randomMinFps < 0) randomMinFps = 0;
        if (randomMaxFps < randomMinFps) randomMaxFps = randomMinFps;
        if (fixedFps < 0) fixedFps = 0;
        if (smoothingSpeed <= 0.0 || smoothingSpeed > 1.0) smoothingSpeed = 0.15;
        if (updateIntervalMs < 20) updateIntervalMs = 20;
        if (prefix == null) prefix = "FPS: ";
        if (suffix == null) suffix = "";
        if (textColor == null || textColor.trim().isEmpty()) textColor = "green";
        if (mode == null) mode = FPSMode.FIXED;
    }

    /**
     * Resolves the text color as an ARGB / RGB integer.
     * Supports named colors, hex strings, and animated rainbow mode.
     */
    public int resolveTextColor() {
        String col = textColor.trim().toLowerCase();

        switch (col) {
            case "green":
                return 0xFF55FF55;
            case "white":
                return 0xFFFFFFFF;
            case "yellow":
                return 0xFFFFFF55;
            case "red":
                return 0xFFFF5555;
            case "aqua":
            case "cyan":
                return 0xFF55FFFF;
            case "gold":
            case "orange":
                return 0xFFFFAA00;
            case "blue":
                return 0xFF5555FF;
            case "purple":
            case "magenta":
            case "pink":
                return 0xFFFF55FF;
            case "gray":
            case "grey":
                return 0xFFAAAAAA;
            case "rainbow":
            case "chroma": {
                // Smooth RGB rainbow wave
                float hue = (System.currentTimeMillis() % 4000L) / 4000.0f;
                int rgb = java.awt.Color.HSBtoRGB(hue, 0.8f, 1.0f);
                return 0xFF000000 | (rgb & 0x00FFFFFF);
            }
            default:
                if (col.startsWith("#")) {
                    try {
                        long parsed = Long.parseLong(col.substring(1), 16);
                        if (col.length() <= 7) {
                            return 0xFF000000 | (int) parsed;
                        } else {
                            return (int) parsed;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                return 0xFF55FF55; // default green
        }
    }

    /**
     * Resolves background color as ARGB integer.
     */
    public int resolveBackgroundColor() {
        if (backgroundColor != null && backgroundColor.startsWith("#")) {
            try {
                long parsed = Long.parseLong(backgroundColor.substring(1), 16);
                if (backgroundColor.length() <= 7) {
                    return 0x80000000 | (int) parsed;
                } else {
                    return (int) parsed;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return 0x60000000; // Semi-transparent black default
    }
}
