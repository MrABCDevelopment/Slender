package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.menu.EditorMenu;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaEditArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("console-only-player"));
            return true;
        }
        if(args.length > 2) {
            commandSender.sendMessage(ColourUtil.colorize("&cToo many arguments!"));
            return true;
        }
        if(args[1] == null) {
            commandSender.sendMessage(ColourUtil.colorize("&cThere's no ID! Type any id to create an arena!"));
            return true;
        }
        Player player = (Player) commandSender;
        Arena arena = SlenderMain.getInstance().getGameManager().getArena(args[1]);
        if(arena == null) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-arena"));
            return true;
        }
        new EditorMenu(player, arena);
        return true;
    }

    @Override
    public String getHelpText() {
        return "&a/stopitslender editarena <id> - edits an arena with specific ID";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}