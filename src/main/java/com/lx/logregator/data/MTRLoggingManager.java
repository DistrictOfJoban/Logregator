package com.lx.logregator.data;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.webhook.DiscordEmbed;
import com.lx.logregator.data.webhook.DiscordWebhook;
import com.lx.logregator.Util;
import mtr.data.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MTRLoggingManager {
    private static final int TICK_PER_SECOND = 20;

    private final HashMap<String, String> nameMapping = new HashMap<>();

    private final HashMap<String, String> keyMapping = new HashMap<>();

    public MTRLoggingManager() {
        nameMapping.put("mtr.block.BlockLiftTrackFloor$TileEntityLiftTrackFloor", "MTR Lift Track (Floor)");
        nameMapping.put("mtr.block.BlockTrainScheduleSensor$TileEntityTrainScheduleSensor", "MTR Train Schedule Sensor");
        nameMapping.put("mtr.block.BlockTrainAnnouncer$TileEntityTrainAnnouncer", "MTR Train Announcer");
        nameMapping.put("mtr.block.BlockArrivalProjector1Large$TileEntityArrivalProjector1Large", "MTR Arrival Projector (Large)");
        nameMapping.put("mtr.block.BlockArrivalProjector1Medium$TileEntityArrivalProjector1Medium", "MTR Arrival Projector (Medium)");
        nameMapping.put("mtr.block.BlockArrivalProjector1Small$TileEntityArrivalProjector1Small", "MTR Arrival Projector (Small)");
        nameMapping.put("mtr.block.BlockPIDS1$TileEntityBlockPIDS1", "MTR PIDS 1 (Old MTR PIDS)");
        nameMapping.put("mtr.block.BlockPIDS2$TileEntityBlockPIDS2", "MTR PIDS 2 (3 Row PIDS)");
        nameMapping.put("mtr.block.BlockPIDS3$TileEntityBlockPIDS3", "MTR PIDS 3 (WRL PIDS)");
        nameMapping.put("mtr.block.BlockRailwaySign$TileEntityRailwaySign", "MTR Railway Sign");
        nameMapping.put("mtr.block.BlockRouteSignStandingLight$TileEntityRouteSignStandingLight", "MTR Route Sign (Standing Light)");
        nameMapping.put("mtr.block.BlockRouteSignStandingMetal$TileEntityRouteSignStandingMetal", "MTR Route Sign (Standing Metal)");
        nameMapping.put("mtr.block.BlockRouteSignStandingWallLight$TileEntityRouteSignStandingWallLight", "MTR Route Sign (Wall Light)");
        nameMapping.put("mtr.block.BlockRouteSignStandingWallMetal$TileEntityRouteSignStandingWallMetal", "MTR Route Sign (Wall Metal)");
        nameMapping.put("com.jsblock.block.PIDS1A$TileEntityBlockPIDS1A", "JCM PIDS 1A");
        nameMapping.put("mtr.data.LiftServer", "MTR Lift");
        nameMapping.put("mtr.data.Siding", "MTR Siding");
        nameMapping.put("mtr.data.Depot", "MTR Depot");
        nameMapping.put("mtr.data.Route", "MTR Route");
        nameMapping.put("mtr.data.Platform", "MTR Platform");
        nameMapping.put("mtr.data.Rail", "MTR Rail");
        nameMapping.put("mtr.data.Station", "MTR Station");
        nameMapping.put("mtr.data.SignalBlocks$SignalBlock", "MTR Signal Section");
        nameMapping.put("mtr.block.BlockNode", "MTR Node");

        keyMapping.put("id", "Internal ID");
        keyMapping.put("transport_mode", "Transport Type");
        keyMapping.put("color", "Color");
        keyMapping.put("is_light_rail_route", "Light Rail Route?");
        keyMapping.put("train_custom_id", "Train Type");
        keyMapping.put("train_type", "Train Base Type");
        keyMapping.put("dwell_time", "Dwell time");
        keyMapping.put("platform_id", "Platform IDs");
        keyMapping.put("custom_destinations", "Custom Destinations");
        keyMapping.put("route_type", "Route Type");
        keyMapping.put("light_rail_route_number", "LRT No.");
        keyMapping.put("use_real_time", "Real time Scheduling?");
        keyMapping.put("repeat_infinitely", "Repeat Route Instructions?");
        keyMapping.put("frequencies", "Schedule frequencies");
        keyMapping.put("departures", "Scheduled timetable");
        keyMapping.put("one_way", "One way");
        keyMapping.put("name", "Name");
        keyMapping.put("route_ids", "Route(s)");
        keyMapping.put("zone", "Fare Zone");
        keyMapping.put("exits", "Exits");
        keyMapping.put("seconds", "Seconds");
        keyMapping.put("x_min", "Corner 1 (X)");
        keyMapping.put("z_min", "Corner 1 (Z)");
        keyMapping.put("x_max", "Corner 2 (X)");
        keyMapping.put("z_max", "Corner 2 (Z)");
        keyMapping.put("floor_number", "Floor Number");
        keyMapping.put("floor_description", "Floor Description");
        keyMapping.put("should_ding", "Play Ding Sound?");
        keyMapping.put("display_page", "Page Shown");
        keyMapping.put("message0", "Row 1 message");
        keyMapping.put("message1", "Row 2 message");
        keyMapping.put("message2", "Row 3 message");
        keyMapping.put("message3", "Row 4 message");
        keyMapping.put("hide_arrival0", "Row 1 hidden");
        keyMapping.put("hide_arrival1", "Row 2 hidden");
        keyMapping.put("hide_arrival2", "Row 3 hidden");
        keyMapping.put("hide_arrival3", "Row 4 hidden");
        keyMapping.put("sign_length0", "Slot 1");
        keyMapping.put("sign_length1", "Slot 2");
        keyMapping.put("sign_length2", "Slot 3");
        keyMapping.put("sign_length3", "Slot 4");
        keyMapping.put("sign_length4", "Slot 5");
        keyMapping.put("sign_length5", "Slot 6");
        keyMapping.put("sign_length6", "Slot 7");
        keyMapping.put("sound_id", "Sound ID");
        keyMapping.put("message", "Message");
        keyMapping.put("stopped_only", "Stopped Only");
        keyMapping.put("moving_only", "Moving Only");
    }

    public String getFriendlyClassName(String str) {
        String filteredStr = str.replace("fabric.", "");
        return nameMapping.getOrDefault(filteredStr, filteredStr);
    }

    public String getFriendlyKeyName(String fieldName) {
        return keyMapping.getOrDefault(fieldName, fieldName);
    }

    public String getPossibleFriendlyValue(String className, String fieldName, String value, World world) {
        if(value.isEmpty()) {
            return "(None)";
        }
        if (Objects.equals(value, "true") || Objects.equals(value, "false")) {
            if (value.equals("true")) return "Yes";
            if (value.equals("false")) return "No";
        }
        if (Objects.equals(fieldName, "acceleration_constant")) {
            return String.valueOf(Double.parseDouble(value) * 10 * TICK_PER_SECOND * 3.6);
        }
        if(fieldName.equals("name")) {
            return IGui.formatStationName(value);
        }
        if(fieldName.equals("frequencies")) {
            if(value.equals("[]")) return "N/A";
            StringBuilder newString = new StringBuilder();
            String[] frequencies = value.substring(1, value.length() - 1).split(",");
            int i = 0;
            for(String frequency : frequencies) {
                if(frequency.isEmpty()) continue;

                int freq = Integer.parseInt(frequency);
                newString.append("\\n").append("[" + Util.padZero(i) + ":00-" + Util.padZero(i) + ":59] ").append(freq * 0.25).append("/hr");
                i++;
            }
            return newString.toString();
        }

        // Real time scheduled departure
        if(fieldName.equals("departures")) {
            if(value.equals("[]")) return "N/A";
            final long offset = System.currentTimeMillis() / Depot.MILLISECONDS_PER_DAY * Depot.MILLISECONDS_PER_DAY;
            final StringBuilder resultStr = new StringBuilder();
            final String[] departures = value.substring(1, value.length() - 1).split(",");
            final int entryLimit = 35;

            Long lastDeparture = null;
            int i = 0;
            for(String departure : departures) {
                if(i >= entryLimit) {
                    resultStr.append("\\n").append("...");
                    break;
                }
                long depTime = Long.parseLong(departure);
                final Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(depTime + offset);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);
                final int second = calendar.get(Calendar.SECOND);
                String hms = String.format("%02d:%02d:%02d", hour, minute, second);
                resultStr.append("\\n").append(hms);

                if(lastDeparture != null) {
                    resultStr.append(" (").append(Util.getTimeDuration(lastDeparture, depTime)).append(" gap)");
                }

                lastDeparture = depTime;
                i++;
            }
            return resultStr.toString();
        }
        if(fieldName.equals("dwell_time")) {
            int dwell = (int)(Integer.parseInt(value) / 2.0) * 1000;
            return Util.formatTime(dwell, true);
        }

        if(fieldName.equals("route_ids")) {
            if(value.equals("[]")) return "N/A";
            RailwayData data = RailwayData.getInstance(world);
            final StringBuilder resultStr = new StringBuilder();
            final String[] routeIds = value.substring(1, value.length() - 1).split(",");
            for(String routeIdStr : routeIds) {
                long routeId = Long.parseLong(routeIdStr);
                Route route = data.dataCache.routeIdMap.get(routeId);
                if(route != null) {
                    resultStr.append("\\n").append(IGui.formatStationName(route.name));
                }
            }
            return resultStr.toString();
        }


        return value;
    }

    public void send(ServerPlayerEntity player, Class<?> dataClass, long id, String name, List<String> oldData, List<String> newData, BlockPos[] positions) {
        final List<String> oldDataDiff;
        final List<String> newDataDiff;

        if (oldData.size() == newData.size()) {
            oldDataDiff = new ArrayList<>();
            newDataDiff = new ArrayList<>();

            for (int i = 0; i < oldData.size(); i++) {
                final String oldDataString = oldData.get(i);
                final String newDataString = newData.get(i);
                if (!oldDataString.equals(newDataString)) {
                    oldDataDiff.add(oldDataString);
                    newDataDiff.add(newDataString);
                }
            }
        } else {
            oldDataDiff = oldData;
            newDataDiff = newData;
        }

        if(oldDataDiff.equals(newDataDiff)) return;

        MTRActionType actionType;
        if(!newDataDiff.isEmpty() && oldData.isEmpty()) {
            actionType = MTRActionType.CREATE;
        } else if(newDataDiff.isEmpty() && !oldData.isEmpty()) {
            actionType = MTRActionType.REMOVE;
        } else {
            actionType = MTRActionType.EDIT;
        }

        try {
            processMTREvent(player, dataClass, id, name, oldData, newData, oldDataDiff, newDataDiff, positions, actionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMTREvent(ServerPlayerEntity player, Class<?> dataClass, long id, String name, List<String> oldData, List<String> newData, List<String> oldDataDiff, List<String> newDataDiff, BlockPos[] positions, MTRActionType actionType) {
        String className = dataClass.getName();
        EventType eventType = className.contains(".block.") ? EventType.MTR_BLOCK : EventType.MTR_DATA;
        DiscordWebhook webhook = new DiscordWebhook(LogregatorConfig.webhookUrl);
        DiscordEmbed embed = new DiscordEmbed();

        StringBuilder oldDatas = new StringBuilder();
        if(oldDataDiff.isEmpty()) {
            oldDatas.append("N/A");
        }
        for(String str : oldDataDiff) {
            String key = str.split(":")[0].replace("\"", "");
            String value = str.split(":")[1].replace("\"", "");
            String friendlyKey = getFriendlyKeyName(key);
            String friendlyValue = getPossibleFriendlyValue(className, key, value, player.world);
            oldDatas.append(friendlyKey).append(": ").append("**").append(friendlyValue).append("**").append("\\n");
        }
        embed.addField("Before", oldDatas.toString(), false);


        StringBuilder newDatas = new StringBuilder();
        if(newDataDiff.isEmpty()) {
            newDatas.append("N/A");
        }
        for(String str : newDataDiff) {
            String key = str.split(":")[0].replace("\"", "");
            String value = str.split(":")[1].replace("\"", "");
            String friendlyKey = getFriendlyKeyName(key);
            String friendlyValue = getPossibleFriendlyValue(className, key, value, player.world);
            newDatas.append(friendlyKey).append(": ").append("**").append(friendlyValue).append("**").append("\\n");
        }
        embed.addField("After", newDatas.toString(), false);

        /* Get Context */
        Integer color = getColor(oldData, newData);
        String displayedName = !name.isEmpty() ? " \\\" " + IGui.formatStationName(name) + " \\\" " : " ";

        if(positions.length > 0) {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for(BlockPos pos : positions) {
                sb.append(String.format("%d. **[%d, %d, %d]**", i+1, pos.getX(), pos.getY(), pos.getZ())).append("\\n");
                i++;
            }
            embed.addField("Block Positions", sb.toString(), false);
        }

        embed.setTitle(actionType.getEmoji() + " " + getFriendlyClassName(className) + displayedName + actionType.getAction());
        embed.setAuthor(player.getGameProfile().getName(), null, "https://minotar.net/avatar/" + player.getGameProfile().getName() + "/16");
        embed.setTimestamp();
        if(color != null) embed.setColor(new Color(color));
        webhook.addEmbed(embed);
        try {
            webhook.send(eventType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getColor(List<String> oldData, List<String> newData) {
        List<String> chosenDataset = newData.isEmpty() ? oldData : newData;

        for(String str : chosenDataset) {
            String key = str.split(":")[0].replace("\"", "");
            String value = str.split(":")[1].replace("\"", "");
            if(key.equals("color")) {
                try {
                    return Integer.parseInt(value);
                } catch (Exception ignored) {

                }
            }
        }
        return null;
    }
}
