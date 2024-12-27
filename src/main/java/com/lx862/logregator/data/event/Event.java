package com.lx862.logregator.data.event;

import com.lx862.logregator.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;

public abstract class Event {
    protected final List<Integer> permLevels;
    public Event(List<Integer> permLevels) {
        this.permLevels = permLevels;
    }

    public boolean hasPermLevel(Player player) {
        int playerPerm = Util.getPermissionLevel(player);
        return permLevels.isEmpty() || permLevels.contains(playerPerm);
    }

    public abstract void send(Player player, BlockPos pos, HashMap<String, String> extraData);
}
