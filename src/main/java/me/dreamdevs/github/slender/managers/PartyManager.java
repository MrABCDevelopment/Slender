package me.dreamdevs.github.slender.managers;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.party.Party;
import me.dreamdevs.github.slender.game.party.PartyRole;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class PartyManager {

    private final List<Party> parties;
    private final Map<GamePlayer, Party> pendingRequests;

    public PartyManager() {
        this.parties = new ArrayList<>();
        this.pendingRequests = new HashMap<>();
    }

    public void joinParty(Player player, Party party) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
        if(isInParty(gamePlayer)) {
            gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-is-already-in-party"));
            return;
        }
        party.getMembers().put(gamePlayer, PartyRole.MEMBER);
        party.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-joined-to-party").replaceAll("%PLAYER%", player.getName()));
    }

    public void leaveParty(Player player, Party party) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
        if(!isInParty(gamePlayer)) {
            gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-is-not-in-party"));
            return;
        }
        party.getMembers().remove(gamePlayer);
        party.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-left-from-party").replaceAll("%PLAYER%", player.getName()));
    }

    public void invitePlayer(Player player, Party party) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
        pendingRequests.put(gamePlayer, party);
        TextComponent textComponent = new TextComponent(SlenderMain.getInstance().getMessagesManager().getMessage("party-request-message"));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));
        textComponent.setUnderlined(true);
        gamePlayer.getPlayer().spigot().sendMessage(textComponent);
    }

    public boolean isInParty(GamePlayer gamePlayer) {
        return getParty(gamePlayer) != null;
    }

    public Party getParty(GamePlayer gamePlayer) {
        return parties.stream().filter(party -> party.getMembers().containsKey(gamePlayer)).findAny().orElse(null);
    }

    public Party getRandomParty() {
        return parties.stream().filter(Party::isOpen).findAny().orElse(null);
    }

}