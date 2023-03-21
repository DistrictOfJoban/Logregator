package com.lx.logregator.data;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.webhook.DiscordEmbed;
import com.lx.logregator.data.webhook.DiscordWebhook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FilteredItem extends Event {
    private final String itemId;
    public FilteredItem(String itemId, List<Integer> permLevel) {
        super(permLevel);
        this.itemId = itemId;
    }

    @Override
    public void send(PlayerEntity player, BlockPos pos, HashMap<String, String> extraData) {
        String itemIdentifier = extraData.get("itemId");
        if(!itemId.equals(itemIdentifier)) return;
        if(!hasPermLevel(player)) return;

        DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
        webhook.addEmbed(new DiscordEmbed()
                .setAuthor(player.getGameProfile().getName(), null, "https://minotar.net/avatar/" + player.getGameProfile().getName() + "/16")
                .setTitle(":warning: Filtered Items in Player's inventory")
                .setDescription("Player " + player.getGameProfile().getName() + " has obtained **" + itemIdentifier + "**")
                .setTimestamp()
        );
        try {
            webhook.send(EventType.FILTERED_ITEM);
        } catch (IOException ignored) {
        }
    }
}
