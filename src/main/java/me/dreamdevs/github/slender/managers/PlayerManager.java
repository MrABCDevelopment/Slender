package me.dreamdevs.github.slender.managers;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.utils.CustomItem;
import me.libraryaddict.disguise.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerManager {

    private final List<GamePlayer> players;

    public PlayerManager() {
        this.players = new ArrayList<>();
    }

    public GamePlayer getPlayer(Player player) {
        return players.stream().filter(gamePlayer -> gamePlayer.getPlayer().equals(player)).findAny().orElse(null);
    }

    public void loadLobby(Player player) {
        player.getInventory().setItem(0, CustomItem.ARENA_SELECTOR.toItemStack());

        ItemStack itemStack = CustomItem.MY_PROFILE.toItemStack();
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(player);
        itemStack.setItemMeta(skullMeta);

        player.getInventory().setItem(4, itemStack);
    }

    public void sendToLobby(Player player) {
        SlenderMain.getInstance().getLobby().teleportPlayerToLobby(player);
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
        gamePlayer.clearInventory();
        player.setAllowFlight(false);
        player.setLevel(gamePlayer.getLevel());
        player.setGameMode(GameMode.ADVENTURE);
        player.setFlying(false);
        player.setFoodLevel(20);
        player.setBedSpawnLocation(null);
        player.setExp(0);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        player.setHealth(20);
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            if(SlenderMain.getInstance().getGameManager().isInArena(onlinePlayer)) {
                onlinePlayer.hidePlayer(SlenderMain.getInstance(), player);
                player.hidePlayer(SlenderMain.getInstance(), onlinePlayer);
            } else {
                onlinePlayer.showPlayer(SlenderMain.getInstance(), player);
                player.showPlayer(SlenderMain.getInstance(), onlinePlayer);
            }
        });

        if(SlenderMain.getInstance().isUseLibsDisguises())
            DisguiseAPI.undisguiseToAll(player);
    }

    public void loadData(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        SlenderMain.getInstance().getDatabase().loadData(gamePlayer);
        players.add(gamePlayer);
    }

    public void saveData(Player player) {
        GamePlayer gamePlayer = getPlayer(player);
        SlenderMain.getInstance().getDatabase().saveData(gamePlayer);
    }

}