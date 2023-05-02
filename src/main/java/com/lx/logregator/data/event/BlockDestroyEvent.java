package com.lx.logregator.data.event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lx.logregator.Util;
import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.Area;
import com.lx.logregator.data.webhook.DiscordEmbed;
import com.lx.logregator.data.webhook.DiscordWebhook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockDestroyEvent extends Event {
    private final String blockId;
    private final Area area;
    public BlockDestroyEvent(String blockId, Area area, List<Integer> permLevel) {
        super(permLevel);
        this.blockId = blockId;
        this.area = area;
    }

    @Override
    public void send(PlayerEntity player, BlockPos pos, HashMap<String, String> extraData) {
        if(area != null && !area.isInArea(pos)) return;
        if(!hasPermLevel(player)) return;
        String affectedBlockId = Registry.BLOCK.getId(player.world.getBlockState(pos).getBlock()).toString();
        if(blockId != null && !affectedBlockId.equals(blockId)) return;

        DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
        webhook.addEmbed(new DiscordEmbed()
                .setAuthor(player.getGameProfile().getName(), null, "https://minotar.net/avatar/" + player.getGameProfile().getName() + "/16")
                .setTitle(":warning: Player destroyed block")
                .setDescription(String.format("Player %s broke a **%s** at %s\nStanding at: `%s`", player.getGameProfile().getName(), affectedBlockId, Util.formatBlockPos(pos), Util.formatBlockPos(player.getBlockPos())))
                .setTimestamp()
        );
        try {
            webhook.send();
        } catch (IOException ignored) {
        }
    }

    public static BlockDestroyEvent fromJson(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        String blockId;
        if(object.has("blockId")) {
            blockId = object.get("blockId").getAsString();
        } else {
            blockId = null;
        }
        Area area;
        if(object.has("area")) {
            area = new Area(object.get("area").getAsJsonObject());
        } else {
            area = null;
        }

        List<Integer> permLevel = new ArrayList<>();
        if(object.has("permLevel")) {
            permLevel.addAll(Util.fromJsonArray(object.get("permLevel").getAsJsonArray()));
        }

        return new BlockDestroyEvent(blockId, area, permLevel);
    }
}
