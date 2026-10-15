package com.fakefps.hud;

import com.fakefps.config.FakeFPSConfig;
import com.fakefps.simulator.FakeFPSSimulator;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles rendering the Fake FPS counter on the Minecraft HUD.
 * Protected against runtime version mismatches.
 */
public class FakeFPSHudRenderer implements HudRenderCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger("FakeFPS/HUD");
    private static boolean errorLogged = false;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        try {
            FakeFPSConfig config = FakeFPSConfig.getInstance();

            // Check if disabled
            if (!config.enabled) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.world == null) {
                return;
            }

            // Hide when player has F1 HUD hidden enabled
            if (client.options.hudHidden) {
                return;
            }

            // Hide when F3 debug screen is open if configured
            if (config.hideInF3 && client.getDebugHud().shouldShowDebugHud()) {
                return;
            }

            // Calculate displayed FPS value
            int fps = FakeFPSSimulator.getInstance().getFps(config, client);

            // Construct string
            String fullString = (config.prefix != null ? config.prefix : "")
                    + fps
                    + (config.suffix != null ? config.suffix : "");

            MutableText text = Text.literal(fullString);
            if (config.bold) {
                text = text.formatted(Formatting.BOLD);
            }

            TextRenderer textRenderer = client.textRenderer;
            int x = config.xOffset;
            int y = config.yOffset;
            int color = config.resolveTextColor();

            // Draw background box if enabled
            if (config.background) {
                int width = textRenderer.getWidth(text);
                int height = textRenderer.fontHeight;
                int bgColor = config.resolveBackgroundColor();
                RenderHelper.fill(drawContext, x - 2, y - 2, x + width + 2, y + height + 2, bgColor);
            }

            // Render text with optional shadow using cross-version RenderHelper
            RenderHelper.drawText(drawContext, textRenderer, text, x, y, color, config.shadow);
        } catch (Throwable t) {
            // Guard against any runtime exceptions so the game and server connection never crash
            if (!errorLogged) {
                errorLogged = true;
                LOGGER.error("Error rendering Fake FPS HUD element", t);
            }
        }
    }
}
