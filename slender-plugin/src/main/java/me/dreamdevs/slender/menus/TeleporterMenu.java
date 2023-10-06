package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.game.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TeleporterMenu extends ItemMenu {

	public TeleporterMenu(Arena arena) {
		super(Langauge.MENU_SPECTATOR_TELEPORTER_TITLE.toString(), Size.fit(arena.getSurvivorsAmount()));

		int x = 0;
		for (Player player : arena.getSurvivors()) {
			this.setItem(x, new TeleportToPlayerItem(player));
			x++;
		}
	}

	private static class TeleportToPlayerItem extends MenuItem {

		private final Player target;

		public TeleportToPlayerItem(Player target) {
			super(ColourUtil.colorize("&a"+target.getName()), new ItemStack(Material.PLAYER_HEAD));
			this.target = target;
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			event.getPlayer().teleport(target);
		}
	}

}