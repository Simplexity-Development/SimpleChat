package simplexity.simplechat;


import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import simplexity.simplechat.commands.SimpleChatReload;
import simplexity.simplechat.config.FormatConfig;
import simplexity.simplechat.listeners.AsyncChatListener;
import simplexity.simplechat.utils.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class SimpleChat extends JavaPlugin {
    
    private static SimpleChat instance;
    
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private static final HashMap<UUID, TagResolver> playerTagResolverCache = new HashMap<>();
    
    
    @Override
    public void onEnable() {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(new AsyncChatListener(), this);
        } else {
            getLogger().warning("SimpleChat requires PlaceholderAPI to be installed. Disabling SimpleChat.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Objects.requireNonNull(this.getCommand("simplechatreload")).setExecutor(new SimpleChatReload());
        this.saveDefaultConfig();
        FormatConfig.populateFormats();
        Message.reloadChatFormat();
    }
    
    
    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }
    
    public static HashMap<UUID, TagResolver> getPlayerTagResolverCache() {
        return playerTagResolverCache;
    }
    
    public static SimpleChat getInstance() {
        return instance;
    }
    
}
