package me.dreamdevs.github.slender.commands.arguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("console-only-player"));
            return true;
        }
        Player player = (Player) commandSender;
        Lobby lobby = SlenderMain.getInstance().getLobby();
        lobby.saveLobby(player);
        player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("lobby-set-successfully"));
        return true;
    }

    @Override
    public String getHelpText() {
        return "&c/stopitslender setlobby - sets the lobby to your location";
    }

    @Override
    public String getPermission() {
        return "stopitslender.admin";
    }
}