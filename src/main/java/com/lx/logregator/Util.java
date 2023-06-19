package com.lx.logregator;

import com.google.gson.JsonArray;
import mtr.data.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static SavedRailBase getAnySavedRailBase(RailwayData railwayData, long id) {
        for(int i = 0; i < 2; i++) {
            boolean isPlatform = i == 0;
            boolean isSiding = i == 1;

            List<SavedRailBase> savedRails = null;

            if(isPlatform) {
                savedRails = railwayData.platforms.stream().filter(e -> e.id == id).collect(Collectors.toList());
            }
            if(isSiding) {
                savedRails = railwayData.sidings.stream().filter(e -> e.id == id).collect(Collectors.toList());
            }

            SavedRailBase savedRailBase = savedRails.size() > 0 ? savedRails.get(0) : null;
            if(savedRailBase != null) {
                return savedRailBase;
            }
        }

        return null;
    }

    public static String findNearestMTRStructure(RailwayData railwayData, BlockPos pos) {
        double closest = 10000000;
        boolean isAbs = false;
        boolean isStation = false;
        String structureName = null;

        for(int i = 0; i < 2; i++) {
            // We already found an area that directly covers the pos, no point in continuing
            if(isAbs) continue;

            boolean searchingStation = false;
            if(i == 0) {
                searchingStation = true;
            }
            if(i == 1) {
                searchingStation = false;
            }

            for(AreaBase structure : (searchingStation ? railwayData.stations : railwayData.depots)) {
                if(structure.inArea(pos.getX(), pos.getZ())) {
                    structureName = structure.name;
                    isAbs = true;
                    isStation = searchingStation;
                    break;
                }
                BlockPos centeredStructurePos = structure.getCenter();
                //Today I learned: The center pos is apparently nullable, which means there's no 2 corners in an area????
                //idk how is this possible, but it happens at least on Joban so
                if(centeredStructurePos == null) continue;
                double dist = getManhattenDistance(centeredStructurePos, pos);
                if(dist < closest) {
                    isAbs = false;
                    closest = dist;
                    structureName = structure.name;
                    isStation = searchingStation;
                }
            }
        }

        return structureName == null ? "" : (isAbs ? " (In " : " (Near ") + IGui.formatStationName(structureName) + (isStation ? " station)" : " depot)");
    }

    public static List<Integer> fromJsonArray(JsonArray arr) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++) {
            list.add(arr.get(i).getAsInt());
        }
        return list;
    }

    public static double getManhattenDistance(BlockPos pos1, BlockPos pos2) {
        return Math.abs(pos2.getX() - pos1.getX()) + Math.abs(pos2.getZ() - pos1.getZ());
    }
}
