package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class MyProfileMenu {

    public MyProfileMenu(Player player) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-menu-title"), 3);

        List<String> list = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-stats-item-lore")
                .replaceAll("%WINS%", String.valueOf(gamePlayer.getWins()))
                .replaceAll("%LEVEL%", String.valueOf(gamePlayer.getLevel()))
                .replaceAll("%EXP%", String.valueOf(gamePlayer.getExp()))
                .replaceAll("%COLLECTED_PAGES%", String.valueOf(gamePlayer.getCollectedPages()))
                .replaceAll("%KILLED_SURVIVORS%", String.valueOf(gamePlayer.getKilledSurvivors()))
                .replaceAll("%KILLED_SLENDERMEN%", String.valueOf(gamePlayer.getKilledSlenderMen()))
                .replaceAll("%TOTAL_KILLS%", String.valueOf((gamePlayer.getKilledSlenderMen()+gamePlayer.getKilledSurvivors()))));

        MenuItem stats = new MenuItem().material(Material.PAPER).name(SlenderMain.getInstance().getMessagesManager().getMessage("my-profile-stats-item-name"))
                .lore(list).build();

        menu.setItem(13, stats);

        menu.open(player);
    }

}