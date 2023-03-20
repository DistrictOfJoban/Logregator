package com.lx.logregator;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.MTRLoggingManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Logregator implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Logregator");
    public static Optional<MTRLoggingManager> mtrLoggingManager;
    public static Version versions = null;

    @Override
    public void onInitialize() {
        versions = FabricLoader.getInstance().getModContainer("logregator").get().getMetadata().getVersion();
        LOGGER.info("[Logregator] Version " + String.valueOf(versions));
        LogregatorConfig.load();
        mtrLoggingManager = Optional.of(new MTRLoggingManager());

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            CommandHandler.registerCMDS(dispatcher);
        });
    }
}
