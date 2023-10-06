package me.dreamdevs.slender.commands.partyarguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.game.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyAcceptInviteArgument implements ArgumentCommand {

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

        Player player = (Player) commandSender;
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        if(!SlenderMain.getInstance().getPartyManager().getPendingRequests().containsKey(gamePlayer)) {
            player.sendMessage(Langauge.PLAYER_NOT_PENDING_REQUEST.toString());
            return true;
        }

        Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);
        if(party != null) {
            player.sendMessage(Langauge.PARTY_IS_ALREADY_IN_PARTY.toString());
            return true;
        }

        Party toJoin = SlenderMain.getInstance().getPartyManager().getPendingRequests().get(gamePlayer);

        SlenderMain.getInstance().getPartyManager().joinParty(player, toJoin);
        return true;
    }

    @Override
    public String getHelpText() {
        return "&e/party accept - accepts invitation to party";
    }

    @Override
    public String getPermission() {
        return "stopitslender.party";
    }
}