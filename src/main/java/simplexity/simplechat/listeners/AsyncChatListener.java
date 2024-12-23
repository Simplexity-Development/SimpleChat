package simplexity.simplechat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.utils.ChatPermission;
import simplexity.simplechat.utils.Message;

public class AsyncChatListener implements Listener {
    
    static MiniMessage miniMessage = SimpleChat.getMiniMessage();



    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent chatEvent) {
        String message = PlainTextComponentSerializer.plainText().serialize(chatEvent.message());
        Player player = chatEvent.getPlayer();
        Component messageParsed = permissionParsedMessage(player, message);
        chatEvent.message(messageParsed);
        if (SimpleChat.isPapiEnabled()) {
            chatEvent.renderer((source, sourceDisplayName, chatMessage, viewer) ->
                    miniMessage.deserialize(Message.CHAT_FORMAT.getMessage(),
                            papiTag(player),
                            Placeholder.component("player", sourceDisplayName),
                            Placeholder.component("message", messageParsed)));
        } else {
            chatEvent.renderer((source, sourceDisplayName, chatMessage, viewer) ->
                    miniMessage.deserialize(Message.CHAT_FORMAT.getMessage(),
                            Placeholder.component("player", sourceDisplayName),
                            Placeholder.component("message", messageParsed)));
        }
    }
    
    
    //Stolen from https://github.com/YouHaveTrouble/JustChat @YouHaveTrouble
    private Component permissionParsedMessage(Player player, String message) {
        TagResolver.Builder tagResolver = TagResolver.builder();
        for (ChatPermission perm : ChatPermission.values()) {
            if (player.hasPermission(perm.getPermission()) && perm.getTagResolver() != null) {
                tagResolver.resolver(perm.getTagResolver());
            }
        }
        if (player.hasPermission(ChatPermission.SHOW_ITEM.getPermission())) {
            tagResolver.resolver(ChatPermission.createItemTag(player));
        }
        MiniMessage msgParser = MiniMessage.builder().tags(tagResolver.build()).build();
        return msgParser.deserialize(message);
    }

    /**
     * Creates a tag resolver capable of resolving PlaceholderAPI tags for a given player.
     *
     * @param player the player
     *
     * @return the tag resolver
     */
    public static @NotNull TagResolver papiTag(final @NotNull Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr(Message.ERROR_PAPI_NEEDS_ARGUMENT.getMessage()).value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            Component componentPlaceholder;
            if (parsedPlaceholder.contains("§")) {
                componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            } else {
                componentPlaceholder = miniMessage.deserialize(parsedPlaceholder);
            }
            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }
    
}
