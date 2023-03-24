package com.lx.logregator.config;

import com.google.gson.*;
import com.lx.logregator.Logregator;
import com.lx.logregator.Util;
import com.lx.logregator.data.Area;
import com.lx.logregator.data.event.BlockDestroy;
import com.lx.logregator.data.event.EventType;
import com.lx.logregator.data.event.FilteredItem;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LogregatorConfig {
    private static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "logregator", "config.json");
    public static final List<EventType> subscribedEvent = new ArrayList<>();
    public static final List<FilteredItem> filteredItems = new ArrayList<>();
    public static final List<BlockDestroy> blockBreak = new ArrayList<>();
    public static String webhookUrl;

    public static boolean load() {
        subscribedEvent.clear();
        filteredItems.clear();
        blockBreak.clear();
        if(!Files.exists(CONFIG_PATH)) {
            Logregator.LOGGER.warn("[Logregator] Config file not found, generating one...");
            write();
            return false;
        }

        Logregator.LOGGER.info("[Logregator] Reading Config...");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();

            if(jsonConfig.has("webhook")) {
                webhookUrl = jsonConfig.get("webhook").getAsString();
            }

            if(jsonConfig.has("filteredItems")) {
                jsonConfig.getAsJsonArray("filteredItems").forEach(e -> {
                    JsonObject object = e.getAsJsonObject();
                    if(!object.has("itemId")) return;
                    String id = object.get("itemId").getAsString();
                    List<Integer> permLevel = new ArrayList<>();
                    if(object.has("permLevel")) {
                        permLevel.addAll(Util.fromJsonArray(object.get("permLevel").getAsJsonArray()));
                    }

                    filteredItems.add(new FilteredItem(id, permLevel));
                });
                subscribedEvent.add(EventType.FILTERED_ITEM);
            }

            if(jsonConfig.has("blockBreak")) {
                jsonConfig.getAsJsonArray("blockBreak").forEach(e -> {
                    JsonObject object = e.getAsJsonObject();
                    String blockId = object.get("blockId").getAsString();
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

                    blockBreak.add(new BlockDestroy(blockId, area, permLevel));
                });
                subscribedEvent.add(EventType.BLOCK_BREAK);
            }

            if(jsonConfig.has("mtr")) {
                boolean logBlock = jsonConfig.get("mtr").getAsJsonObject().get("logBlock").getAsBoolean();
                boolean logRailwayData = jsonConfig.get("mtr").getAsJsonObject().get("logRailwayData").getAsBoolean();

                if(logBlock) subscribedEvent.add(EventType.MTR_BLOCK);
                if(logRailwayData) subscribedEvent.add(EventType.MTR_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void write() {
        Logregator.LOGGER.info("[Logregator] Writing Config...");
        final JsonObject jsonConfig = new JsonObject();
        try {
            Files.write(CONFIG_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
