package com.lx.logregator;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.event.BlockDestroyEvent;
import com.lx.logregator.data.event.BlockOpenEvent;
import com.lx.logregator.data.event.BlockPlaceEvent;
import com.lx.logregator.data.event.FilteredItemEvent;
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
        for(FilteredItemEvent entry : LogregatorConfig.filteredItems) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("itemId", thisStackId);
            entry.send(player, null, hashMap);
        }
    }

    public static void onWorldEvent(Entity entity, GameEvent gameEvent, BlockPos pos) {
        if(entity == null || !entity.isPlayer()) return;
        PlayerEntity player = (PlayerEntity)entity;
        if(gameEvent == GameEvent.BLOCK_DESTROY) {
            for(BlockDestroyEvent entry : LogregatorConfig.blockBreak) {
                entry.send(player, pos, null);
            }
        }
        if(gameEvent == GameEvent.BLOCK_PLACE) {
            for(BlockPlaceEvent entry : LogregatorConfig.blockPlace) {
                entry.send(player, pos, null);
            }
        }
        if(gameEvent == GameEvent.BLOCK_OPEN) {
            for(BlockOpenEvent entry : LogregatorConfig.blockOpen) {
                entry.send(player, pos, null);
            }
        }
    }
}