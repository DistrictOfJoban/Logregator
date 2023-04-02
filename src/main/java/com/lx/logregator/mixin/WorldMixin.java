package com.lx.logregator.mixin;

import com.lx.logregator.EventAction;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class WorldMixin {

    @Inject(method= "emitGameEvent", at = @At("HEAD"))
    #if MC_VERSION >= "11900"
        public void emitGameEvent(GameEvent gameEvent, Vec3d pos, GameEvent.Emitter emitter, CallbackInfo ci) {
            EventAction.onWorldEvent(emitter.sourceEntity(), gameEvent, new BlockPos(pos));
        }
    #else
        public void emitGameEvent(Entity entity, GameEvent gameEvent, BlockPos pos, CallbackInfo ci) {
            EventAction.onWorldEvent(entity, gameEvent, pos);
        }
    #endif
}
