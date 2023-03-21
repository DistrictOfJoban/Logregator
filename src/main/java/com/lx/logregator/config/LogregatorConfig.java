package com.lx.logregator.config;

import com.google.gson.*;
import com.lx.logregator.Logregator;
import com.lx.logregator.Util;
import com.lx.logregator.data.EventType;
import com.lx.logregator.data.FilteredItem;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LogregatorConfig {
    private static final Path CONFIG_PATH = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "logregator", "config.json");
    public static final List<EventType> subscribedEvent = new ArrayList<>();
    public static final List<FilteredItem> filteredItems = new ArrayList<>();
    public static String webhookUrl;

    public static boolean load() {
        subscribedEvent.clear();
        filteredItems.clear();
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
                jsonConfig.getAsJsonArray("events").forEach(e -> {
                    String evType = e.getAsString();
                    try {
                        EventType type = EventType.valueOf(evType);
                        subscribedEvent.add(type);
                    } catch (IllegalArgumentException ignored) {
                        Logregator.LOGGER.warn(evType + "is not a valid EventType!");
                    }
                });
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
