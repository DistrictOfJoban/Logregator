package com.lx.logregator.data;

import com.lx.logregator.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;

public abstract class Event {
    protected final List<Integer> permLevel;
    public Event(List<Integer> permLevel) {
        this.permLevel = permLevel;
    }

    public boolean hasPermLevel(PlayerEntity player) {
        int playerPerm = Util.getPermissionLevel(player);
        return permLevel.isEmpty() || permLevel.contains(playerPerm);
    }

    public abstract void send(PlayerEntity player, BlockPos pos, HashMap<String, String> extraData);
}
