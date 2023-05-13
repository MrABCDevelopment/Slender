package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.menu.AdminMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaAdminArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("console-only-player"));
            return true;
        }
        new AdminMenu((Player) commandSender);
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender admin - opens custom admin inventory";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}