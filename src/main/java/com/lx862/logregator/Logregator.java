package com.lx862.logregator;

import com.lx862.logregator.config.LogregatorConfig;
import com.lx862.logregator.data.MTRLoggingManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logregator implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Logregator");
    public static MTRLoggingManager mtrLoggingManager;
    public static Version modVersion = null;

    @Override
    public void onInitialize() {
        modVersion = FabricLoader.getInstance().getModContainer("logregator").get().getMetadata().getVersion();
        LOGGER.info("[Logregator] Version {}", modVersion);
        LogregatorConfig.load();
        mtrLoggingManager = new MTRLoggingManager();

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, third) -> {
            CommandHandler.registerCommands(dispatcher);
        });
    }
}
