package me.dreamdevs.slender.listeners;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.game.ArenaState;
import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.menus.*;
import me.dreamdevs.slender.game.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return;
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();
            Player player = event.getPlayer();
            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.ARENA_SELECTOR.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.ARENA_SELECTOR.getLore())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());

                new SelectArenaMenu().open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.LEAVE.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.LEAVE.getLore())) {
                event.setCancelled(true);
                Arena arena = (Arena) gamePlayer.getArena();
                SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), arena);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.MY_PROFILE.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.MY_PROFILE.getLore())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new MyProfileMenu(player).open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PLAY_AGAIN.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.PLAY_AGAIN.getLore())) {
                event.setCancelled(true);
                Arena arena = (Arena) gamePlayer.getArena();
                Arena randomArena = SlenderMain.getInstance().getGameManager().getArenas()
                        .stream().filter(rArena -> (rArena.getArenaState() == ArenaState.WAITING
                                || rArena.getArenaState() == ArenaState.STARTING)
                                && !rArena.getPlayers().containsKey(player)).findFirst().orElse(null);
                if(randomArena == null) {
                    player.sendMessage(Langauge.ARENA_NO_AVAILABLE_ARENAS.toString());
                    return;
                }
                SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), arena);
                SlenderMain.getInstance().getGameManager().joinGame(player, randomArena);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.SPECTATOR_SETTINGS.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new SpectatorSettingsMenu().open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PARTY_MENU.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new PartyMenu(gamePlayer).open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PERKS.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new PerkMenu().open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.SPECTATOR_TELEPORTER.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new TeleporterMenu((Arena) gamePlayer.getArena()).open(player);
            }

            if (itemStack.getType() == Material.COMPASS) {
                event.setCancelled(true);
                Player target = gamePlayer.getArena().getPlayers().entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() == Role.SURVIVOR).map(Map.Entry::getKey).findAny().orElse(null);
                if(target == null)
                    return;

                player.setCompassTarget(target.getLocation());
            }

        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if(event.getWhoClicked().getGameMode() == GameMode.CREATIVE)
            return;
        event.setResult(Event.Result.DENY);
    }

    @EventHandler
    public void inventoryClick(InventoryDragEvent event) {
        if(event.getWhoClicked().getGameMode() == GameMode.CREATIVE)
            return;
        event.setResult(Event.Result.DENY);
    }

}
