package com.fakefps;

import com.fakefps.command.FakeFPSCommand;
import com.fakefps.config.FakeFPSConfig;
import com.fakefps.hud.FakeFPSHudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class FakeFPSMod implements ClientModInitializer {
    public static final String MOD_ID = "fakefps";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Fake FPS Mod...");

        // Load configuration from disk
        FakeFPSConfig.load();

        // Register HUD Renderer
        HudRenderCallback.EVENT.register(new FakeFPSHudRenderer());

        // Register client commands
        ClientCommandRegistrationCallback.EVENT.register(FakeFPSCommand::register);

        LOGGER.info("Fake FPS Mod initialized successfully!");
    }
}
