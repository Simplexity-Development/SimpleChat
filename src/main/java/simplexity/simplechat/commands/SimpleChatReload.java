package simplexity.simplechat.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import simplexity.simplechat.config.ConfigHandler;
import simplexity.simplechat.utils.ChatPermission;

@SuppressWarnings("UnstableApiUsage")
public class SimpleChatReload {

    public static LiteralCommandNode<CommandSourceStack> createCommand(){
        return Commands.literal("simplechatreload")
                .requires(css -> css.getSender().hasPermission(ChatPermission.CHAT_RELOAD.getPermission()))
                .executes(ctx -> {
                    CommandSender sender = ctx.getSource().getSender();
                    ConfigHandler.getInstance().reloadConfigValues();
                    sender.sendRichMessage(ConfigHandler.getInstance().getConfigReloaded());
                    return 1;
                }).build();
    }

}
