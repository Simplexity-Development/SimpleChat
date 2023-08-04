package adhdmc.simplechat;

// import adhdmc.simplechat.config.ConfigStuff;
import adhdmc.simplechat.listeners.AsyncChatListener;
import adhdmc.simplechat.utils.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleChat extends JavaPlugin {
    private static SimpleChat instance;

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(new AsyncChatListener(), this);
        } else {
            getLogger().warning("SimpleChat requires PlaceholderAPI to be installed. Disabling SimpleChat.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.saveDefaultConfig();
        // ConfigStuff.setConfigDefaults();
        Message.reloadChatFormat();

    }


    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static SimpleChat getInstance() {
        return instance;
    }

}
