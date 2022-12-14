package adhdmc.simplechat.config;

import adhdmc.simplechat.SimpleChat;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigStuff {

    public static void setConfigDefaults(){
        FileConfiguration config = SimpleChat.getInstance().getConfig();
        config.addDefault("chat-format", "%player_displayname%<white> »</white><reset>%player_message%");
        config.addDefault("config-reload", "<prefix><gold> SimpleChat config has been reloaded");
        config.addDefault("prefix", "<aqua>SimpleChat<white>»<reset>");
    }
}
