package me.dreamdevs.slender.commands.partyarguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.game.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyKickMemberArgument implements ArgumentCommand {

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

        Player player = (Player) commandSender;
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);
        if (party == null) {
            player.sendMessage(Langauge.PARTY_PLAYER_NOT_IN_PARTY.toString());
            return true;
        }

        if (!party.getPartyLeader().equals(gamePlayer.getPlayer())) {
            player.sendMessage(Langauge.PARTY_PLAYER_NOT_LEADER.toString());
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null) {
            player.sendMessage(Langauge.ARENA_NO_PLAYER.toString());
            return true;
        }

        GamePlayer targetGamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(target);
        Party targetParty = SlenderMain.getInstance().getPartyManager().getParty(targetGamePlayer);

        if(!targetParty.equals(party)) {
            player.sendMessage(Langauge.PARTY_NO_PLAYER.toString());
            return true;
        }

        party.getMembers().remove(targetGamePlayer);
        party.sendMessage(Langauge.PARTY_KICKED_PLAYER.toString());
        return true;
    }

    @Override
    public String getHelpText() {
        return "&e/party kick <player> - kicks your party member";
    }

    @Override
    public String getPermission() {
        return "stopitslender.party";
    }
}