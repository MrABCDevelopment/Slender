package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.Statistic;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.BookItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.game.Level;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class LevelsMenu extends BookItemMenu {

	public LevelsMenu(GamePlayer gamePlayer) {
		super(Langauge.MENU_LEVELS_TITLE.toString(), buildItems(gamePlayer), false, false);
	}

	private static List<MenuItem> buildItems(GamePlayer gamePlayer) {
		return SlenderMain.getInstance().getLevelManager().getLevels().entrySet().stream()
				.map(integerLevelEntry -> new LevelItem(gamePlayer, integerLevelEntry.getKey(), integerLevelEntry.getValue()))
				.collect(Collectors.toList());
	}

	private static class LevelItem extends MenuItem {

		public LevelItem(GamePlayer gamePlayer, int number, Level level) {
			super(Langauge.MENU_LEVELS_LEVEL_ITEM_NAME.toString().replace("%NUMBER%", String.valueOf(number)),
					new ItemStack(Material.COAL),
					(gamePlayer.getStatistic(Statistic.LEVEL) >= number) ? "&a&l✔" : "&c&l✘");
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			// Do nothing...
		}
	}

}