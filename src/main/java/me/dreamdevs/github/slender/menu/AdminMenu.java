package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Lobby;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AdminMenu {

    public AdminMenu(Player player) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("admin-menu-title"), 3);

        if(gamePlayer.isInArena()) {
            MenuItem forceStart = new MenuItem().material(Material.EMERALD).name(SlenderMain.getInstance().getMessagesManager().getMessage("admin-menu-force-start-game")).action(event -> {
                if(gamePlayer.getArena().isRunning()) {
                    gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("force-start-game-unsuccessfully"));
                    return;
                }
                gamePlayer.getArena().start();
                gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("force-start-game-successfully"));
            }).build();
            MenuItem forceStop = new MenuItem().material(Material.REDSTONE).name(SlenderMain.getInstance().getMessagesManager().getMessage("admin-menu-force-stop-game")).action(event -> {
                if(!gamePlayer.getArena().isRunning()) {
                    gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("force-stop-game-unsuccessfully"));
                    return;
                }
                gamePlayer.getArena().endGame();
                gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("force-stop-game-successfully"));
            }).build();

            MenuItem forceRestart = new MenuItem().material(Material.SLIME_BALL).name(SlenderMain.getInstance().getMessagesManager().getMessage("admin-menu-force-restart-game")).action(event -> {
                gamePlayer.getArena().restart();
                gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("force-restart-game-successfully"));
            }).build();

            menu.setItem(12, forceStart);
            menu.setItem(13, forceStop);
            menu.setItem(14, forceRestart);
        } else {
            MenuItem setLobby = new MenuItem().material(Material.BEACON).name(SlenderMain.getInstance().getMessagesManager().getMessage("admin-menu-set-lobby-location")).action(event -> {
                Lobby lobby = SlenderMain.getInstance().getLobby();
                lobby.saveLobby(player);
                player.closeInventory();
                player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("lobby-set-successfully"));
            }).build();

            menu.setItem(13, setLobby);
        }


    }

}