package me.dreamdevs.github.slender.commands;

import org.bukkit.command.CommandSender;

public interface ArgumentCommand {

    boolean execute(CommandSender commandSender, String[] args);

    String getHelpText();

    String getPermission();

}