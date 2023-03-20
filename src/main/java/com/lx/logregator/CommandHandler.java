package com.lx.logregator;

import com.lx.logregator.commands.logregator;
import com.mojang.brigadier.CommandDispatcher;

public class CommandHandler {

    public static void registerCMDS(CommandDispatcher<net.minecraft.server.command.ServerCommandSource> dispatcher) {
        logregator.register(dispatcher);
    }
}
