package com.lx862.logregator.config;

import com.google.gson.*;
import com.lx862.logregator.Logregator;
import com.lx862.logregator.data.event.BlockDestroyEvent;
import com.lx862.logregator.data.event.BlockOpenEvent;
import com.lx862.logregator.data.event.BlockPlaceEvent;
import com.lx862.logregator.data.event.FilteredItemEvent;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LogregatorConfig {
    private static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "logregator", "config.json");
    public static final List<FilteredItemEvent> filteredItems = new ArrayList<>();
    public static final List<BlockDestroyEvent> blockBreak = new ArrayList<>();
    public static final List<BlockPlaceEvent> blockPlace = new ArrayList<>();
    public static final List<BlockOpenEvent> blockOpen = new ArrayList<>();
    public static boolean logMTRBlocks = false;
    public static boolean logMTRData = false;
    public static String webhookUrl;

    public static boolean load() {
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
            final JsonObject jsonConfig = JsonParser.parseString(String.join("", Files.readAllLines(CONFIG_PATH))).getAsJsonObject();

            if(jsonConfig.has("webhook")) {
                webhookUrl = jsonConfig.get("webhook").getAsString();
            }

            if(jsonConfig.has("events")) {
                JsonObject eventObject = jsonConfig.getAsJsonObject("events");

                if(eventObject.has("filteredItems")) {
                    eventObject.getAsJsonArray("filteredItems").forEach(e -> {
                        filteredItems.add(FilteredItemEvent.fromJson(e));
                    });
                }

                if(eventObject.has("blockBreak")) {
                    eventObject.getAsJsonArray("blockBreak").forEach(e -> {
                        blockBreak.add(BlockDestroyEvent.fromJson(e));
                    });
                }

                if(eventObject.has("blockOpen")) {
                    eventObject.getAsJsonArray("blockOpen").forEach(e -> {
                        blockOpen.add(BlockOpenEvent.fromJson(e));
                    });
                }

                if(eventObject.has("blockPlace")) {
                    eventObject.getAsJsonArray("blockPlace").forEach(e -> {
                        blockPlace.add(BlockPlaceEvent.fromJson(e));
                    });
                }

                if(eventObject.has("mtr")) {
                    boolean logBlock = eventObject.get("mtr").getAsJsonObject().get("logBlock").getAsBoolean();
                    boolean logRailwayData = eventObject.get("mtr").getAsJsonObject().get("logRailwayData").getAsBoolean();

                    LogregatorConfig.logMTRBlocks = logBlock;
                    LogregatorConfig.logMTRData = logRailwayData;
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
