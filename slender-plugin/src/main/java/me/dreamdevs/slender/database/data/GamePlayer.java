package me.dreamdevs.slender.database.data;

import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Setting;
import me.dreamdevs.slender.api.Statistic;
import me.dreamdevs.slender.api.database.IGamePlayer;
import me.dreamdevs.slender.api.game.IArena;
import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.api.game.perks.Perk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GamePlayer implements IGamePlayer {

	private final OfflinePlayer player;
	private final Map<Statistic, Integer> statistics;
	private final Map<Setting, Object> settings;

	public GamePlayer(OfflinePlayer player) {
		this.player = player;
		this.statistics = new HashMap<>();
		this.settings = new HashMap<>();
	}

	@Override
	public OfflinePlayer getOfflinePlayer() {
		return player;
	}

	@Override
	public void setStatistic(Statistic statistic, int value) {
		this.statistics.put(statistic, value);
	}

	@Override
	public int getStatistic(Statistic statistic) {
		return statistics.getOrDefault(statistic, 0);
	}

	@Override
	public Object getSetting(Setting setting) {
		return settings.get(setting);
	}

	@Override
	public void setSetting(Setting setting, Object value) {
		this.settings.put(setting, value);
	}

	@Override
	public void setPerk(Role role, Perk perk) {

	}

	@Override
	public Perk getPerk(Role role) {
		return null;
	}

	@Override
	public IArena getArena() {
		return SlenderMain.getInstance().getGameManager().getArenas().stream()
				.filter(arena -> arena.getPlayers().containsKey(getPlayer()))
				.findFirst()
				.orElse(null);
	}

	@Override
	public boolean isInArena() {
		return getArena() != null;
	}

	public Player getPlayer() {
		return player.getPlayer();
	}

	public void clearInventory() {
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setArmorContents(null);
		getPlayer().getInventory().setExtraContents(null);
	}
}