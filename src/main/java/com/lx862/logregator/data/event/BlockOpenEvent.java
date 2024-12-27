package com.lx862.logregator.data.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lx862.logregator.Util;
import com.lx862.logregator.config.LogregatorConfig;
import com.lx862.logregator.data.Area;
import com.lx862.logregator.data.webhook.DiscordEmbed;
import com.lx862.logregator.data.webhook.DiscordWebhook;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockOpenEvent extends Event {
    private final String blockId;
    private final Area area;
    public BlockOpenEvent(String blockId, Area area, List<Integer> permLevel) {
        super(permLevel);
        this.blockId = blockId;
        this.area = area;
    }

    @Override
    public void send(Player player, BlockPos pos, HashMap<String, String> extraData) {
        if(area != null && !area.isInArea(pos)) return;
        if(!hasPermLevel(player)) return;
        String affectedBlockId = BuiltInRegistries.BLOCK.getKey(player.getLevel().getBlockState(pos).getBlock()).toString();
        if(blockId != null && !affectedBlockId.equals(blockId)) return;

        DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
        webhook.addEmbed(new DiscordEmbed()
                .setAuthor(player.getGameProfile().getName(), null, "https://minotar.net/avatar/" + player.getGameProfile().getName() + "/16")
                .setTitle(":warning: Player opened block")
                .setDescription(String.format("Player %s opened a a **%s** at %s\nStanding at: `%s`", player.getGameProfile().getName(), affectedBlockId, Util.formatBlockPos(pos), Util.formatBlockPos(player.blockPosition())))
                .setTimestamp()
        );
        try {
            webhook.send();
        } catch (IOException ignored) {
        }
    }

    public static BlockOpenEvent fromJson(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        final String blockId;
        if(object.has("blockId")) {
            blockId = object.get("blockId").getAsString();
        } else {
            blockId = null;
        }

        final Area area;
        if(object.has("area")) {
            area = Area.fromJson(object.get("area").getAsJsonObject());
        } else {
            area = null;
        }

        List<Integer> permLevel = new ArrayList<>();
        if(object.has("permLevel")) {
            permLevel.addAll(Util.fromJsonArray(object.get("permLevel").getAsJsonArray()));
        }

        return new BlockOpenEvent(blockId, area, permLevel);
    }
}
