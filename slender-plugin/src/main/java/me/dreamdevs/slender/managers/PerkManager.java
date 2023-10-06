package me.dreamdevs.slender.managers;

import lombok.Getter;
import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.api.game.perks.Perk;
import me.dreamdevs.slender.api.game.perks.PerkInfo;
import me.dreamdevs.slender.api.utils.Util;
import me.dreamdevs.slender.game.perks.BetterTogether;
import me.dreamdevs.slender.game.perks.Runaway;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PerkManager {

	private @Getter final List<Perk> perks;

	public PerkManager() {
		this.perks = new LinkedList<>();
		registerPerk(Runaway.class);
		//registerPerk(Archaeologist.class);
		//registerPerk(BetterTogether.class);
	}

	public void registerPerk(Class<? extends Perk> perkClass) {
		try {
			PerkInfo info = perkClass.getAnnotation(PerkInfo.class);
			Perk perk = perkClass.getConstructor().newInstance();
			this.perks.add(perk);
			Util.sendPluginMessage("&aRegistered perk "+info.name());
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Perk> getPerksByRole(Role role) {
		return perks.stream().filter(perk -> perk.getClass().getAnnotation(PerkInfo.class).role().equals(role))
				.collect(Collectors.toList());
	}

	public Perk getPerk(String name) {
		return perks.stream().filter(perk -> perk.getClass().getAnnotation(PerkInfo.class).name().equalsIgnoreCase(name))
				.findFirst().orElse(null);
	}

}