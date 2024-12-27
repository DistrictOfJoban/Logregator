package com.lx862.logregator.command;

import com.lx862.logregator.config.LogregatorConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class LogregatorCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("logregator")
            .requires(ctx -> ctx.hasPermission(2))
            .then(Commands.literal("reload")
            .executes(context -> {
                boolean success = LogregatorConfig.load();
                if(success) {
                    context.getSource().sendSuccess(Component.literal("Logregator config reloaded!").withStyle(ChatFormatting.GREEN), false);
                } else {
                    context.getSource().sendSuccess(Component.literal("Failed to reload Logregator config! Please check console for detail").withStyle(ChatFormatting.RED), false);
                }
                return 1;
            }))
        );
    }
}
