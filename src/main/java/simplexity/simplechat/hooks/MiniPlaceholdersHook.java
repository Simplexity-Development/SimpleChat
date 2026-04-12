package simplexity.simplechat.hooks;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MiniPlaceholdersHook {

    public static TagResolver miniPlaceholderTag(){
        return TagResolver.resolver(
                MiniPlaceholders.globalPlaceholders(),
                MiniPlaceholders.audiencePlaceholders()
        );
    }
}
