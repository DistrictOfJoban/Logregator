package com.lx.logregator.commands;

import com.lx.logregator.Mappings;
import com.lx.logregator.config.LogregatorConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class logregator {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            dispatcher.register(net.minecraft.server.command.CommandManager.literal("logregator")
                .requires(ctx -> ctx.hasPermissionLevel(2))
                .then(CommandManager.literal("reload")
                .executes(context -> {
                    boolean success = LogregatorConfig.load();
                    if(success) {
                        context.getSource().sendFeedback(Mappings.literalText("Logregator config reloaded!").formatted(Formatting.GREEN), false);
                    } else {
                        context.getSource().sendFeedback(Mappings.literalText("Failed to reload Logregator config! Please check console for detail").formatted(Formatting.RED), false);
                    }
                    return 1;
                })));
    }
}
