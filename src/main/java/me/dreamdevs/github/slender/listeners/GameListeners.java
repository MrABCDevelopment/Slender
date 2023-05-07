package me.dreamdevs.github.slender.listeners;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.ArenaState;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Role;
import me.dreamdevs.github.slender.utils.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GameListeners implements Listener {

    @EventHandler
    public void damageEvent(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();
        Player entity = (Player) event.getEntity();

        GamePlayer attackerPlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(damager);
        GamePlayer victimPlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(entity);

        if(!attackerPlayer.isInArena() || !victimPlayer.isInArena()) {
            event.setCancelled(true);
            return;
        }

        Arena arena = attackerPlayer.getArena();
        if(arena.getPlayers().get(damager) == Role.SPECTATOR || arena.getPlayers().get(damager) == Role.NONE) {
            event.setCancelled(true);
            return;
        }

        if(arena.getPlayers().get(damager) == Role.SURVIVOR && arena.getPlayers().get(entity) == Role.SURVIVOR) {
            event.setCancelled(true);
        }
     }

    @EventHandler
    public void deathPlayer(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.setNewTotalExp(0);
        event.setNewLevel(0);
        event.getDrops().clear();

        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getEntity());
        if(!gamePlayer.isInArena())
            return;
        Arena arena = gamePlayer.getArena();

        if(arena.getPlayers().get(gamePlayer.getPlayer()) == Role.SURVIVOR) {
            GamePlayer slender = SlenderMain.getInstance().getPlayerManager().getPlayer(arena.getSlenderMan());
            SlenderMain.getInstance().getPlayerManager().addExp(gamePlayer, 5);
            slender.setKilledSurvivors(slender.getKilledSurvivors()+1);

            arena.getPlayers().put(gamePlayer.getPlayer(), Role.SPECTATOR);
            if(arena.getSurvivorsAmount() == 0) {
                arena.endGame();
            }

            event.getEntity().getLocation().getWorld().strikeLightningEffect(event.getEntity().getLocation());
            arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-killed-by-slenderman").replaceAll("%PLAYER%", gamePlayer.getPlayer().getName()));
            Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> {
                gamePlayer.getPlayer().spigot().respawn();
                gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-spectator-mode"));
            }, 4L);

        }

        if(arena.getPlayers().get(gamePlayer.getPlayer()) == Role.SLENDER) {
            arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-slenderman-killed"));
            GamePlayer killer = SlenderMain.getInstance().getPlayerManager().getPlayer(gamePlayer.getPlayer().getKiller());
            killer.setKilledSlenderMen(killer.getKilledSlenderMen()+1);
            SlenderMain.getInstance().getPlayerManager().addExp(killer, 10);
            Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> gamePlayer.getPlayer().spigot().respawn(), 4L);
        }
    }

    @EventHandler
    public void respawnEvent(PlayerRespawnEvent event) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        if(!gamePlayer.isInArena())
            return;
        Arena arena = gamePlayer.getArena();
        if((arena.getPlayers().get(gamePlayer.getPlayer()) == Role.SURVIVOR || arena.getPlayers().get(gamePlayer.getPlayer()) == Role.SPECTATOR || arena.getPlayers().get(gamePlayer.getPlayer()) == Role.NONE) && (arena.getArenaState() == ArenaState.RUNNING || arena.getArenaState() == ArenaState.ENDING)) {
            event.setRespawnLocation(gamePlayer.getArena().getSlenderSpawnLocation());
            arena.getPlayers().entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() != Role.SPECTATOR).map(Map.Entry::getKey).forEach(player -> player.hidePlayer(SlenderMain.getInstance(), gamePlayer.getPlayer()));
            gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE));
            gamePlayer.getPlayer().getInventory().setItem(4, CustomItem.SPECTATOR_TOOL.toItemStack());
            gamePlayer.getPlayer().getInventory().setItem(7, CustomItem.PLAY_AGAIN.toItemStack());
            gamePlayer.getPlayer().getInventory().setItem(8, CustomItem.LEAVE.toItemStack());
            gamePlayer.getPlayer().setAllowFlight(true);
            gamePlayer.getPlayer().setFlying(true);
        } else if(arena.getPlayers().get(gamePlayer.getPlayer()) == Role.SLENDER) {
            event.setRespawnLocation(gamePlayer.getArena().getSlenderSpawnLocation());
            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().setItem(0, CustomItem.SLENDERMAN_WEAPON.toItemStack());
            event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
            event.getPlayer().setHealth(40);
            Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE)), 2L);
        }
    }

    @EventHandler
    public void pickupEvent(PlayerPickupItemEvent event) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        event.setCancelled(true);
        if(!gamePlayer.isInArena())
            return;
        Arena arena = gamePlayer.getArena();
        if(arena.getPlayers().get(gamePlayer.getPlayer()) != Role.SURVIVOR)
            return;

        event.getItem().remove();
        arena.setCollectedPages(arena.getCollectedPages()+1);
        gamePlayer.setCollectedPages(gamePlayer.getCollectedPages()+1);
        SlenderMain.getInstance().getPlayerManager().addExp(gamePlayer, 5);
        if(arena.getCollectedPages() == 8) {
            arena.endGame();
            return;
        }
        arena.spawnPage();
    }

}