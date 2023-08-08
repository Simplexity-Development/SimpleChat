package simplexity.simplechat.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import simplexity.simplechat.SimpleChat;

import java.util.*;

public class FormatConfig {
    private static final HashMap<String, FormatConfig> chatFormatConfigMap = new HashMap<>();
    
    public final String formatID;
    public final String permissionString;
    public final String formatString;
    public final int weight;
    
    private FormatConfig(String formatID){
        ConfigurationSection configurationSection = SimpleChat.getInstance().getConfig().getConfigurationSection(formatID);
        assert configurationSection != null;
        this.formatID = formatID;
        this.permissionString = configurationSection.getString("permission", "none");
        this.formatString = configurationSection.getString("chat-format", "<player> : <message>");
        this.weight = configurationSection.getInt("weight", 0);
    }
    
    public static void populateFormats() {
        chatFormatConfigMap.clear();
        Set<String> formatIDs = SimpleChat.getInstance().getConfig().getKeys(false);
        for (String key : formatIDs) {
            chatFormatConfigMap.put(key, new FormatConfig(key));
        }
    }
    
    public static Map<String, FormatConfig> getFormats(){
        return Collections.unmodifiableMap(chatFormatConfigMap);
    }
    public static FormatConfig getFormatByID(String id){
        return chatFormatConfigMap.get(id);
    }

}
