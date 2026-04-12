package simplexity.simplechat.utils;

import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.permissions.PermissionDefault;

public enum ChatPermission {
    CHAT_COLOR("simplechat.chat.color", StandardTags.color(), PermissionDefault.OP, "color", "Allows player to use colors in chat"),
    CHAT_GRADIENT("simplechat.chat.gradient", StandardTags.gradient(), PermissionDefault.OP, "gradient", "Allows player to use gradients in chat"),
    CHAT_RAINBOW("simplechat.chat.rainbow", StandardTags.rainbow(), PermissionDefault.OP, "rainbow", "Allows player to use the rainbow tag in chat"),
    CHAT_RESET("simplechat.chat.reset", StandardTags.reset(), PermissionDefault.OP, "reset", "Allows player to reset their chat formatting"),
    CHAT_UNDERLINE("simplechat.chat.format.underline", StandardTags.decorations(TextDecoration.UNDERLINED), PermissionDefault.OP, "underline", "Allows player to use underline in chat"),
    CHAT_ITALIC("simplechat.chat.format.italic", StandardTags.decorations(TextDecoration.ITALIC), PermissionDefault.OP, "italic", "Allows player to use italics in chat"),
    CHAT_STRIKETHROUGH("simplechat.chat.format.strikethrough", StandardTags.decorations(TextDecoration.STRIKETHROUGH), PermissionDefault.OP, "strike-through", "Allows player to use strikethrough in chat"),
    CHAT_BOLD("simplechat.chat.format.bold", StandardTags.decorations(TextDecoration.BOLD), PermissionDefault.OP, "bold", "Allows player to use bold formatting in chat"),
    CHAT_OBFUSCATED("simplechat.chat.format.obfuscated", StandardTags.decorations(TextDecoration.OBFUSCATED), PermissionDefault.OP, "obfuscated", "Allows player to use obfuscation formatting in chat"),
    CHAT_HOVER("simplechat.chat.special.hover", StandardTags.hoverEvent(), PermissionDefault.FALSE, "hover", "Allows player to use hover tags in chat"),
    CHAT_CLICK("simplechat.chat.special.click", StandardTags.clickEvent(), PermissionDefault.FALSE, "click", "Allows player to use clickable tags in chat"),
    CHAT_NEWLINE("simplechat.chat.special.newline", StandardTags.newline(), PermissionDefault.FALSE, "newline", "Allows player to use new line tags in chat"),
    CHAT_INSERT("simplechat.chat.special.insert", StandardTags.insertion(), PermissionDefault.FALSE, "insert", "Allows player to use insertion tags in chat"),
    CHAT_KEYBIND("simplechat.chat.special.keybind", StandardTags.keybind(), PermissionDefault.FALSE, null, "Allows player to use keybind tags in chat"),
    CHAT_FONT("simplechat.chat.special.font", StandardTags.font(), PermissionDefault.FALSE, "font", "Allows player to use font tags in chat"),
    CHAT_SELECTOR("simplechat.chat.special.selector", StandardTags.selector(), PermissionDefault.FALSE, "selector", "Allows player to use selector tags in chat"),
    CHAT_TRANSITION("simplechat.chat.special.transition", StandardTags.transition(), PermissionDefault.FALSE, "transition", "Allows player to use transition tags in chat"),
    CHAT_TRANSLATABLE("simplechat.chat.special.translatable", StandardTags.translatable(), PermissionDefault.FALSE, "translatable", "Allows player to use translatable tags in chat"),
    CHAT_LINKS("simplechat.chat.links", null, PermissionDefault.OP, "links", "Parses links so players can click them"),
    SHOW_ITEM("simplechat.item.show-item", null, PermissionDefault.OP, "show-item", "Allows a player to use <item> to show an item in chat"),
    CHAT_RELOAD("simplechat.reload", null, PermissionDefault.OP, null, "Allows player to reload SimpleChat configuration");

    private final String permission;
    private final TagResolver resolver;
    private final PermissionDefault defaultValue;
    private final String configKey;
    private final String description;

    ChatPermission(String permission, TagResolver resolver, PermissionDefault defaultValue, String configKey, String description) {
        this.permission = permission;
        this.resolver = resolver;
        this.defaultValue = defaultValue;
        this.configKey = configKey;
        this.description = description;
    }

    public String getPermission() {
        return permission;
    }

    public TagResolver getTagResolver() {
        return resolver;
    }

    public PermissionDefault getDefault() {
        return defaultValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public String getDescription() {
        return description;
    }

}
