package me.dreamdevs.slender.api;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Config {

	// Game settings
	USE_TERROR_RADIUS("GameSettings.Use-Terror-Radius", true),
	TERROR_RADIUS("GameSettings.Terror-Radius",24),
	USE_PARTY("GameSettings.Use-Party", true),

	// Database settings
	DATABASE_TYPE("Database.Type", "YAML"),
	DATABASE_AUTO_SAVE("Database.Auto-Save", 300),
	DATABASE_HOST("Database.Host","localhost"),
	DATABASE_PORT("Database.Port",3306),
	DATABASE_NAME("Database.Name", "slender"),
	DATABASE_USER("Database.User","root"),
	DATABASE_PASSWORD("Database.Password","password");

	private static YamlConfiguration configuration;
	private @Getter Object defaultValue;
	private @Getter String path;

	Config(String path, Object defaultValue) {
		this.path = path;
		this.defaultValue = defaultValue;
	}

	public static void setConfiguration(File file) {
		configuration = YamlConfiguration.loadConfiguration(file);
	}

	public boolean toBoolean() {
		return configuration.getBoolean(getPath());
	}

	public int toInt() {
		return configuration.getInt(getPath());
	}

	@Override
	public String toString() {
		return configuration.getString(getPath());
	}

}