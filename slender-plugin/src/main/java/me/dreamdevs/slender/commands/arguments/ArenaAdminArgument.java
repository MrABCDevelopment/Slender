package me.dreamdevs.slender.commands.arguments;

import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaAdminArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Langauge.ADMIN_ONLY_PLAYER.toString());
            return true;
        }
      //  new AdminMenu((Player) commandSender);
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