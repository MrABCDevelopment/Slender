package me.dreamdevs.github.slender.listeners;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.*;
import me.dreamdevs.github.slender.menu.MyProfileMenu;
import me.dreamdevs.github.slender.menu.PartyMenu;
import me.dreamdevs.github.slender.menu.PerksMenu;
import me.dreamdevs.github.slender.menu.SpectatorMenu;
import me.dreamdevs.github.slender.utils.CustomItem;
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
import org.bukkit.inventory.meta.CompassMeta;

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
                Menu menu = new Menu(SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getString("items.arena-selector.DisplayName"), 6);
                SlenderMain.getInstance().getGameManager().getArenas().forEach(arena -> {
                    MenuItem menuItem = new MenuItem().material(Material.GRASS_BLOCK).name("&aArena "+arena.getId())
                            .lore("", "&7Players: &b"+arena.getPlayers().size()+"/"+arena.getMaxPlayers(),
                                    "&7Status: &b"+arena.getArenaState().name(),
                                    "",
                                    "&7Click to join to the arena.")
                            .action(itemAction -> {
                                itemAction.getPlayer().closeInventory();
                                SlenderMain.getInstance().getGameManager().joinGame(player, arena);
                            }).build();
                    menu.addItem(menuItem);
                });
                menu.open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.LEAVE.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.LEAVE.getLore())) {
                event.setCancelled(true);
                Arena arena = gamePlayer.getArena();
                SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), arena);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.MY_PROFILE.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.MY_PROFILE.getLore())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new MyProfileMenu(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PLAY_AGAIN.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.PLAY_AGAIN.getLore())) {
                event.setCancelled(true);
                Arena arena = gamePlayer.getArena();
                Arena randomArena = SlenderMain.getInstance().getGameManager().getArenas()
                        .stream().filter(rArena -> (rArena.getArenaState() == ArenaState.WAITING
                                || rArena.getArenaState() == ArenaState.STARTING)
                                && !rArena.getPlayers().containsKey(player)).findFirst().orElse(null);
                if(randomArena == null) {
                    player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-available-arenas"));
                    return;
                }
                SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), arena);
                SlenderMain.getInstance().getGameManager().joinGame(player, randomArena);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.SPECTATOR_TOOL.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new SpectatorMenu(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PARTY_MENU.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new PartyMenu(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PERKS.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new PerksMenu(player);
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
