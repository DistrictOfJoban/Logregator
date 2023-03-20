package com.lx.logregator;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.EventType;
import com.lx.logregator.data.webhook.DiscordEmbed;
import com.lx.logregator.data.webhook.DiscordWebhook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.io.IOException;

public class Events {

    public static void checkLoggedItems(PlayerEntity player, ItemStack stack) {
        String thisStackId = Registry.ITEM.getId(stack.getItem()).toString();
        if(LogregatorConfig.filteredItems.contains(thisStackId)) {
            DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
            webhook.addEmbed(new DiscordEmbed()
                    .setAuthor(player.getGameProfile().getName(), null, "https://minotar.net/avatar/" + player.getGameProfile().getName() + "/16")
                    .setTitle(":warning: Filtered Items in Player's inventory")
                    .setDescription("Player " + player.getGameProfile().getName() + " has obtained **" + thisStackId + "**")
                    .setTimestamp()
            );
            try {
                webhook.send(EventType.FILTERED_ITEM);
            } catch (IOException ignored) {
            }
        }
    }
}