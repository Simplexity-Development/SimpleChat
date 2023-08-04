package adhdmc.simplechat.listeners;

import adhdmc.simplechat.SimpleChat;
import adhdmc.simplechat.utils.ChatPermission;
import adhdmc.simplechat.utils.Message;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.regex.Matcher;

public class AsyncChatListener implements Listener {
    Server server = SimpleChat.getInstance().getServer();
    MiniMessage miniMessage = SimpleChat.getMiniMessage();
    @EventHandler
    public void onPlayerChat(AsyncChatEvent chatEvent){
        String message = miniMessage.serialize(chatEvent.message());
        Player player = chatEvent.getPlayer();
        Component chatStyle = miniMessage.deserialize(Message.CHAT_FORMAT.getMessage(), papiTag(player));
        Component messageParsed = permissionParsedMessage(player, message);
        chatEvent.message(messageParsed);
        //TODO make the chat style work
        

    }
    //Stolen from https://github.com/YouHaveTrouble/JustChat @YouHaveTrouble
    private Component permissionParsedMessage(Player player, String message) {
        TagResolver.Builder tagResolver = TagResolver.builder();
        for(ChatPermission perm : ChatPermission.values()) {
            if (player.hasPermission(perm.getPermission()) && perm.getTagResolver() != null) {
                tagResolver.resolver(perm.getTagResolver());
            }
        }
        MiniMessage msgParser = MiniMessage.builder().tags(tagResolver.build()).build();
        return msgParser.deserialize(message);
    }
    
    //Credit: https://docs.advntr.dev/faq.html#how-can-i-use-bukkits-placeholderapi-in-minimessage-messages
    /**
     * Creates a tag resolver capable of resolving PlaceholderAPI tags for a given player.
     *
     * @param player the player
     * @return the tag resolver
     */
    public @NotNull TagResolver papiTag(final @NotNull Player player) {
        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            final String papiPlaceholder = argumentQueue.popOr(Message.ERROR_PAPI_NEEDS_ARGUMENT.getMessage()).value();
            final String parsedPlaceholder = PlaceholderAPI.setPlaceholders(player, '%' + papiPlaceholder + '%');
            final Component componentPlaceholder = LegacyComponentSerializer.legacySection().deserialize(parsedPlaceholder);
            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }

}
