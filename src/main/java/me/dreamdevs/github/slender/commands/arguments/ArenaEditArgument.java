package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.commands.ArgumentCommand;
import org.bukkit.command.CommandSender;

public class ArenaEditArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {

        return true;
    }

    @Override
    public String getHelpText() {
        return "";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}