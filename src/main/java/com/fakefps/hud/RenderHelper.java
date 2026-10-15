package com.fakefps.hud;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * Universal text rendering helper compatible across Minecraft 1.21.0 - 1.21.11+.
 * Seamlessly handles the return type change of DrawContext.drawText (int in 1.21.1 vs void in 1.21.11/1.21.2+).
 */
public class RenderHelper {
    private static MethodHandle drawTextHandle = null;
    private static Method drawTextMethod = null;
    private static boolean initialized = false;

    private static synchronized void init(DrawContext context, TextRenderer textRenderer, Text text) {
        if (initialized) return;
        initialized = true;

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        // 1. Try finding by parameter types with Text
        for (Method m : context.getClass().getMethods()) {
            Class<?>[] p = m.getParameterTypes();
            if (p.length == 6
                    && p[0].isAssignableFrom(textRenderer.getClass())
                    && p[1].isAssignableFrom(text.getClass())
                    && p[2] == int.class
                    && p[3] == int.class
                    && p[4] == int.class
                    && p[5] == boolean.class) {
                try {
                    m.setAccessible(true);
                    drawTextMethod = m;
                    drawTextHandle = lookup.unreflect(m);
                    return;
                } catch (Throwable ignored) {
                }
            }
        }

        // 2. Fallback: try finding by parameter types with String
        for (Method m : context.getClass().getMethods()) {
            Class<?>[] p = m.getParameterTypes();
            if (p.length == 6
                    && p[0].isAssignableFrom(textRenderer.getClass())
                    && p[1] == String.class
                    && p[2] == int.class
                    && p[3] == int.class
                    && p[4] == int.class
                    && p[5] == boolean.class) {
                try {
                    m.setAccessible(true);
                    drawTextMethod = m;
                    drawTextHandle = lookup.unreflect(m);
                    return;
                } catch (Throwable ignored) {
                }
            }
        }
    }

    /**
     * Cross-version safe drawText method.
     */
    public static void drawText(DrawContext context, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow) {
        if (!initialized) {
            init(context, textRenderer, text);
        }

        if (drawTextHandle != null) {
            try {
                if (drawTextMethod != null && drawTextMethod.getParameterTypes()[1] == String.class) {
                    drawTextHandle.invoke(context, textRenderer, text.getString(), x, y, color, shadow);
                } else {
                    drawTextHandle.invoke(context, textRenderer, text, x, y, color, shadow);
                }
                return;
            } catch (Throwable ignored) {
            }
        }

        if (drawTextMethod != null) {
            try {
                if (drawTextMethod.getParameterTypes()[1] == String.class) {
                    drawTextMethod.invoke(context, textRenderer, text.getString(), x, y, color, shadow);
                } else {
                    drawTextMethod.invoke(context, textRenderer, text, x, y, color, shadow);
                }
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Cross-version safe fill method.
     */
    public static void fill(DrawContext context, int x1, int y1, int x2, int y2, int color) {
        try {
            context.fill(x1, y1, x2, y2, color);
        } catch (Throwable t) {
            for (Method m : context.getClass().getMethods()) {
                Class<?>[] p = m.getParameterTypes();
                if (p.length == 5
                        && p[0] == int.class
                        && p[1] == int.class
                        && p[2] == int.class
                        && p[3] == int.class
                        && p[4] == int.class) {
                    try {
                        m.setAccessible(true);
                        m.invoke(context, x1, y1, x2, y2, color);
                        return;
                    } catch (Throwable ignored) {
                    }
                }
            }
        }
    }
}
