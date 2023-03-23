package com.lx.logregator;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.event.BlockDestroy;
import com.lx.logregator.data.event.FilteredItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

import java.util.HashMap;

public class EventAction {
    public static void checkLoggedItems(PlayerEntity player, ItemStack stack) {
        String thisStackId = Registry.ITEM.getId(stack.getItem()).toString();
        for(FilteredItem entry : LogregatorConfig.filteredItems) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("itemId", thisStackId);
            entry.send(player, null, hashMap);
        }
    }

    public static void onWorldEvent(Entity entity, GameEvent gameEvent, BlockPos pos) {
        if(entity == null || !entity.isPlayer()) return;
        if(gameEvent == GameEvent.BLOCK_DESTROY) {
            PlayerEntity player = (PlayerEntity)entity;
            for(BlockDestroy entry : LogregatorConfig.blockBreak) {
                entry.send(player, pos, null);
            }
        }
    }
}