package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpectatorSettingsMenu extends ItemMenu {

	public SpectatorSettingsMenu() {
		super(Langauge.MENU_SPECTATOR_SETTINGS_TITLE.toString(), Size.THREE_LINE);

		setItem(11, new NoSpeedItem());
		for(int x = 0; x<4; x++) {
			Material material = null;
			if(x == 0) {
				material = Material.CHAINMAIL_BOOTS;
			} else if(x == 1) {
				material = Material.IRON_BOOTS;
			} else if(x == 2) {
				material = Material.GOLDEN_BOOTS;
			} else {
				material = Material.DIAMOND_BOOTS;
			}
			setItem(12+x, new SpeedMenuItem(Langauge.MENU_SPECTATOR_SETTINGS_SPEED_ITEM_NAME.toString().replace(String.valueOf(x), toRoman(x)), material, x));
		}
	}

	private String toRoman(int speed) {
		String romanNumber = "";
		switch (speed) {
			case 0:
				romanNumber = "I";
				break;
			case 1:
				romanNumber = "II";
				break;
			case 2:
				romanNumber = "III";
				break;
			case 3:
				romanNumber = "IV";
				break;
			default:
				break;
		}
		return romanNumber;
	}

	private static class SpeedMenuItem extends MenuItem {

		private final int speed;

		public SpeedMenuItem(String title, Material material, int speed) {
			super(title, new ItemStack(material));
			this.speed = speed;
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed));
		}
	}

	private static class NoSpeedItem extends MenuItem {

		public NoSpeedItem() {
			super(Langauge.MENU_SPECTATOR_SETTINGS_NO_SPEED_ITEM_NAME.toString(), new ItemStack(Material.LEATHER_BOOTS));
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
		}
	}

}