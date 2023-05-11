package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.commands.ArgumentCommand;
import org.bukkit.command.CommandSender;

public class ArenaAdminArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        return false;
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