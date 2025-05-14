package simplexity.simplechat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import simplexity.simplechat.config.ConfigHandler;

public class SimpleChatReload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigHandler.getInstance().reloadConfigValues();
        sender.sendRichMessage(ConfigHandler.getInstance().getConfigReloaded());
        return true;
    }

}
