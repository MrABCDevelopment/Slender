package me.dreamdevs.slender.menus;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.events.ItemClickEvent;
import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.api.game.perks.Perk;
import me.dreamdevs.slender.api.game.perks.PerkInfo;
import me.dreamdevs.slender.api.inventory.BookItemMenu;
import me.dreamdevs.slender.api.inventory.ItemMenu;
import me.dreamdevs.slender.api.inventory.buttons.MenuItem;
import me.dreamdevs.slender.database.data.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class PerkMenu extends ItemMenu {

	public PerkMenu() {
		super(Langauge.MENU_PERKS_TITLE.toString(), Size.THREE_LINE);

		setItem(12, new OpenPerkSelector(Role.SURVIVOR));
		setItem(14, new OpenPerkSelector(Role.SLENDER));
	}

	private static class OpenPerkSelector extends MenuItem {

		private final Role role;

		public OpenPerkSelector(Role role) {
			super((role == Role.SURVIVOR) ? Langauge.MENU_PERKS_OPEN_SURVIVOR_PERKS.toString()
					: Langauge.MENU_PERKS_OPEN_SLENDERMAN_PERKS.toString(), new ItemStack(
					(role == Role.SURVIVOR) ? Material.PLAYER_HEAD : Material.IRON_SWORD));
			this.role = role;
		}

		@Override
		public void onItemClick(ItemClickEvent event) {
			event.setWillClose(true);
			Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> {
				new PerkSelectorMenu(role).open(event.getPlayer());
			}, 4L);
		}
	}

	private static class PerkSelectorMenu extends BookItemMenu {

		public PerkSelectorMenu(Role role) {
			super(Langauge.MENU_PERKS_TITLE.toString(), buildItems(role), false, true);
		}

		private static List<MenuItem> buildItems(Role role) {
			return SlenderMain.getInstance().getPerkManager().getPerksByRole(role).stream()
					.map(perk -> new SelectPerk(role, perk))
					.collect(Collectors.toList());
		}

		private static class SelectPerk extends MenuItem {

			private final Perk perk;
			private final Role role;

			public SelectPerk(Role role, Perk perk) {
				super(perk.getClass().getAnnotation(PerkInfo.class).name(),
						new ItemStack(perk.getClass().getAnnotation(PerkInfo.class).icon()),
						perk.getLore().toArray(String[]::new));
				this.perk = perk;
				this.role = role;
			}

			@Override
			public void onItemClick(ItemClickEvent event) {
				event.setWillClose(true);
				GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getPlayer());
				gamePlayer.setPerk(role, perk);
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
				event.getPlayer().sendMessage(Langauge.PERKS_SELECTED.toString().replace("%PERK_NAME%", perk.getClass().getAnnotation(PerkInfo.class).name())
						.replace("%TEAM%", (role == Role.SURVIVOR) ? Langauge.ARENA_SURVIVOR_TEAM.toString() : Langauge.ARENA_SLENDERMAN_TEAM.toString()));
			}
		}
	}

}