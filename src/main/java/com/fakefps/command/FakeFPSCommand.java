package com.fakefps.command;

import com.fakefps.config.FPSMode;
import com.fakefps.config.FakeFPSConfig;
import com.fakefps.simulator.FakeFPSSimulator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Registers and handles the /fakefps client commands.
 */
public class FakeFPSCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("fakefps")
                // /fakefps (status & help)
                .executes(context -> showStatus(context.getSource()))

                // /fakefps on
                .then(ClientCommandManager.literal("on")
                        .executes(context -> {
                            FakeFPSConfig config = FakeFPSConfig.getInstance();
                            config.enabled = true;
                            FakeFPSConfig.save();
                            sendPrefixFeedback(context.getSource(), Text.literal("Counter ").formatted(Formatting.GRAY)
                                    .append(Text.literal("ENABLED").formatted(Formatting.GREEN, Formatting.BOLD)));
                            return 1;
                        }))

                // /fakefps off
                .then(ClientCommandManager.literal("off")
                        .executes(context -> {
                            FakeFPSConfig config = FakeFPSConfig.getInstance();
                            config.enabled = false;
                            FakeFPSConfig.save();
                            sendPrefixFeedback(context.getSource(), Text.literal("Counter ").formatted(Formatting.GRAY)
                                    .append(Text.literal("DISABLED").formatted(Formatting.RED, Formatting.BOLD)));
                            return 1;
                        }))

                // /fakefps toggle
                .then(ClientCommandManager.literal("toggle")
                        .executes(context -> {
                            FakeFPSConfig config = FakeFPSConfig.getInstance();
                            config.enabled = !config.enabled;
                            FakeFPSConfig.save();
                            Formatting color = config.enabled ? Formatting.GREEN : Formatting.RED;
                            String state = config.enabled ? "ENABLED" : "DISABLED";
                            sendPrefixFeedback(context.getSource(), Text.literal("Counter ").formatted(Formatting.GRAY)
                                    .append(Text.literal(state).formatted(color, Formatting.BOLD)));
                            return 1;
                        }))

                // /fakefps set <number>
                .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.argument("fps", IntegerArgumentType.integer(0))
                                .executes(context -> {
                                    int fps = IntegerArgumentType.getInteger(context, "fps");
                                    FakeFPSConfig config = FakeFPSConfig.getInstance();
                                    config.mode = FPSMode.FIXED;
                                    config.fixedFps = fps;
                                    config.enabled = true;
                                    FakeFPSConfig.save();
                                    FakeFPSSimulator.getInstance().reset(fps);

                                    sendPrefixFeedback(context.getSource(), Text.literal("Fixed FPS set to ").formatted(Formatting.GRAY)
                                            .append(Text.literal(String.valueOf(fps)).formatted(Formatting.GREEN, Formatting.BOLD))
                                            .append(Text.literal(" (Mode: FIXED)").formatted(Formatting.DARK_GRAY)));
                                    return 1;
                                })))

                // /fakefps random <min> <max>
                .then(ClientCommandManager.literal("random")
                        .then(ClientCommandManager.argument("min", IntegerArgumentType.integer(0))
                                .then(ClientCommandManager.argument("max", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            int min = IntegerArgumentType.getInteger(context, "min");
                                            int max = IntegerArgumentType.getInteger(context, "max");

                                            if (min > max) {
                                                int temp = min;
                                                min = max;
                                                max = temp;
                                            }

                                            FakeFPSConfig config = FakeFPSConfig.getInstance();
                                            config.mode = FPSMode.RANDOM;
                                            config.randomMinFps = min;
                                            config.randomMaxFps = max;
                                            config.enabled = true;
                                            FakeFPSConfig.save();
                                            FakeFPSSimulator.getInstance().reset((min + max) / 2);

                                            sendPrefixFeedback(context.getSource(), Text.literal("Random FPS range set to ").formatted(Formatting.GRAY)
                                                    .append(Text.literal(min + " - " + max).formatted(Formatting.AQUA, Formatting.BOLD))
                                                    .append(Text.literal(" (Mode: RANDOM)").formatted(Formatting.DARK_GRAY)));
                                            return 1;
                                        }))))

                // /fakefps real
                .then(ClientCommandManager.literal("real")
                        .executes(context -> {
                            FakeFPSConfig config = FakeFPSConfig.getInstance();
                            config.mode = FPSMode.REAL;
                            config.enabled = true;
                            FakeFPSConfig.save();

                            sendPrefixFeedback(context.getSource(), Text.literal("Now displaying ").formatted(Formatting.GRAY)
                                    .append(Text.literal("REAL GAME FPS").formatted(Formatting.YELLOW, Formatting.BOLD))
                                    .append(Text.literal(" (Mode: REAL)").formatted(Formatting.DARK_GRAY)));
                            return 1;
                        }))

                // /fakefps reload
                .then(ClientCommandManager.literal("reload")
                        .executes(context -> {
                            FakeFPSConfig.load();
                            sendPrefixFeedback(context.getSource(), Text.literal("Configuration ").formatted(Formatting.GRAY)
                                    .append(Text.literal("RELOADED").formatted(Formatting.GREEN, Formatting.BOLD))
                                    .append(Text.literal(" from disk.").formatted(Formatting.GRAY)));
                            return 1;
                        }))

                // /fakefps color <color>
                .then(ClientCommandManager.literal("color")
                        .then(ClientCommandManager.argument("color", StringArgumentType.word())
                                .executes(context -> {
                                    String colorStr = StringArgumentType.getString(context, "color");
                                    FakeFPSConfig config = FakeFPSConfig.getInstance();
                                    config.textColor = colorStr;
                                    FakeFPSConfig.save();

                                    sendPrefixFeedback(context.getSource(), Text.literal("Text color set to ").formatted(Formatting.GRAY)
                                            .append(Text.literal(colorStr).formatted(Formatting.WHITE, Formatting.BOLD)));
                                    return 1;
                                })))

                // /fakefps pos <x> <y>
                .then(ClientCommandManager.literal("pos")
                        .then(ClientCommandManager.argument("x", IntegerArgumentType.integer(0))
                                .then(ClientCommandManager.argument("y", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            int x = IntegerArgumentType.getInteger(context, "x");
                                            int y = IntegerArgumentType.getInteger(context, "y");
                                            FakeFPSConfig config = FakeFPSConfig.getInstance();
                                            config.xOffset = x;
                                            config.yOffset = y;
                                            FakeFPSConfig.save();

                                            sendPrefixFeedback(context.getSource(), Text.literal("Position set to X: ").formatted(Formatting.GRAY)
                                                    .append(Text.literal(String.valueOf(x)).formatted(Formatting.YELLOW))
                                                    .append(Text.literal(", Y: ").formatted(Formatting.GRAY))
                                                    .append(Text.literal(String.valueOf(y)).formatted(Formatting.YELLOW)));
                                            return 1;
                                        }))))

                // /fakefps prefix <text>
                .then(ClientCommandManager.literal("prefix")
                        .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                                .executes(context -> {
                                    String prefix = StringArgumentType.getString(context, "text");
                                    FakeFPSConfig config = FakeFPSConfig.getInstance();
                                    config.prefix = prefix;
                                    FakeFPSConfig.save();

                                    sendPrefixFeedback(context.getSource(), Text.literal("Prefix set to: '").formatted(Formatting.GRAY)
                                            .append(Text.literal(prefix).formatted(Formatting.WHITE))
                                            .append(Text.literal("'").formatted(Formatting.GRAY)));
                                    return 1;
                                })))

                // /fakefps style <shadow|bold|bg> <true|false>
                .then(ClientCommandManager.literal("style")
                        .then(ClientCommandManager.argument("property", StringArgumentType.word())
                                .then(ClientCommandManager.argument("value", StringArgumentType.word())
                                        .executes(context -> {
                                            String prop = StringArgumentType.getString(context, "property").toLowerCase();
                                            boolean val = Boolean.parseBoolean(StringArgumentType.getString(context, "value"));
                                            FakeFPSConfig config = FakeFPSConfig.getInstance();

                                            switch (prop) {
                                                case "shadow":
                                                    config.shadow = val;
                                                    break;
                                                case "bold":
                                                    config.bold = val;
                                                    break;
                                                case "bg":
                                                case "background":
                                                    config.background = val;
                                                    break;
                                                default:
                                                    sendPrefixFeedback(context.getSource(), Text.literal("Unknown style property: " + prop + " (use shadow, bold, bg)").formatted(Formatting.RED));
                                                    return 0;
                                            }
                                            FakeFPSConfig.save();
                                            sendPrefixFeedback(context.getSource(), Text.literal("Style '" + prop + "' set to " + val).formatted(Formatting.GREEN));
                                            return 1;
                                        }))))
        );
    }

    private static int showStatus(FabricClientCommandSource source) {
        FakeFPSConfig config = FakeFPSConfig.getInstance();

        MutableText header = Text.literal("--- Fake FPS Mod v1.0.0 ---").formatted(Formatting.GOLD, Formatting.BOLD);
        source.sendFeedback(header);

        source.sendFeedback(Text.literal(" Status: ").formatted(Formatting.GRAY)
                .append(config.enabled ? Text.literal("ENABLED").formatted(Formatting.GREEN, Formatting.BOLD) : Text.literal("DISABLED").formatted(Formatting.RED, Formatting.BOLD))
                .append(Text.literal(" | Mode: ").formatted(Formatting.GRAY))
                .append(Text.literal(config.mode.name()).formatted(Formatting.AQUA, Formatting.BOLD)));

        source.sendFeedback(Text.literal(" Fixed FPS: ").formatted(Formatting.GRAY)
                .append(Text.literal(String.valueOf(config.fixedFps)).formatted(Formatting.WHITE))
                .append(Text.literal(" | Random Range: ").formatted(Formatting.GRAY))
                .append(Text.literal(config.randomMinFps + " - " + config.randomMaxFps).formatted(Formatting.WHITE)));

        source.sendFeedback(Text.literal(" Position: ").formatted(Formatting.GRAY)
                .append(Text.literal("X=" + config.xOffset + ", Y=" + config.yOffset).formatted(Formatting.WHITE))
                .append(Text.literal(" | Color: ").formatted(Formatting.GRAY))
                .append(Text.literal(config.textColor).formatted(Formatting.WHITE))
                .append(Text.literal(" | Shadow: ").formatted(Formatting.GRAY))
                .append(Text.literal(String.valueOf(config.shadow)).formatted(Formatting.WHITE)));

        // Interactive command shortcuts
        MutableText cmds = Text.literal(" Quick commands: ").formatted(Formatting.GRAY);
        cmds.append(createClickableCommand("/fakefps on", Formatting.GREEN));
        cmds.append(Text.literal(" "));
        cmds.append(createClickableCommand("/fakefps off", Formatting.RED));
        cmds.append(Text.literal(" "));
        cmds.append(createClickableCommand("/fakefps set 999", Formatting.YELLOW));
        cmds.append(Text.literal(" "));
        cmds.append(createClickableCommand("/fakefps random 800 1200", Formatting.AQUA));
        cmds.append(Text.literal(" "));
        cmds.append(createClickableCommand("/fakefps real", Formatting.LIGHT_PURPLE));
        source.sendFeedback(cmds);

        return 1;
    }

    private static MutableText createClickableCommand(String cmd, Formatting color) {
        return Text.literal("[" + cmd + "]").formatted(color)
                .styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to suggest: " + cmd))));
    }

    private static void sendPrefixFeedback(FabricClientCommandSource source, Text message) {
        MutableText prefix = Text.literal("[FakeFPS] ").formatted(Formatting.DARK_GREEN, Formatting.BOLD);
        source.sendFeedback(prefix.append(message));
    }
}
