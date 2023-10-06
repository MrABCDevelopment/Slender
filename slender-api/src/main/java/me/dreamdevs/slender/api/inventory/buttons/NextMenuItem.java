package me.dreamdevs.slender.api.inventory.buttons;

import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NextMenuItem extends MenuItem {

	private ItemMenu nextMenu;

	public NextMenuItem(ItemMenu nextMenu) {
		super(ChatColor.GREEN+"Next Page ->", new ItemStack(Material.ARROW));
		this.nextMenu = nextMenu;
	}

	public void setNextMenu(ItemMenu nextMenu) {
		this.nextMenu = nextMenu;
	}

	@Override
	public void onItemClick(ItemClickEvent event) {
		if (this.nextMenu != null) {
			this.nextMenu.open(event.getPlayer());
		} else {
			event.setWillClose(true);
		}

	}
}