package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Role;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class SpectatorMenu {

    public SpectatorMenu(Player player) {
        Menu menu = new Menu("Spectator Menu", 3);
        MenuItem speed1 = new MenuItem().material(Material.LEATHER_BOOTS).name("&aSpeed I").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
            player.closeInventory();
        }).build();

        MenuItem speed2 = new MenuItem().material(Material.GOLDEN_BOOTS).name("&aSpeed II").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            player.closeInventory();
        }).build();

        MenuItem speed3 = new MenuItem().material(Material.CHAINMAIL_BOOTS).name("&aSpeed III").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            player.closeInventory();
        }).build();

        MenuItem teleportToRandomPlayer = new MenuItem().material(Material.PLAYER_HEAD).name("&aRandom Player").action(event -> {
            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
            Arena arena = gamePlayer.getArena();
            Location location = arena.getPlayers().entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() == Role.SURVIVOR || playerRoleEntry.getValue() == Role.SLENDER).map(Map.Entry::getKey).map(Player::getLocation).findAny().orElse(null);
            if(location != null) {
                player.teleport(location);
            }
            player.closeInventory();
        }).build();

        menu.setItem(10, speed1);
        menu.setItem(11, speed2);
        menu.setItem(12, speed3);
        menu.setItem(16, teleportToRandomPlayer);

        menu.open(player);
    }

}