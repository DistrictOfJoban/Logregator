package com.lx.logregator;

import com.google.gson.JsonArray;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private static int PERM_LEVEL = 5;
    public static String padZero(int i) {
        if(i <= 9) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

    public static String formatTime(long ms, boolean exactTime) {
        int seconds = (int)(ms / 1000);
        int minutes = (int)Math.round(seconds / 60.0);
        if(seconds < 60) {
            return seconds + "s";
        } else {
            if(exactTime) {
                return minutes + " min " + seconds % 60 + " sec";
            } else {
                return minutes + " min";
            }
        }
    }
    public static String getTimeDuration(long n1, long n2) {
        long difference = Math.abs(n1 - n2);
        return formatTime(difference, false);
    }

    public static int getPermissionLevel(PlayerEntity player) {
        for(int i = 0; i < PERM_LEVEL; i++) {
            if(player.hasPermissionLevel(PERM_LEVEL - i)) {
                return i;
            }
        }
        return 0;
    }

    public static List<Integer> fromJsonArray(JsonArray arr) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++) {
            list.add(arr.get(i).getAsInt());
        }
        return list;
    }
}
