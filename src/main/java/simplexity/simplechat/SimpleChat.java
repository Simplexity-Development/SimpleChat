package simplexity.simplechat;


import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplechat.commands.SimpleChatReload;
import simplexity.simplechat.config.ConfigHandler;
import simplexity.simplechat.listeners.AsyncChatListener;
import simplexity.simplechat.utils.ChatPermission;

@SuppressWarnings("UnstableApiUsage")
public final class SimpleChat extends JavaPlugin {

    private static SimpleChat instance;
    private static boolean papiEnabled = false;
    private static boolean miniPlaceholdersEnabled = false;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();


    @Override
    public void onEnable() {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papiEnabled = true;
        }
        if (Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null) {
            miniPlaceholdersEnabled = true;
        }
        registerPermissions();
        this.getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(SimpleChatReload.createCommand());
        });
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConfigHandler.getInstance().reloadConfigValues();
    }

    private void registerPermissions() {
        for (ChatPermission chatPermission : ChatPermission.values()) {
            Permission permission = new Permission(chatPermission.getPermission(), chatPermission.getDescription(), chatPermission.getDefault());
            Bukkit.getPluginManager().addPermission(permission);
        }
    }


    public static boolean isMiniPlaceholdersEnabled() {
        return miniPlaceholdersEnabled;
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static SimpleChat getInstance() {
        return instance;
    }

    public static boolean isPapiEnabled() {
        return papiEnabled;
    }


}
