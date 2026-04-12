package simplexity.simplechat.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.utils.ChatPermission;

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
    private final HashMap<ChatPermission, Boolean> chatParsingSettings = new HashMap<>();
    private String linkFormat;
    private final Logger logger = SimpleChat.getInstance().getLogger();
    private boolean debug = false;

    private String defaultFormat, emptyItemFormat, configReloaded;
    private boolean permissionFormatting;
    private boolean chatParsingUsePermissions;

    public void reloadConfigValues() {
        SimpleChat.getInstance().reloadConfig();
        FileConfiguration config = SimpleChat.getInstance().getConfig();
        defaultFormat = config.getString("chat-format", "<player><white> » </white><reset><message>");
        emptyItemFormat = config.getString("show-item.empty-format", "<white>[nothing]</white>");
        configReloaded = config.getString("config-reload", "<aqua>SimpleChat<white> »<reset><gold> SimpleChat config has been reloaded");
        linkFormat = config.getString("link-format", "<link>");
        permissionFormatting = config.getBoolean("permission-formatting.enabled", false);
        debug = config.getBoolean("debug.enabled", false);
        chatParsingUsePermissions = config.getBoolean("chat-parsing.use-permissions", true);
        populateChatParsingSettings(config);
        if (permissionFormatting) populateFormats(config);
    }

    private void populateChatParsingSettings(FileConfiguration config) {
        chatParsingSettings.clear();
        ConfigurationSection section = config.getConfigurationSection("chat-parsing.settings");
        for (ChatPermission perm : ChatPermission.values()) {
            if (perm.getConfigKey() == null) continue;
            boolean enabled = section != null && section.getBoolean(perm.getConfigKey(), false);
            debug = section.getBoolean(perm.getConfigKey() + ".debug", false);
            chatParsingSettings.put(perm, enabled);
        }
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
            debug("key: " + key);
            String permissionName = section.getString(key + ".permission");
            if (permissionName == null || permissionName.isEmpty()) {
                logger.warning("No valid permission name provided for " + key + ", unable to continue registering this format");
                continue;
            }
            Permission permission = new Permission(permissionName, PermissionDefault.OP);
            debug("Permission: " + permission);
            String format = section.getString(key + ".format");
            debug("format: " + format);
            int priority = section.getInt(key + ".priority");
            debug("Priority: " + priority);
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

    public String getLinkFormat() {
        return linkFormat;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isChatParsingUsePermissions() {
        return chatParsingUsePermissions;
    }

    public boolean isChatParsingEnabled(ChatPermission permission) {
        return chatParsingSettings.getOrDefault(permission, false);
    }

    private void debug(String message) {
        if (debug) {
            logger.info(message);
        }
    }
}
