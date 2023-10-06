package me.dreamdevs.slender.api.inventory.buttons;

import me.dreamdevs.slender.api.SlenderApi;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class OpenOtherMenuItem extends MenuItem {

	private final ItemMenu menu;

	public OpenOtherMenuItem(ItemMenu menu, String displayName, ItemStack icon, String... lore) {
		super(displayName, icon, lore);
		this.menu = menu;
	}

	@Override
	public void onItemClick(ItemClickEvent event) {
		event.setWillClose(true);

		Bukkit.getScheduler().runTaskLater(SlenderApi.plugin, () -> {
			menu.open(event.getPlayer());
		}, 3L);
	}
}