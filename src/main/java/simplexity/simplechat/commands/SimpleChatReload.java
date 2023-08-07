package simplexity.simplechat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import simplexity.simplechat.SimpleChat;
import simplexity.simplechat.utils.Message;

public class SimpleChatReload implements CommandExecutor {

    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SimpleChat.getInstance().reloadConfig();
        Message.reloadChatFormat();
        sender.sendRichMessage(Message.CONFIG_RELOAD.getMessage());
        return true;
    }
    
}
