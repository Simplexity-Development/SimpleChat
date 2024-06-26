package simplexity.simplechat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.utils.ChatPermission;
import simplexity.simplechat.utils.Message;

public class AsyncChatListener implements Listener {
    
    MiniMessage miniMessage = SimpleChat.getMiniMessage();
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent chatEvent) {
        String message = PlainTextComponentSerializer.plainText().serialize(chatEvent.message());
        Player player = chatEvent.getPlayer();
        Component messageParsed = permissionParsedMessage(player, message);
        chatEvent.message(messageParsed);
        chatEvent.renderer((source, sourceDisplayName, chatMessage, viewer) ->
                miniMessage.deserialize(Message.CHAT_FORMAT.getMessage(),
                        Placeholder.component("player", sourceDisplayName),
                        Placeholder.component("message", messageParsed)));
    }
    
    
    //Stolen from https://github.com/YouHaveTrouble/JustChat @YouHaveTrouble
    private Component permissionParsedMessage(Player player, String message) {
        TagResolver.Builder tagResolver = TagResolver.builder();
        for (ChatPermission perm : ChatPermission.values()) {
            if (player.hasPermission(perm.getPermission()) && perm.getTagResolver() != null) {
                tagResolver.resolver(perm.getTagResolver());
            }
        }
        MiniMessage msgParser = MiniMessage.builder().tags(tagResolver.build()).build();
        return msgParser.deserialize(message);
    }

    
}
