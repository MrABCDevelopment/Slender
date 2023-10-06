package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.Setting;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.database.data.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public class SettingsMenu extends ItemMenu {

	public SettingsMenu(GamePlayer gamePlayer) {
		super(Langauge.MENU_MY_PROFILE_SETTINGS_TITLE.toString(), Size.THREE_LINE);

		setItem(12, new SetSettingItem(gamePlayer, Setting.AUTO_JOIN_MODE,
				Langauge.MENU_MY_PROFILE_SETTINGS_AUTO_JOIN_MODE_ITEM_NAME.toString(),
				Material.APPLE,
				ColourUtil.colouredLore(Langauge.MENU_MY_PROFILE_SETTINGS_AUTO_JOIN_MODE_ITEM_LORE.toString()
						.replace("%STATUS%", ((boolean)gamePlayer.getSetting(Setting.AUTO_JOIN_MODE)) ?
											Langauge.MENU_STATUS_ON.toString() : Langauge.MENU_STATUS_OFF.toString())).toArray(String[]::new)));

		setItem(13, new SetSettingItem(gamePlayer, Setting.SHOW_ARENA_JOIN_MESSAGE,
				Langauge.MENU_MY_PROFILE_SETTINGS_SH0W_ARENA_JOIN_MESSAGE_ITEM_NAME.toString(),
				Material.PAPER,
				ColourUtil.colouredLore(Langauge.MENU_MY_PROFILE_SETTINGS_SHOW_ARENA_JOIN_MESSAGE_ITEM_LORE.toString()
						.replace("%STATUS%", ((boolean)gamePlayer.getSetting(Setting.SHOW_ARENA_JOIN_MESSAGE)) ?
								Langauge.MENU_STATUS_ON.toString() : Langauge.MENU_STATUS_OFF.toString())).toArray(String[]::new)));

		setItem(14, new SetSettingItem(gamePlayer, Setting.MESSAGE_TYPE,
				Langauge.MENU_MY_PROFILE_SETTINGS_MESSAGES_TYPE_ITEM_NAME.toString(),
				Material.SLIME_BALL,
				ColourUtil.colouredLore(Langauge.MENU_MY_PROFILE_SETTINGS_MESSAGES_TYPE_ITEM_LORE.toString()
						.replace("%STATUS%", (String) gamePlayer.getSetting(Setting.MESSAGE_TYPE)))
						.toArray(String[]::new)));

		setItem(26, new BackToMyProfileItem());
	}

	private static class SetSettingItem extends MenuItem {

		private final GamePlayer gamePlayer;
		private final Setting setting;

		public SetSettingItem(GamePlayer gamePlayer, Setting setting, String displayName, Material icon, String... lore) {
			super(displayName, new ItemStack(icon), lore);
			this.gamePlayer = gamePlayer;
			this.setting = setting;
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			gamePlayer.setSetting(setting, !asBoolean(gamePlayer.getSetting(setting)));
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4f, 0.4f);
		}

		private boolean asBoolean(Object object) {
			return (boolean) object;
		}
	}

	private static class BackToMyProfileItem extends MenuItem {

		public BackToMyProfileItem() {
			super(Langauge.MENU_BACK_ITEM_NAME.toString(), new ItemStack(Material.BARRIER));
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> {
				new MyProfileMenu(event.getPlayer()).open(event.getPlayer());
			}, 4L);
		}
	}

}