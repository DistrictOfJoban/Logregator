package com.lx862.logregator.mixin;

import com.lx862.logregator.EventAction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final public Player player;

    @Inject(method= "addResource(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"))
    public void addStack(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        EventAction.checkLoggedItems(player, itemStack);
    }
}
