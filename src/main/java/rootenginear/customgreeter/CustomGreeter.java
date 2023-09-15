package rootenginear.customgreeter;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomGreeter implements ModInitializer {
    public static final String MOD_ID = "customgreeter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("CustomGreeter initialized.");
    }
}
