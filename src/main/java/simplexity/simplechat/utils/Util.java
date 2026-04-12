package simplexity.simplechat.utils;

import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.config.ConfigHandler;

public class Util {
    public static void debug(String message){
        if (ConfigHandler.getInstance().isDebug()) {
            SimpleChat.getInstance().getSLF4JLogger().info(message);
        }
    }
}
