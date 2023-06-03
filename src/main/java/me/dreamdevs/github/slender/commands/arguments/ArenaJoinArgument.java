package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaJoinArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("console-only-player"));
            return true;
        }
        if(args.length > 2) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("too-many-arguments"));
            return true;
        }

        Arena arena = SlenderMain.getInstance().getGameManager().getArena(args[1]);
        if(arena == null) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-arena"));
            return true;
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