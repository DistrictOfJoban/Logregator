package com.lx.logregator.mixin;

import com.lx.logregator.EventAction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method= "emitGameEvent", at = @At("HEAD"))
    public void emitGameEvent(Entity entity, GameEvent gameEvent, BlockPos pos, int range, CallbackInfo ci) {
        EventAction.onWorldEvent(entity, gameEvent, pos);
    }
}
