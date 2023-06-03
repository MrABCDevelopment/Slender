package me.dreamdevs.github.slender.commands.partyarguments;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.commands.ArgumentCommand;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.party.Party;
import me.dreamdevs.github.slender.game.party.PartyRole;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCreateArgument implements ArgumentCommand {

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

        Party newParty = new Party();
        newParty.setOpen(false);
        newParty.setMaxPlayers(8);
        newParty.getMembers().put(gamePlayer, PartyRole.LEADER);
        SlenderMain.getInstance().getPartyManager().getParties().add(newParty);

        player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-created"));
        return true;
    }

    @Override
    public String getHelpText() {
        return "&e/party create - creates the party";
    }

    @Override
    public String getPermission() {
        return "stopitslender.party";
    }
}