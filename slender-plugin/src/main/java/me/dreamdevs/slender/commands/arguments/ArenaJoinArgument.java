package me.dreamdevs.slender.commands.arguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaJoinArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Langauge.ADMIN_ONLY_PLAYER.toString());
            return false;
        }
        if(args.length > 2) {
            commandSender.sendMessage(Langauge.ARENA_TOO_MANY_ARGUMENTS.toString());
            return false;
        }

        Arena arena = SlenderMain.getInstance().getGameManager().getArena(args[1]);
        if(arena == null) {
            commandSender.sendMessage(Langauge.ARENA_NO_ARENA.toString());
            return false;
        }

        Player player = (Player) commandSender;
        SlenderMain.getInstance().getGameManager().joinGame(player, arena);
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender join <id> - joins to an arena with specific ID";
    }

    @Override
    public String getPermission() {
        return "stopitslender.player";
    }
}