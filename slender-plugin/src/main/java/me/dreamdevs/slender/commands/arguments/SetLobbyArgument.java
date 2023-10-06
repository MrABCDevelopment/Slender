package me.dreamdevs.slender.commands.arguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.game.Lobby;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobbyArgument implements ArgumentCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(Langauge.ADMIN_ONLY_PLAYER.toString());
            return false;
        }
        Player player = (Player) commandSender;
        Lobby lobby = SlenderMain.getInstance().getLobby();
        lobby.saveLobby(player);
        player.sendMessage(Langauge.ADMIN_SET_LOBBY_SUCCESSFULLY.toString());
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