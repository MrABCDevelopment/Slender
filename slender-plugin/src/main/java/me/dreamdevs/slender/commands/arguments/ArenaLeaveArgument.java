package me.dreamdevs.slender.commands.arguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaLeaveArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Langauge.ADMIN_ONLY_PLAYER.toString());
            return false;
        }
        if(args.length > 1) {
            commandSender.sendMessage(Langauge.ARENA_TOO_MANY_ARGUMENTS.toString());
            return false;
        }

        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer((Player) commandSender);

        SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), (Arena) gamePlayer.getArena());
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender leave - use this command to leave from arena";
    }

    @Override
    public String getPermission() {
        return "stopitslender.player";
    }
}