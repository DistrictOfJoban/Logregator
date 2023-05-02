package com.lx.logregator.config;

import com.google.gson.*;
import com.lx.logregator.Logregator;
import com.lx.logregator.Util;
import com.lx.logregator.data.Area;
import com.lx.logregator.data.event.*;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LogregatorConfig {
    private static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "logregator", "config.json");
    public static final List<EventType> subscribedEvent = new ArrayList<>();
    public static final List<FilteredItemEvent> filteredItems = new ArrayList<>();
    public static final List<BlockDestroyEvent> blockBreak = new ArrayList<>();
    public static final List<BlockPlaceEvent> blockPlace = new ArrayList<>();
    public static final List<BlockOpenEvent> blockOpen = new ArrayList<>();
    public static String webhookUrl;

    public static boolean load() {
        subscribedEvent.clear();
        filteredItems.clear();
        blockBreak.clear();
        blockPlace.clear();
        blockOpen.clear();
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

            if(jsonConfig.has("events")) {
                JsonObject eventObject = jsonConfig.getAsJsonObject("events");

                if(eventObject.has("filteredItems")) {
                    eventObject.getAsJsonArray("filteredItems").forEach(e -> {
                        filteredItems.add(FilteredItemEvent.fromJson(e));
                    });
                    subscribedEvent.add(EventType.FILTERED_ITEM);
                }

                if(eventObject.has("blockBreak")) {
                    eventObject.getAsJsonArray("blockBreak").forEach(e -> {
                        blockBreak.add(BlockDestroyEvent.fromJson(e));
                    });
                    subscribedEvent.add(EventType.BLOCK_BREAK);
                }

                if(eventObject.has("blockOpen")) {
                    eventObject.getAsJsonArray("blockOpen").forEach(e -> {
                        blockOpen.add(BlockOpenEvent.fromJson(e));
                    });
                    subscribedEvent.add(EventType.BLOCK_OPEN);
                }

                if(eventObject.has("blockPlace")) {
                    eventObject.getAsJsonArray("blockPlace").forEach(e -> {
                        blockPlace.add(BlockPlaceEvent.fromJson(e));
                    });
                    subscribedEvent.add(EventType.BLOCK_PLACE);
                }

                if(eventObject.has("mtr")) {
                    boolean logBlock = eventObject.get("mtr").getAsJsonObject().get("logBlock").getAsBoolean();
                    boolean logRailwayData = eventObject.get("mtr").getAsJsonObject().get("logRailwayData").getAsBoolean();

                    if(logBlock) subscribedEvent.add(EventType.MTR_BLOCK);
                    if(logRailwayData) subscribedEvent.add(EventType.MTR_DATA);
                }
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
