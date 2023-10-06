package me.dreamdevs.slender.database;

import lombok.Getter;
import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Config;
import me.dreamdevs.slender.api.database.IDatabase;
import me.dreamdevs.slender.api.utils.Util;
import org.bukkit.Bukkit;

public class Database {

	private @Getter IDatabase database;

	public void connect(String databaseType) {
		Util.sendPluginMessage("&aConnecting to database...");
		try {
			Class<? extends IDatabase> clazz = Class.forName("me.dreamdevs.slender.database.Database" + databaseType).asSubclass(IDatabase.class);
			database = clazz.getConstructor().newInstance();
			database.connect();
			Util.sendPluginMessage("&aConnected to "+databaseType+" database.");
		} catch (Exception e) {
			Util.sendPluginMessage("&cDatabase type '"+databaseType+"' does not exist!");
		}
	}

	public void disconnect() {
		database.disconnect();
		Util.sendPluginMessage("&aDisconnected from the database.");
	}

	public void autoSaveData() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(SlenderMain.getInstance(), this::saveData, 0L, 20L * Config.DATABASE_AUTO_SAVE.toInt());
	}

	public void saveData() {
		Util.sendPluginMessage("&aSaving data...");
		database.saveStatistics();
	}

	public void loadData() {
		Util.sendPluginMessage("&aLoading data...");
		database.loadStatistics();
	}

}