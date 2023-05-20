package com.lx.logregator;

import com.google.gson.JsonArray;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private static int PERM_LEVEL = 4;
    public static String padZero(int i) {
        if(i <= 9) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }

    public static int sliderValueToSpeed(int sliderValue) {
        switch(sliderValue) {
            case 0:
                return 20;
            case 1:
                return 40;
            case 2:
                return 60;
            case 3:
                return 80;
            case 4:
                return 120;
            case 5:
                return 160;
            case 6:
                return 200;
            case 7:
                return 300;
        }
        return 0;
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
        for(int i = 0; i <= PERM_LEVEL; i++) {
            if(player.hasPermissionLevel(PERM_LEVEL - i)) {
                return i;
            }
        }
        return 0;
    }

    public static String formatBlockPos(BlockPos pos) {
        return String.format("`%d,%d,%d`", pos.getX(), pos.getY(), pos.getZ());
    }

    public static List<Integer> fromJsonArray(JsonArray arr) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++) {
            list.add(arr.get(i).getAsInt());
        }
        return list;
    }
}
