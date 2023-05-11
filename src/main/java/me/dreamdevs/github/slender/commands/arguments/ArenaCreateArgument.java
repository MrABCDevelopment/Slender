package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.command.CommandSender;

public class ArenaCreateArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(args[1] == null) {
            commandSender.sendMessage(ColourUtil.colorize("&cThere's no ID! Type any id to create an arena!"));
            return true;
        }
        Arena arena = new Arena(args[1]);
        arena.setGameTime(240);
        arena.setMinPlayers(2);
        arena.setMaxPlayers(10);
        arena.setSlenderSpawnLocation(null);
        SlenderMain.getInstance().getGameManager().getArenas().add(arena);
        commandSender.sendMessage(ColourUtil.colorize("&aYou created new arena with ID: "+arena.getId()+"!"));
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender createarena <id> - creates an arena with specific ID";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}