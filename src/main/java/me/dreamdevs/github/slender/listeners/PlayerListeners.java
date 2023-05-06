package me.dreamdevs.github.slender.listeners;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Role;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    @EventHandler
    public void joinPlayer(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        SlenderMain.getInstance().getPlayerManager().loadData(event.getPlayer());

        SlenderMain.getInstance().getPlayerManager().sendToLobby(event.getPlayer());
        SlenderMain.getInstance().getPlayerManager().loadLobby(event.getPlayer());
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        if(gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if(arena.getPlayers().get(gamePlayer.getPlayer()) == Role.SLENDER) {
                arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-slenderman-left-game"));
                arena.restart();
            }

            arena.getPlayers().remove(gamePlayer.getPlayer());
        }

        SlenderMain.getInstance().getPlayerManager().saveData(event.getPlayer());
        SlenderMain.getInstance().getPlayerManager().getPlayers().remove(gamePlayer);
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getPlayer());
        if(gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();
            arena.getPlayers().keySet().forEach(player -> player.sendMessage(ColourUtil.colorize("&7"+player.getName()+" » &r")+event.getMessage()));
        } else {
            Bukkit.getOnlinePlayers().stream().filter(player -> !SlenderMain.getInstance().getGameManager().isInArena(player))
                    .forEach(player -> player.sendMessage(ColourUtil.colorize("&7"+player.getName()+" » &r")+event.getMessage()));
        }
    }

    @EventHandler
    public void changeFoodEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void dropEvent(PlayerDropItemEvent event) {
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

}