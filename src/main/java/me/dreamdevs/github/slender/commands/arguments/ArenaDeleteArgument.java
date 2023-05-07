package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.command.CommandSender;

public class ArenaDeleteArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(args.length != 2) {
            commandSender.sendMessage(ColourUtil.colorize("&cLet's try with /stopitslender arenadelete <id>"));
            return true;
        }
        String id = args[1];
        if(SlenderMain.getInstance().getGameManager().getArena(id) == null) {
            commandSender.sendMessage(ColourUtil.colorize("&cArena does not exist!"));
            return true;
        }
        Arena arena = SlenderMain.getInstance().getGameManager().getArena(id);
        if(!arena.getPlayers().isEmpty()) {
            commandSender.sendMessage(ColourUtil.colorize("&cYou cannot do this if someone is on arena!"));
            return true;
        }
        arena.getFile().delete();
        SlenderMain.getInstance().getGameManager().getArenas().remove(arena);
        commandSender.sendMessage(ColourUtil.colorize("&aRemoved an arena!"));
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender arenadelete <id> - deletes an arena with specific ID";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}