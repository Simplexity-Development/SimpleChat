package simplexity.simplechat.utils;

import me.clip.placeholderapi.libs.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simplexity.simplechat.SimpleChat;

public enum ChatPermission {
    //Chat Perms
    CHAT_COLOR("simplechat.chat.color", StandardTags.color()),
    CHAT_GRADIENT("simplechat.chat.gradient", StandardTags.gradient()),
    CHAT_RAINBOW("simplechat.chat.rainbow", StandardTags.rainbow()),
    CHAT_RESET("simplechat.chat.reset", StandardTags.reset()),
    CHAT_UNDERLINE("simplechat.chat.format.underline", StandardTags.decorations(TextDecoration.UNDERLINED)),
    CHAT_ITALIC("simplechat.chat.format.italic", StandardTags.decorations(TextDecoration.ITALIC)),
    CHAT_STRIKETHROUGH("simplechat.chat.format.strikethrough", StandardTags.decorations(TextDecoration.STRIKETHROUGH)),
    CHAT_BOLD("simplechat.chat.format.bold", StandardTags.decorations(TextDecoration.BOLD)),
    CHAT_OBFUSCATED("simplechat.chat.format.obfuscated", StandardTags.decorations(TextDecoration.OBFUSCATED)),
    CHAT_HOVER("simplechat.chat.special.hover", StandardTags.hoverEvent()),
    CHAT_CLICK("simplechat.chat.special.click", StandardTags.clickEvent()),
    CHAT_NEWLINE("simplechat.chat.special.newline", StandardTags.newline()),
    CHAT_INSERT("simplechat.chat.special.insert", StandardTags.insertion()),
    CHAT_KEYBIND("simplechat.chat.special.keybind", StandardTags.keybind()),
    CHAT_FONT("simplechat.chat.special.font", StandardTags.font()),
    CHAT_SELECTOR("simplechat.chat.special.selector", StandardTags.selector()),
    CHAT_TRANSITION("simplechat.chat.special.transition", StandardTags.transition()),
    CHAT_TRANSLATABLE("simplechat.chat.special.translatable", StandardTags.translatable()),
    SHOW_ITEM("simplechat.item.show-item", null),
    CHAT_RELOAD("simplechat.reload", null);

    private final String permission;
    private final TagResolver resolver;

    ChatPermission(String permission, TagResolver resolver) {
        this.permission = permission;
        this.resolver = resolver;
    }

    public String getPermission() {
        return permission;
    }

    public TagResolver getTagResolver() {
        return resolver;
    }

    public static TagResolver createItemTag(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.isEmpty()) {
            return Placeholder.unparsed("item", Message.EMPTY_FORMAT.getMessage());
        }

        HoverEvent<HoverEvent.ShowItem> hoverEvent = itemInHand.asHoverEvent();

        return Placeholder.component("item", itemInHand.displayName().hoverEvent(hoverEvent));
    }
}
