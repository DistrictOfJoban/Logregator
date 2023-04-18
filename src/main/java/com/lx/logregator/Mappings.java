package com.lx.logregator;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class Mappings {
    public static MutableText literalText(String content) {
        return Text.literal(content);
    }

    public static void registerCommand(Consumer<CommandDispatcher<ServerCommandSource>> callback) {
            net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> callback.accept(dispatcher));
    }
}
