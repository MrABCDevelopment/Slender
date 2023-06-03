package me.dreamdevs.github.slender.commands.partyarguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.party.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyAcceptInviteArgument implements ArgumentCommand {

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

        Player player = (Player) commandSender;
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        if(!SlenderMain.getInstance().getPartyManager().getPendingRequests().containsKey(gamePlayer)) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-is-not-pending-request"));
            return true;
        }

        Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);
        if(party != null) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-is-already-in-party"));
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