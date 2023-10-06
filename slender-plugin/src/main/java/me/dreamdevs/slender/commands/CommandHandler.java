package me.dreamdevs.slender.commands;

import lombok.Getter;
import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.commands.arguments.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandHandler implements TabExecutor {

    private final @Getter HashMap<String, Class<? extends ArgumentCommand>> arguments;

    public CommandHandler(SlenderMain plugin) {
        this.arguments = new HashMap<>();
        registerCommand("setlobby", SetLobbyArgument.class);
        registerCommand("admin", ArenaAdminArgument.class);
        registerCommand("createarena", ArenaCreateArgument.class);
        registerCommand("editarena", ArenaEditArgument.class);
        registerCommand("deletearena", ArenaDeleteArgument.class);
        registerCommand("join", ArenaJoinArgument.class);
        registerCommand("leave", ArenaLeaveArgument.class);
        Objects.requireNonNull(plugin.getCommand("stopitslender")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("stopitslender")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        try {
            if(strings.length >= 1) {
                if(arguments.containsKey(strings[0])) {
                    Class<? extends ArgumentCommand> argumentCommand = arguments.get(strings[0]).asSubclass(ArgumentCommand.class);
                    ArgumentCommand argument = argumentCommand.getConstructor().newInstance();
                    if(commandSender.hasPermission(argument.getPermission())) {
                        argument.execute(commandSender, strings);
                    } else {
                        commandSender.sendMessage(Langauge.ARENA_NO_PERMISSION.toString());
                    }
                    return true;
                } else {
                    commandSender.sendMessage(Langauge.ARENA_NO_ARGUMENT.toString());
                    return true;
                }
            } else {
                commandSender.sendMessage(ColourUtil.colorize("&aHelp for Stop It Slender:"));
                for(Class<? extends ArgumentCommand> argumentCommand : arguments.values()) {
                    commandSender.sendMessage(ColourUtil.colorize(argumentCommand.getConstructor().newInstance().getHelpText()));
                }
                return true;
            }
        } catch (Exception ignored) {

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
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