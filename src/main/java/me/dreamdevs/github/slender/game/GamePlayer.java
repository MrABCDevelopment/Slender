package me.dreamdevs.github.slender.game;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.game.perks.Perks;
import org.bukkit.entity.Player;

@Setter @Getter
public class GamePlayer {

    private final Player player;

    // Player's stats
    private int wins;
    private int collectedPages;
    private int level;
    private int exp;
    private int killedSurvivors;
    private int killedSlenderMen;

    // Player's settings
    private boolean autoJoinMode;
    private boolean showArenaJoinMessage;
    private String messagesType;

    private Perks equippedSurvivorPerk;
    private Perks equippedSlenderManPerk;

    public GamePlayer(Player player) {
        this.player = player;
    }

    public void clearInventory() {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setExtraContents(null);
    }

    public boolean isInArena() {
        return getArena() != null;
    }

    public Arena getArena() {
        return SlenderMain.getInstance().getGameManager().getArenas().stream().filter(arena -> arena.getPlayers().containsKey(player)).findFirst().orElse(null);
    }

}