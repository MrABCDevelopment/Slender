package me.dreamdevs.github.slender.commands.partyarguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyKickMemberArgument implements ArgumentCommand {

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

        Player player = (Player) commandSender;
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);
        if(party == null) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-is-not-in-party"));
            return true;
        }

        if(!party.getLeader().equals(gamePlayer)) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-not-leader"));
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if(target == null) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-player"));
            return true;
        }

        GamePlayer targetGamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(target);
        Party targetParty = SlenderMain.getInstance().getPartyManager().getParty(targetGamePlayer);

        if(!targetParty.equals(party)) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-no-player"));
            return true;
        }

        party.getMembers().remove(targetGamePlayer);
        party.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-kicked-player"));
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