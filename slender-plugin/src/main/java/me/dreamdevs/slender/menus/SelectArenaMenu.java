package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.BookItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.game.Arena;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class SelectArenaMenu extends BookItemMenu {

	public SelectArenaMenu() {
		super(Langauge.MENU_ARENA_SELECTOR_TITLE.toString(), buildIcons(), false, false);
	}

	private static List<MenuItem> buildIcons() {
		return SlenderMain.getInstance().getGameManager().getArenas().stream()
				.map(SelectArenaItem::new)
				.collect(Collectors.toList());
	}

	private static class SelectArenaItem extends MenuItem {

		private final Arena arena;

		public SelectArenaItem(Arena arena) {
			super(Langauge.MENU_ARENA_SELECT_ARENA_ITEM_NAME.toString().replace("%MAP_NAME%", arena.getId()),
					new ItemStack(Material.GRASS_BLOCK),
					ColourUtil.colouredArrayLore(Langauge.MENU_ARENA_SELECT_ARENA_ITEM_LORE.toString()
							.replace("%CURRENT_PLAYERS%", String.valueOf(arena.getPlayers().size()))
							.replace("%MAX_PLAYERS%", String.valueOf(arena.getMaxPlayers()))
							.replace("%ARENA_STATUS%", arena.getArenaState().name())));
			this.arena = arena;
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			SlenderMain.getInstance().getGameManager().joinGame(event.getPlayer(), arena);
		}
	}
}