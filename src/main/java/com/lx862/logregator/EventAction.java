package com.lx862.logregator;

import com.lx862.logregator.config.LogregatorConfig;
import com.lx862.logregator.data.event.BlockDestroyEvent;
import com.lx862.logregator.data.event.BlockOpenEvent;
import com.lx862.logregator.data.event.BlockPlaceEvent;
import com.lx862.logregator.data.event.FilteredItemEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.HashMap;

public class EventAction {
    public static void checkLoggedItems(Player player, ItemStack stack) {
        String thisStackId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        for(FilteredItemEvent entry : LogregatorConfig.filteredItems) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("itemId", thisStackId);
            entry.send(player, null, hashMap);
        }
    }

    public static void onWorldEvent(Entity entity, GameEvent gameEvent, BlockPos pos) {
        if(entity == null || !entity.isAlwaysTicking()) return;
        Player player = (Player) entity;
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