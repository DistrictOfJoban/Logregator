package com.lx862.logregator;

import com.lx862.logregator.command.LogregatorCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class CommandHandler {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        LogregatorCommand.register(dispatcher);
    }
}
