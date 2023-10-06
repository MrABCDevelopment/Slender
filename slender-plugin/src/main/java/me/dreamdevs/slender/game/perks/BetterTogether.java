package me.dreamdevs.slender.game.perks;

import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.api.game.perks.Perk;
import me.dreamdevs.slender.api.game.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@PerkInfo(name = "Better Together", icon = Material.BRICKS, role = Role.SURVIVOR)
public class BetterTogether implements Perk {

	@Override
	public List<String> getLore() {
		return Collections.emptyList();
	}

	@Override
	public void usePerk(Player player) {

	}
}