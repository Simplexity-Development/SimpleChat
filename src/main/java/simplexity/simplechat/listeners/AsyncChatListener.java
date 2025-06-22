package simplexity.simplechat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.config.ConfigHandler;
import simplexity.simplechat.utils.ChatPermission;
import simplexity.simplechat.utils.Message;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncChatListener implements Listener {

    private static final MiniMessage miniMessage = SimpleChat.getMiniMessage();
    private static final Pattern URL_PATTERN = Pattern.compile("(?<=\\s|^)(https?://\\S+)(?=\\s|$)", Pattern.CASE_INSENSITIVE);

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent chatEvent) {
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(chatEvent.message());
        Player player = chatEvent.getPlayer();
        chatEvent.renderer((source, sourceDisplayName, chatMessage, viewer) -> {
            Component permissionParsed = permissionParsedMessage(player, plainMessage);
            return renderedMessage(player, permissionParsed, sourceDisplayName);
        });
    }

    private Component renderedMessage(Player player, Component permissionParsedMessage, Component sourceDisplayName) {
        String format = ConfigHandler.getInstance().getDefaultFormat();
        if (ConfigHandler.getInstance().shouldDoPermissionFormatting()) {
            HashMap<Permission, String> formats = ConfigHandler.getInstance().getFormats();
            HashMap<Permission, Integer> priorities = ConfigHandler.getInstance().getFormatPriority();
            int currentPriority = 0;
            for (Permission permission : formats.keySet()) {
                int priority = priorities.get(permission);
                if (currentPriority > priority) continue;
                if (!player.hasPermission(permission)) continue;
                format = formats.get(permission);
                currentPriority = priority;
            }
        }
        if (SimpleChat.isPapiEnabled()) {
            return miniMessage.deserialize(format,
                    papiTag(player),
                    Placeholder.component("player", sourceDisplayName),
                    Placeholder.component("message", permissionParsedMessage));
        }
        return miniMessage.deserialize(format,
                Placeholder.component("player", sourceDisplayName),
                Placeholder.component("message", permissionParsedMessage));
    }


    //Stolen from https://github.com/YouHaveTrouble/JustChat @YouHaveTrouble
    private Component permissionParsedMessage(Player player, String message) {
        TagResolver.Builder tagResolver = TagResolver.builder();
        if (player.hasPermission(ChatPermission.CHAT_LINKS.getPermission())) {
            message = detectAndWrapLinks(message);
            tagResolver.resolver(urlClickTag());
        }
        for (ChatPermission perm : ChatPermission.values()) {
            if (player.hasPermission(perm.getPermission()) && perm.getTagResolver() != null) {
                tagResolver.resolver(perm.getTagResolver());
            }
        }
        if (player.hasPermission(ChatPermission.SHOW_ITEM.getPermission())) {
            tagResolver.resolver(itemTag(player));
        }
        MiniMessage msgParser = MiniMessage.builder().tags(tagResolver.build()).build();
        return msgParser.deserialize(message);
    }

    /**
     * Creates a tag resolver capable of resolving PlaceholderAPI tags for a given player.
     *
     * @param player the player
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


    public static TagResolver itemTag(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.isEmpty()) {
            return Placeholder.parsed("item", ConfigHandler.getInstance().getEmptyItemFormat());
        }

        HoverEvent<HoverEvent.ShowItem> hoverEvent = itemInHand.asHoverEvent();

        return Placeholder.component("item", itemInHand.displayName().hoverEvent(hoverEvent));
    }

    public static TagResolver urlClickTag() {
        return TagResolver.resolver("link", (args, ctx) -> {
            if (!args.hasNext()) {
                throw ctx.newException("Click requires a type");
            }
            String type = args.pop().value();
            if (!type.equalsIgnoreCase("open_url")) {
                throw ctx.newException("This tag resolver is only for open URL - not other click types");
            }

            if (!args.hasNext()) {
                throw ctx.newException("Open URL requires a URL");
            }
            String url = args.pop().value();

            return Tag.styling(ClickEvent.openUrl(url));
        });
    }

    public static String detectAndWrapLinks(String input) {
        Matcher matcher = URL_PATTERN.matcher(input);
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String url = matcher.group(1);
            String linkFormat = ConfigHandler.getInstance().getLinkFormat();
            linkFormat = linkFormat.replace("<link>", url);
            String wrapped = "<click:open_url:'" + url + "'>" + linkFormat + "</click>";
            matcher.appendReplacement(stringBuilder, Matcher.quoteReplacement(wrapped));
        }
        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

}
