package adhdmc.simplechat.listeners;

import adhdmc.simplechat.SimpleChat;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {
    MiniMessage miniMessage = SimpleChat.getMiniMessage();
    @EventHandler
    public void onPlayerChat(AsyncChatEvent chatEvent){
        Component originalMessage = chatEvent.originalMessage();
        Player player = chatEvent.getPlayer();
        String blankMessage = miniMessage.serialize(originalMessage);

    }


}
