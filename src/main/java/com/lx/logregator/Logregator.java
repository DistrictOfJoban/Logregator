package com.lx.logregator;

import com.lx.logregator.config.LogregatorConfig;
import com.lx.logregator.data.MTRLoggingManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logregator implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("Logregator");
    public static MTRLoggingManager mtrLoggingManager;
    public static Version versions = null;

    @Override
    public void onInitialize() {
        versions = FabricLoader.getInstance().getModContainer("logregator").get().getMetadata().getVersion();
        LOGGER.info("[Logregator] Version " + String.valueOf(versions));
        LogregatorConfig.load();
        mtrLoggingManager = new MTRLoggingManager();

        Mappings.registerCommand((dispatcher) -> {
            CommandHandler.registerCMDS(dispatcher);
        });
    }
}
