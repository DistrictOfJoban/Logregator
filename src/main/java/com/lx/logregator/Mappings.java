package com.lx.logregator;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class Mappings {
    public static MutableText literalText(String content) {
        #if MC_VERSION >= "11900"
            return Text.literal(content);
        #else
            return new net.minecraft.text.LiteralText(content);
        #endif
    }

    public static void registerCommand(Consumer<CommandDispatcher<ServerCommandSource>> callback) {
        #if MC_VERSION < "11900"
            net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> callback.accept(dispatcher));
        #elif MC_VERSION >= "11900"
            net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> callback.accept(dispatcher));
        #endif
    }
}
