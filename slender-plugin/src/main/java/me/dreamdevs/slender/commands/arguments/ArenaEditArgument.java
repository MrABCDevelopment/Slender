package me.dreamdevs.slender.commands.arguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.game.Arena;
import me.dreamdevs.slender.menus.EditorMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaEditArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Langauge.ADMIN_ONLY_PLAYER.toString());
            return true;
        }
        if(args.length > 2) {
            commandSender.sendMessage(Langauge.ARENA_TOO_MANY_ARGUMENTS.toString());
            return true;
        }
        if(args[1] == null) {
            commandSender.sendMessage(Langauge.ARENA_NO_ARENA.toString());
            return true;
        }
        Player player = (Player) commandSender;
        Arena arena = SlenderMain.getInstance().getGameManager().getArena(args[1]);
        if(arena == null) {
            player.sendMessage(Langauge.ARENA_NO_ARENA.toString());
            return true;
        }
        new EditorMenu(arena).open(player);
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender editarena <id> - edits an arena with specific ID";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}