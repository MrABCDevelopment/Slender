package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class EditorMenu {

    public EditorMenu(Player player, Arena arena) {
        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-title"), 3);

        List<String> listMin = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-minimum-players-lore"));
        List<String> listMax = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-maximum-players-lore"));
        List<String> listSlenderSpawn = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-set-slenderman-spawn-lore"));
        List<String> listGameTime = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-game-time-lore"));
        List<String> listSurvivorsSpawn = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-survivors-spawn-lore"));
        List<String> listPages = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-pages-spawn-lore"));
        List<String> listSave = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-save-all-lore"));

        MenuItem minimumPlayers = new MenuItem().material(Material.REDSTONE).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-minimum-players").replaceAll("%AMOUNT%", String.valueOf(arena.getMinPlayers()))).lore(listMin).action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMinPlayers(arena.getMinPlayers()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMinPlayers(arena.getMinPlayers()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem maximumPlayers = new MenuItem().material(Material.LAPIS_LAZULI).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-maximum-players").replaceAll("%AMOUNT%", String.valueOf(arena.getMaxPlayers()))).lore(listMax).action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMaxPlayers(arena.getMaxPlayers()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMaxPlayers(arena.getMaxPlayers()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem slenderLocation = new MenuItem().material(Material.REDSTONE_BLOCK).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-set-slenderman-spawn")).lore(listSlenderSpawn).action(event -> {
            arena.setSlenderSpawnLocation(player.getLocation());
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("slenderman-spawn-set-successfully"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem gameTime = new MenuItem().material(Material.CLOCK).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-game-time").replaceAll("%AMOUNT%", String.valueOf(arena.getGameTime()))).lore(listGameTime).action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMaxPlayers(arena.getGameTime()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMaxPlayers(arena.getGameTime()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem survivorsLocations = new MenuItem().material(Material.BEACON).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-survivors-spawn")).lore(listSurvivorsSpawn).action(event -> {
            arena.getSurvivorsLocations().add(player.getLocation());
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("survivors-spawn-add-successfully"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem pagesLocations = new MenuItem().material(Material.CLOCK).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-add-pages-spawn")).lore(listPages).action(event -> {
            arena.getPagesLocations().add(player.getLocation());
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("pages-spawn-add-successfully"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem save = new MenuItem().material(Material.DIAMOND).name(SlenderMain.getInstance().getMessagesManager().getMessage("editor-menu-save-all")).lore(listSave).action(event -> {
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