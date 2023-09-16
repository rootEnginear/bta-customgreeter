package rootenginear.customgreeter;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.net.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class CustomGreeter implements ModInitializer {
    public static final String MOD_ID = "customgreeter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static String WELCOME_STR;
    public static String FAREWELL_STR;
    public static String KICK_STR;
    public static int WELCOME_SOUND;
    public static int FAREWELL_SOUND;

    @Override
    public void onInitialize() {
        PropertyManager propertyManagerObj = new PropertyManager(new File("server.properties"));

        WELCOME_STR = propertyManagerObj.getStringProperty("welcome-text", "{PLAYER}§4 joined the game.");
        FAREWELL_STR = propertyManagerObj.getStringProperty("farewell-text", "{PLAYER}§4 left the game.");
        KICK_STR = propertyManagerObj.getStringProperty("kicked-text", "{PLAYER}§4 was kicked from the game.");
        WELCOME_SOUND = propertyManagerObj.getIntProperty("welcome-soundevent", 0);
        FAREWELL_SOUND = propertyManagerObj.getIntProperty("farewell-soundevent", 0);

        WELCOME_STR = WELCOME_STR.isEmpty() ? "{PLAYER}§4 joined the game." : WELCOME_STR;
        FAREWELL_STR = FAREWELL_STR.isEmpty() ? "{PLAYER}§4 left the game." : FAREWELL_STR;
        KICK_STR = KICK_STR.isEmpty() ? "{PLAYER}§4 was kicked from the game." : KICK_STR;

        LOGGER.info("CustomGreeter initialized.");
    }
}
