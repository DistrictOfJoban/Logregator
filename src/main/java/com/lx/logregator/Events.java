package com.lx.logregator;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.EventType;
import com.lx.logregator.data.FilteredItem;
import com.lx.logregator.data.webhook.DiscordEmbed;
import com.lx.logregator.data.webhook.DiscordWebhook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.HashMap;

public class Events {

    public static void checkLoggedItems(PlayerEntity player, ItemStack stack) {
        String thisStackId = Registry.ITEM.getId(stack.getItem()).toString();
        for(FilteredItem entry : LogregatorConfig.filteredItems) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("itemId", thisStackId);
            entry.send(player, player.getBlockPos(), hashMap);
        }
    }
}