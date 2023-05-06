package me.dreamdevs.github.slender.listeners;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.ArenaState;
import me.dreamdevs.github.slender.game.GamePlayer;
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
                SlenderMain.getInstance().getGameManager().leaveGame(player, arena);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.MY_PROFILE.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.MY_PROFILE.getLore())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                Menu menu = new Menu("My Profile", 3);

                MenuItem menuItem = new MenuItem().material(Material.PAPER).name("&bYour Stats")
                        .lore("&7Wins: &b"+gamePlayer.getWins(), "&7Exp: &b"+gamePlayer.getExp(), "&7Level: &b"+gamePlayer.getLevel(), "&7Collected Pages: &b"+gamePlayer.getCollectedPages()).build();

                menu.setItem(13, menuItem);

                menu.open(player);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.PLAY_AGAIN.getDisplayName()) && itemStack.getItemMeta().getLore().equals(CustomItem.PLAY_AGAIN.getLore())) {
                event.setCancelled(true);
                Arena arena = gamePlayer.getArena();
                Arena randomArena = SlenderMain.getInstance().getGameManager().getArenas()
                        .stream().filter(rArena -> (rArena.getArenaState() == ArenaState.WAITING
                                || rArena.getArenaState() == ArenaState.STARTING)
                                && !rArena.getPlayers().containsKey(player)).findFirst().orElse(null);
                if(randomArena == null) {
                    player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("no-availble-arenas"));
                    return;
                }
                SlenderMain.getInstance().getGameManager().leaveGame(player, arena);
                SlenderMain.getInstance().getGameManager().joinGame(player, randomArena);
            }

            if (itemStack.getItemMeta().getDisplayName().equals(CustomItem.SPECTATOR_TOOL.getDisplayName())) {
                event.setCancelled(true);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, (float) Math.random());
                new SpectatorMenu(player);
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
