package me.dreamdevs.slender.commands.partyarguments;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.commands.ArgumentCommand;
import me.dreamdevs.slender.api.game.party.PartyRole;
import me.dreamdevs.slender.api.game.party.PartySettings;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.game.Party;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCreateArgument implements ArgumentCommand {

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

        Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);
        if(party != null) {
            player.sendMessage(Langauge.PARTY_IS_ALREADY_IN_PARTY.toString());
            return true;
        }

        Party newParty = new Party();
        newParty.setPartySetting(PartySettings.OPEN_PARTY, false);
        newParty.setPartySetting(PartySettings.MAX_PLAYERS, 8);
        newParty.setPartySetting(PartySettings.CHAT_PARTY, false);

        newParty.getMembersMap().put(gamePlayer, PartyRole.LEADER);
        SlenderMain.getInstance().getPartyManager().getParties().add(newParty);

        player.sendMessage(Langauge.PARTY_CREATED_INFO.toString());
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