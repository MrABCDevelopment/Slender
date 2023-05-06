package me.dreamdevs.github.slender.game;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.SlenderMain;
import org.bukkit.entity.Player;

@Setter @Getter
public class GamePlayer {

    private final Player player;

    // Player's stats
    private int wins;
    private int collectedPages;
    private int level;
    private int exp;

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