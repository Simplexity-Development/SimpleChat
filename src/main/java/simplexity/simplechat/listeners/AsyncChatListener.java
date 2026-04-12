package simplexity.simplechat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.config.ConfigHandler;
import simplexity.simplechat.hooks.MiniPlaceholdersHook;
import simplexity.simplechat.hooks.PapiHook;
import simplexity.simplechat.utils.ChatPermission;
import simplexity.simplechat.utils.Util;

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
        Util.debug("Chat event fired for player: " + player.getName());
        Util.debug("Plain message: " + plainMessage);
        chatEvent.renderer((source, sourceDisplayName, chatMessage, viewer) -> {
            Component permissionParsed = permissionParsedMessage(player, plainMessage);
            return renderedMessage(player, permissionParsed, sourceDisplayName);
        });
    }

    private Component renderedMessage(Player player, Component permissionParsedMessage, Component sourceDisplayName) {
        String format = ConfigHandler.getInstance().getDefaultFormat();
        Util.debug("Starting with default format: " + format);
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
            Util.debug("Format after permission check: " + format);
        }

        TagResolver.Builder resolverBuilder = TagResolver.builder()
                .resolver(Placeholder.component("player", sourceDisplayName))
                .resolver(Placeholder.component("message", permissionParsedMessage));
        if (SimpleChat.isPapiEnabled()) {
            Util.debug("PAPI is enabled, adding PAPI resolver for: " + player.getName());
            resolverBuilder.resolver(PapiHook.papiTag(player));
        }
        if (SimpleChat.isMiniPlaceholdersEnabled()) {
            Util.debug("MiniPlaceholders is enabled, adding resolver for: " + player.getName());
            resolverBuilder.resolver(MiniPlaceholdersHook.miniPlaceholderTag());
        }
        Component result = miniMessage.deserialize(format, player, resolverBuilder.build());
        Util.debug("Final rendered component: " + PlainTextComponentSerializer.plainText().serialize(result));
        return result;
    }


    //Stolen from https://github.com/YouHaveTrouble/JustChat @YouHaveTrouble
    private Component permissionParsedMessage(Player player, String message) {
        TagResolver.Builder tagResolver = TagResolver.builder();
        boolean usePermissions = ConfigHandler.getInstance().isChatParsingUsePermissions();
        if (isFeatureEnabled(player, ChatPermission.CHAT_LINKS, usePermissions)) {
            message = detectAndWrapLinks(message);
            tagResolver.resolver(urlClickTag());
        }
        for (ChatPermission perm : ChatPermission.values()) {
            if (perm.getTagResolver() == null) continue;
            if (isFeatureEnabled(player, perm, usePermissions)) {
                tagResolver.resolver(perm.getTagResolver());
            }
        }
        if (isFeatureEnabled(player, ChatPermission.SHOW_ITEM, usePermissions)) {
            tagResolver.resolver(itemTag(player));
        }
        MiniMessage msgParser = MiniMessage.builder().tags(tagResolver.build()).build();
        return msgParser.deserialize(message);
    }

    private boolean isFeatureEnabled(Player player, ChatPermission perm, boolean usePermissions) {
        if (perm.getConfigKey() == null) {
            return player.hasPermission(perm.getPermission());
        }
        if (!ConfigHandler.getInstance().isChatParsingEnabled(perm)) return false;
        return !usePermissions || player.hasPermission(perm.getPermission());
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
        return TagResolver.resolver("click", (args, ctx) -> {
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
