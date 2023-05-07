package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class EditorMenu {

    public EditorMenu(Player player, Arena arena) {
        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-title"), 3);
        MenuItem minimumPlayers = new MenuItem().material(Material.REDSTONE).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-minimum-players").replaceAll("%AMOUNT%", String.valueOf(arena.getMinPlayers()))).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-minimum-players-lore")).action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMinPlayers(arena.getMinPlayers()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMinPlayers(arena.getMinPlayers()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem maximumPlayers = new MenuItem().material(Material.LAPIS_LAZULI).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-maximum-players").replaceAll("%AMOUNT%", String.valueOf(arena.getMaxPlayers()))).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-maximum-players-lore")).action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMaxPlayers(arena.getMaxPlayers()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMaxPlayers(arena.getMaxPlayers()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem slenderLocation = new MenuItem().material(Material.REDSTONE_BLOCK).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-set-slenderman-spawn")).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-set-slenderman-spawn-lore")).action(event -> {
            arena.setSlenderSpawnLocation(player.getLocation());
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("slenderman-spawn-set-successfully"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem gameTime = new MenuItem().material(Material.CLOCK).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-game-time").replaceAll("%AMOUNT%", String.valueOf(arena.getGameTime()))).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-game-time-lore")).action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMaxPlayers(arena.getGameTime()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMaxPlayers(arena.getGameTime()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem survivorsLocations = new MenuItem().material(Material.BEACON).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-survivors-spawn")).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-survivors-spawn-lore")).action(event -> {
            arena.getSurvivorsLocations().add(player.getLocation());
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("survivors-spawn-add-successfully"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem pagesLocations = new MenuItem().material(Material.CLOCK).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-pages-spawn")).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-pages-spawn-lore")).action(event -> {
            arena.getPagesLocations().add(player.getLocation());
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("pages-spawn-add-successfully"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem save = new MenuItem().material(Material.DIAMOND).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-save-all")).lore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-save-all-lore")).action(event -> {
            SlenderMain.getInstance().getGameManager().saveGame(arena);
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("saved-arena-settings-successfully"));
            player.closeInventory();
        }).build();

        menu.setItem(10, minimumPlayers);
        menu.setItem(11, maximumPlayers);
        menu.setItem(12, gameTime);
        menu.setItem(13, slenderLocation);
        menu.setItem(14, survivorsLocations);
        menu.setItem(15, pagesLocations);
        menu.setItem(16, save);

        menu.open(player);
    }

}