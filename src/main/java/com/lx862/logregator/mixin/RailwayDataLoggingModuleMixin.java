package com.lx862.logregator.mixin;

import com.lx862.logregator.Logregator;
import mtr.data.RailwayDataLoggingModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RailwayDataLoggingModule.class)
public class RailwayDataLoggingModuleMixin {
    @Inject(method = "addEvent(Lnet/minecraft/server/level/ServerPlayer;Ljava/lang/Class;JLjava/lang/String;Ljava/util/List;Ljava/util/List;[Lnet/minecraft/core/BlockPos;)V", at = @At("HEAD"))
    public void addEvent(ServerPlayer player, Class<?> dataClass, long id, String name, List<String> oldData, List<String> newData, BlockPos[] positions, CallbackInfo ci) {
        Logregator.mtrLoggingManager.send(player, dataClass, id, name, oldData, newData, positions);
    }
}