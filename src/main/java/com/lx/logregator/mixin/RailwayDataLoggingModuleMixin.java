package com.lx.logregator.mixin;

import com.lx.logregator.Logregator;
import mtr.data.RailwayDataLoggingModule;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RailwayDataLoggingModule.class)
public class RailwayDataLoggingModuleMixin {
    @Inject(method = "addEvent(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Class;JLjava/lang/String;Ljava/util/List;Ljava/util/List;[Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
    public void addEvent(ServerPlayerEntity player, Class<?> dataClass, long id, String name, List<String> oldData, List<String> newData, BlockPos[] positions, CallbackInfo ci) {
        Logregator.mtrLoggingManager.ifPresent(mtrLoggingManager -> {
            mtrLoggingManager.send(player, dataClass, id, name, oldData, newData, positions);
        });
    }
}