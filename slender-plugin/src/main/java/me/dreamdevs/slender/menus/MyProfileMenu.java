package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.Statistic;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import me.dreamdevs.slender.api.inventory.buttons.OpenOtherMenuItem;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.database.data.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MyProfileMenu extends ItemMenu {

	public MyProfileMenu(Player player) {
		super(Langauge.MENU_MY_PROFILE_TITLE.toString(), Size.THREE_LINE);

		GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

		List<String> list = ColourUtil.colouredLore(Langauge.MENU_MY_PROFILE_STATS_ITEM_LORE.toString()
				.replace("%WINS%", String.valueOf(gamePlayer.getStatistic(Statistic.WINS)))
				.replace("%LEVEL%", String.valueOf(gamePlayer.getStatistic(Statistic.LEVEL)))
				.replace("%EXP%", String.valueOf(gamePlayer.getStatistic(Statistic.EXP)))
				.replace("%COLLECTED_PAGES%", String.valueOf(gamePlayer.getStatistic(Statistic.COLLECTED_PAGES)))
				.replace("%KILLED_SURVIVORS%", String.valueOf(gamePlayer.getStatistic(Statistic.KILLED_SURVIVORS)))
				.replace("%KILLED_SLENDERMEN%", String.valueOf(gamePlayer.getStatistic(Statistic.KILLED_SLENDERMEN)))
				.replace("%TOTAL_KILLS%", String.valueOf((gamePlayer.getStatistic(Statistic.KILLED_SURVIVORS)+gamePlayer.getStatistic(Statistic.KILLED_SLENDERMEN)))));

		setItem(12, new MenuItem(Langauge.MENU_MY_PROFILE_STATS_ITEM_NAME.toString(), new ItemStack(Material.PAPER), list.toArray(String[]::new)));
		setItem(13, new OpenLevelsMenuButton(gamePlayer));
		setItem(14, new OpenOtherMenuItem(new SettingsMenu(gamePlayer), Langauge.MENU_MY_PROFILE_SETTINGS_ITEM_NAME.toString(), new ItemStack(Material.COAL), Langauge.MENU_MY_PROFILE_SETTINGS_ITEM_LORE.toString()));
	}

	private static class OpenLevelsMenuButton extends MenuItem {

		private final GamePlayer gamePlayer;

		public OpenLevelsMenuButton(GamePlayer gamePlayer) {
			super(Langauge.MENU_LEVELS_TITLE.toString(), new ItemStack(Material.COAL));
			this.gamePlayer = gamePlayer;
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> {
				new LevelsMenu(gamePlayer).open(event.getPlayer());
			},4L);
		}
	}

}