package me.dreamdevs.github.slender.game.party;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.utils.ColourUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
public class Party {

    private final Map<GamePlayer, PartyRole> members;
    private boolean open;
    private int maxPlayers;

    public Party() {
        this.members = new HashMap<>();
        this.open = true;
        this.maxPlayers = 4;
    }

    public void sendMessage(String message) {
        members.keySet().forEach(gamePlayer -> gamePlayer.getPlayer().sendMessage(ColourUtil.colorize(message)));
    }

    public GamePlayer getLeader() {
        return members.entrySet().stream().filter(gamePlayerPartyRoleEntry -> gamePlayerPartyRoleEntry.getValue() == PartyRole.LEADER).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public List<GamePlayer> getMembersList() {
        return members.entrySet().stream().filter(entry -> entry.getValue().equals(PartyRole.MEMBER)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

}