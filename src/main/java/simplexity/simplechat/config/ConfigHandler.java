package simplexity.simplechat.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import simplexity.simplechat.SimpleChat;

import java.util.HashMap;
import java.util.logging.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;

    public ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    private final HashMap<Permission, String> chatFormats = new HashMap<>();
    private final HashMap<Permission, Integer> formatPriority = new HashMap<>();
    private final Logger logger = SimpleChat.getInstance().getLogger();

    private String defaultFormat, emptyItemFormat, configReloaded;
    private boolean permissionFormatting;

    public void reloadConfigValues() {
        SimpleChat.getInstance().reloadConfig();
        FileConfiguration config = SimpleChat.getInstance().getConfig();
        defaultFormat = config.getString("chat-format", "<player><white> » </white><reset><message>");
        emptyItemFormat = config.getString("show-item.empty-format", "<white>[nothing]</white>");
        configReloaded = config.getString("config-reloaded", "<aqua>SimpleChat<white> »<reset><gold> SimpleChat config has been reloaded");
        permissionFormatting = config.getBoolean("permission-formatting.enabled", false);
        if (permissionFormatting) populateFormats(config);
    }

    private void populateFormats(FileConfiguration config) {
        chatFormats.clear();
        formatPriority.clear();
        ConfigurationSection section = config.getConfigurationSection("permission-formatting.formats");
        if (section == null) {
            logger.warning("No settings found for permission-based chat formatting. All chat will use the default format until this is resolved. Please make sure you did not use TAB instead of SPACE");
            return;
        }
        for (String key : section.getKeys(false)) {
            logger.info("key: " + key);
            Permission permission = new Permission(key + ".permission");
            logger.info("Permission: " + permission);
            String format = section.getString(key + ".format");
            logger.info("format: " + format);
            int priority = section.getInt(key + ".priority");
            logger.info("Priority: " + priority);
            if (format == null || format.isEmpty()) {
                logger.warning("The format for '" + key + "' is null, please provide a valid format string");
                continue;
            }
            chatFormats.put(permission, format);
            formatPriority.put(permission, priority);
        }
    }


    public HashMap<Permission, String> getFormats() {
        return chatFormats;
    }

    public HashMap<Permission, Integer> getFormatPriority() {
        return formatPriority;
    }

    public String getDefaultFormat() {
        return defaultFormat;
    }

    public String getEmptyItemFormat() {
        return emptyItemFormat;
    }

    public String getConfigReloaded() {
        return configReloaded;
    }

    public boolean shouldDoPermissionFormatting() {
        return permissionFormatting;
    }


}
