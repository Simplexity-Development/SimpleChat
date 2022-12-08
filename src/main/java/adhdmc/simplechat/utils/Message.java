package adhdmc.simplechat.utils;

public enum Message {
    CHAT_FORMAT("%luckperms_prefix%%player_displayname%%luckperms_suffix%<white> »</white><reset> <message>"),
    CONFIG_RELOAD("<prefix><gold> SimpleChat config has been reloaded"),
    PREFIX("<aqua>SimpleChat<white>»<reset>");
    String message;
    Message(String message) {
        this.message = message;
    }
    public String getMessage() { return this.message; }
    private void setMessage(String message) { this.message = message; }
    public static void reloadChatFormat(){
        /*YamlConfiguration config = SimpleChat.getChatConfig().getChatYML();
        CHAT_FORMAT.setMessage(config.getString("chat-format"));
        CONFIG_RELOAD.setMessage(config.getString("config-reload"));
        PREFIX.setMessage(config.getString("prefix"));*/
    }
}
