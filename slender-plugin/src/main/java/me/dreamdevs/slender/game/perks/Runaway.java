package me.dreamdevs.slender.game.perks;

import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.api.game.perks.Perk;
import me.dreamdevs.slender.api.game.perks.PerkInfo;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@PerkInfo(name = "RUNAWAY", icon = Material.TORCH, role = Role.SURVIVOR)
public class Runaway implements Perk {

	@Override
	public List<String> getLore() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void usePerk(Player player) {
		player.sendMessage("You used it!");
	}
}