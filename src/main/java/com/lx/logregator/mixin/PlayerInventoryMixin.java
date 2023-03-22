package com.lx.logregator.mixin;

import com.lx.logregator.EventAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @Inject(method= "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At("HEAD"))
    public void addStack(int slot, ItemStack stack, CallbackInfoReturnable<Integer> ci) {
        EventAction.checkLoggedItems(player, stack);
    }
}
