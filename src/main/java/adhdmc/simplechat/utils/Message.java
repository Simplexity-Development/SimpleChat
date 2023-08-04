package adhdmc.simplechat.utils;

import adhdmc.simplechat.SimpleChat;
import org.bukkit.configuration.file.FileConfiguration;

public enum Message {
    CHAT_FORMAT("<papi:player_displayname><white> »</white><reset><message>"),
    ERROR_PAPI_NEEDS_ARGUMENT("papi tag requires an argument"),
    CONFIG_RELOAD("<prefix><gold> SimpleChat config has been reloaded"),
    PREFIX("<aqua>SimpleChat<white>»<reset>");
    String message;
    Message(String message) {
        this.message = message;
    }
    public String getMessage() { return this.message; }
    private void setMessage(String message) { this.message = message; }
    public static void reloadChatFormat(){
        FileConfiguration config = SimpleChat.getInstance().getConfig();
        CHAT_FORMAT.setMessage(config.getString("chat-format"));
        CONFIG_RELOAD.setMessage(config.getString("config-reload"));
        PREFIX.setMessage(config.getString("prefix"));
    }
}
