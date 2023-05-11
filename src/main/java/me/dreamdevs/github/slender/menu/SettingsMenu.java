package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class SettingsMenu {

    public SettingsMenu(Player player) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-item-name"), 2);

        final String on = SlenderMain.getInstance().getMessagesManager().getMessage("status-on");
        final String off = SlenderMain.getInstance().getMessagesManager().getMessage("status-off");

        List<String> autoJoinModeList = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-auto-join-mode-lore")
                .replaceAll("%STATUS%", (gamePlayer.isAutoJoinMode()) ? on : off));
        List<String> showArenaJoinMessageList = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-show-arena-join-message-lore")
                .replaceAll("%STATUS%", (gamePlayer.isShowArenaJoinMessage()) ? on : off));
        List<String> messagesTypeList = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-messages-type-lore")
                .replaceAll("%TYPE%", gamePlayer.getMessagesType()));

        MenuItem autoJoinMode = new MenuItem().material(Material.APPLE).name(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-auto-join-mode-name")).lore(autoJoinModeList).action(event -> {
            gamePlayer.setAutoJoinMode(!gamePlayer.isAutoJoinMode());
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            gamePlayer.getPlayer().closeInventory();
        }).build();

        MenuItem showArenaJoinMessage = new MenuItem().material(Material.PAPER).name(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-show-arena-join-message-name")).lore(showArenaJoinMessageList).action(event -> {
            gamePlayer.setShowArenaJoinMessage(!gamePlayer.isShowArenaJoinMessage());
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            gamePlayer.getPlayer().closeInventory();
        }).build();

        MenuItem messagesType = new MenuItem().material(Material.SLIME_BALL).name(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-settings-messages-type-name")).lore(messagesTypeList).action(event -> {
            gamePlayer.setMessagesType(getNextMessageType(gamePlayer.getMessagesType()));
            gamePlayer.getPlayer().playSound(gamePlayer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            gamePlayer.getPlayer().closeInventory();
        }).build();

        MenuItem back = new MenuItem().material(Material.BARRIER).name(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-back-name")).action(event -> {
            gamePlayer.getPlayer().closeInventory();
            new MyProfileMenu(player);
        }).build();

        menu.setItem(3, autoJoinMode);
        menu.setItem(4, showArenaJoinMessage);
        menu.setItem(5, messagesType);

        menu.setItem(17, back);

        menu.open(player);
    }

    private String getNextMessageType(String message) {
        switch (message) {
            case "all":
                message = "arena";
                break;
            case "arena":
                message = "lobby";
                break;
            case "lobby":
                message = "none";
                break;
            case "none":
                message = "all";
                break;
        }
        return message;
    }

}