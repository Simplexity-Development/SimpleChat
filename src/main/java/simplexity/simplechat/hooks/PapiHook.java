package simplexity.simplechat.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.utils.Message;

public class PapiHook {
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
                componentPlaceholder = SimpleChat.getMiniMessage().deserialize(parsedPlaceholder);
            }
            return Tag.selfClosingInserting(componentPlaceholder);
        });
    }
}
