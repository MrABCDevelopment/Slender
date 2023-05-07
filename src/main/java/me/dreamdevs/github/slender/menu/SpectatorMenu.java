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
        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-menu-title"), 3);
        MenuItem noSpeed = new MenuItem().material(Material.LEATHER_BOOTS).name(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-no-speed")).action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.closeInventory();
        }).build();

        MenuItem speed1 = new MenuItem().material(Material.CHAINMAIL_BOOTS).name(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-speed-word")+" I").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
            player.closeInventory();
        }).build();

        MenuItem speed2 = new MenuItem().material(Material.IRON_BOOTS).name(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-speed-word")+" II").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            player.closeInventory();
        }).build();

        MenuItem speed3 = new MenuItem().material(Material.GOLDEN_BOOTS).name(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-speed-word")+" III").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            player.closeInventory();
        }).build();

        MenuItem speed4 = new MenuItem().material(Material.DIAMOND_BOOTS).name(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-speed-word")+" IV").action(event -> {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
            player.closeInventory();
        }).build();

        MenuItem teleportToRandomPlayer = new MenuItem().material(Material.PLAYER_HEAD).name(SlenderMain.getInstance().getMessagesManager().getMessage("spectator-random-player")).action(event -> {
            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
            Arena arena = gamePlayer.getArena();
            Location location = arena.getPlayers().entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() == Role.SURVIVOR || playerRoleEntry.getValue() == Role.SLENDER).map(Map.Entry::getKey).map(Player::getLocation).findAny().orElse(null);
            if(location != null) {
                player.teleport(location);
            }
            player.closeInventory();
        }).build();

        menu.setItem(10, noSpeed);
        menu.setItem(11, speed1);
        menu.setItem(12, speed2);
        menu.setItem(13, speed3);
        menu.setItem(14, speed4);
        menu.setItem(16, teleportToRandomPlayer);

        menu.open(player);
    }

}