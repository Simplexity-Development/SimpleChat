package simplexity.simplechat.utils;

import simplexity.simplechat.SimpleChat;
import org.bukkit.configuration.file.FileConfiguration;

public enum Message {
    CHAT_FORMAT("<player><white> » </white><reset><message>"),
    EMPTY_FORMAT("<white>[nothing]</white>"),
    ERROR_PAPI_NEEDS_ARGUMENT("papi tag requires an argument"),
    CONFIG_RELOAD("<aqua>SimpleChat<white> »<reset><gold> SimpleChat config has been reloaded");
    String message;
    
    Message(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    private void setMessage(String message) {
        this.message = message;
    }
    
    public static void reloadChatFormat() {
        FileConfiguration config = SimpleChat.getInstance().getConfig();
        CHAT_FORMAT.setMessage(config.getString("chat-format"));
        EMPTY_FORMAT.setMessage(config.getString("empty-format"));
        CONFIG_RELOAD.setMessage(config.getString("config-reload"));
    }
}
