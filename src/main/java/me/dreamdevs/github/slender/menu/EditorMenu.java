package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class EditorMenu {

    public EditorMenu(Player player, Arena arena) {
        Menu menu = new Menu("Map Editor", 3);
        MenuItem minimumPlayers = new MenuItem().material(Material.REDSTONE).name("&bMinimum Players: "+arena.getMinPlayers()).lore("", "&7Left-click to add 1", "&7Right-click to remove 1").action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMinPlayers(arena.getMinPlayers()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMinPlayers(arena.getMinPlayers()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem maximumPlayers = new MenuItem().material(Material.LAPIS_LAZULI).name("&bMaximum Players: "+arena.getMaxPlayers()).lore("", "&7Left-click to add 1", "&7Right-click to remove 1").action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMaxPlayers(arena.getMaxPlayers()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMaxPlayers(arena.getMaxPlayers()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem slenderLocation = new MenuItem().material(Material.REDSTONE_BLOCK).name("&bSet SlenderMan Spawn").lore("", "&7Click to set SlenderMan Spawn location").action(event -> {
            arena.setSlenderSpawnLocation(player.getLocation());
            player.sendMessage(ColourUtil.colorize("&aYou set SlenderMan spawn location!"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem gameTime = new MenuItem().material(Material.CLOCK).name("&bGame Time: "+arena.getGameTime()).lore("", "&7Left-click to add 1", "&7Right-click to remove 1").action(event -> {
            if(event.getClickType() == ClickType.LEFT) {
                arena.setMaxPlayers(arena.getGameTime()+1);
            } else if(event.getClickType() == ClickType.RIGHT) {
                arena.setMaxPlayers(arena.getGameTime()-1);
            }
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem survivorsLocations = new MenuItem().material(Material.BEACON).name("&bAdd Survivors Spawn").lore("", "&7Click to add survivors spawn location").action(event -> {
            arena.getSurvivorsLocations().add(player.getLocation());
            player.sendMessage(ColourUtil.colorize("&aYou add survivors spawn location!"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem pagesLocations = new MenuItem().material(Material.CLOCK).name("&bAdd Pages Spawn").lore("", "&7Click to add pages spawn location").action(event -> {
            arena.getPagesLocations().add(player.getLocation());
            player.sendMessage(ColourUtil.colorize("&aYou add pages spawn location!"));
            player.closeInventory();
            new EditorMenu(player, arena);
        }).build();

        MenuItem save = new MenuItem().material(Material.DIAMOND).name("&bSave Settings").lore("", "&7Click to save settings").action(event -> {
            SlenderMain.getInstance().getGameManager().saveGame(arena);
            player.sendMessage(ColourUtil.colorize("&aSaved all arena settings!"));
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