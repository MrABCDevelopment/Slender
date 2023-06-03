package me.dreamdevs.github.slender.commands.partyarguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.party.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyDeleteArgument implements ArgumentCommand {

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

        Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);
        if(party != null) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-is-already-in-party"));
            return true;
        }

        if(!party.getLeader().equals(gamePlayer)) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-not-leader"));
            return true;
        }

        party.getMembers().forEach((member, role) -> {
            party.getMembers().remove(member);
            member.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-removed"));
        });

        SlenderMain.getInstance().getPartyManager().getParties().remove(party);
        return true;
    }

    @Override
    public String getHelpText() {
        return "&e/party delete - deletes your party";
    }

    @Override
    public String getPermission() {
        return "stopitslender.party";
    }
}