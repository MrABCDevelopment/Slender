package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaLeaveArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("console-only-player"));
            return true;
        }
        if(args.length > 1) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("too-many-arguments"));
            return true;
        }

        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer((Player) commandSender);

        SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), gamePlayer.getArena());
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