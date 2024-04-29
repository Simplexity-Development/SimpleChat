package simplexity.simplechat;


import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import simplexity.simplechat.commands.SimpleChatReload;
import simplexity.simplechat.listeners.AsyncChatListener;
import simplexity.simplechat.listeners.AsyncChatListenerPAPI;
import simplexity.simplechat.utils.Message;

import java.util.Objects;

public final class SimpleChat extends JavaPlugin {
    
    private static SimpleChat instance;
    
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    
    @Override
    public void onEnable() {
        instance = this;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(new AsyncChatListenerPAPI(), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new AsyncChatListener(), this);
        }
        Objects.requireNonNull(this.getCommand("simplechatreload")).setExecutor(new SimpleChatReload());
        this.saveDefaultConfig();
        Message.reloadChatFormat();
    }
    
    
    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }
    
    public static SimpleChat getInstance() {
        return instance;
    }
    
}
