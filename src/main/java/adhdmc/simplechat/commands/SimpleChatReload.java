package adhdmc.simplechat.commands;

import adhdmc.simplechat.SimpleChat;
import adhdmc.simplechat.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SimpleChatReload implements CommandExecutor {
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SimpleChat.getInstance().reloadConfig();
        Message.reloadChatFormat();
        sender.sendRichMessage(Message.CONFIG_RELOAD.getMessage());
        return true;
    }
    
}
