package me.dreamdevs.github.slender.commands;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.arguments.ArenaCreateArgument;
import me.dreamdevs.github.slender.commands.arguments.ArenaDeleteArgument;
import me.dreamdevs.github.slender.commands.arguments.ArenaEditArgument;
import me.dreamdevs.github.slender.commands.arguments.SetLobbyArgument;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommandHandler implements TabExecutor {

    private @Getter HashMap<String, Class<? extends ArgumentCommand>> arguments;

    public CommandHandler(SlenderMain plugin) {
        this.arguments = new HashMap<>();
        registerCommand("setlobby", SetLobbyArgument.class);
        registerCommand("createarena", ArenaCreateArgument.class);
        registerCommand("editarena", ArenaEditArgument.class);
        registerCommand("deletearena", ArenaDeleteArgument.class);
        plugin.getCommand("stopitslender").setExecutor(this);
        plugin.getCommand("stopitslender").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            if(strings.length >= 1) {
                if(arguments.containsKey(strings[0])) {
                    Class<? extends ArgumentCommand> argumentCommand = arguments.get(strings[0]).asSubclass(ArgumentCommand.class);
                    ArgumentCommand argument = argumentCommand.newInstance();
                    if(commandSender.hasPermission(argument.getPermission())) {
                        argument.execute(commandSender, strings);
                    } else {
                        commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-permission"));
                    }
                    return true;
                } else {
                    commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-argument"));
                    return true;
                }
            } else {
                commandSender.sendMessage(ColourUtil.colorize("&aHelp for Stop It Slender:"));
                for(Class<? extends ArgumentCommand> argumentCommand : arguments.values()) {
                    commandSender.sendMessage(ColourUtil.colorize(argumentCommand.newInstance().getHelpText()));
                }
                return true;
            }
        } catch (Exception e) {

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();
        if(strings.length == 1) {
            StringUtil.copyPartialMatches(strings[0], arguments.keySet(), completions);
            Collections.sort(completions);
            return completions;
        } else return Collections.emptyList();
    }

    public void registerCommand(String command, Class<? extends ArgumentCommand> clazz) {
        arguments.put(command, clazz);
    }

}