package com.lx.logregator.data.event;

import com.lx.logregator.Util;
import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.Area;
import com.lx.logregator.data.webhook.DiscordEmbed;
import com.lx.logregator.data.webhook.DiscordWebhook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BlockDestroy extends Event {
    private final String blockId;
    private final Area area;
    public BlockDestroy(String blockId, Area area, List<Integer> permLevel) {
        super(permLevel);
        this.blockId = blockId;
        this.area = area;
    }

    @Override
    public void send(PlayerEntity player, BlockPos pos, HashMap<String, String> extraData) {
        if(area != null && !area.isInArea(pos)) return;
        if(!hasPermLevel(player)) return;

        DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
        webhook.addEmbed(new DiscordEmbed()
                .setAuthor(player.getGameProfile().getName(), null, "https://minotar.net/avatar/" + player.getGameProfile().getName() + "/16")
                .setTitle(":warning: Player destroyed block")
                .setDescription(String.format("Player %s broke a **%s** at %s\\nStanding at: `%s`", player.getGameProfile().getName(), blockId, Util.formatBlockPos(pos), Util.formatBlockPos(player.getBlockPos())))
                .setTimestamp()
        );
        try {
            webhook.send(EventType.BLOCK_BREAK);
        } catch (IOException ignored) {
        }
    }
}
