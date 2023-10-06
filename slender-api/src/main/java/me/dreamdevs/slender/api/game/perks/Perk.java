package me.dreamdevs.slender.api.game.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public interface Perk extends Listener {

	List<String> getLore();

	void usePerk(Player player);

}