package me.dreamdevs.slender;

import lombok.Getter;
import me.dreamdevs.slender.api.Config;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.SlenderApi;
import me.dreamdevs.slender.api.utils.Util;
import me.dreamdevs.slender.commands.CommandHandler;
import me.dreamdevs.slender.commands.PartyCommandHandler;
import me.dreamdevs.slender.database.Database;
import me.dreamdevs.slender.game.Lobby;
import me.dreamdevs.slender.listeners.GameListeners;
import me.dreamdevs.slender.listeners.PlayerInteractListener;
import me.dreamdevs.slender.listeners.PlayerListeners;
import me.dreamdevs.slender.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Getter
public class SlenderMain extends JavaPlugin {

	private @Getter static SlenderMain instance;
	private PlayerManager playerManager;
	private LevelManager levelManager;
	private PartyManager partyManager;
	private GameManager gameManager;
	private PerkManager perkManager;

	private Database database;

	private Lobby lobby;

	// Files
	private final File levelsFile = new File(getDataFolder(), "levels.yml");

	@Override
	public void onEnable() {
		instance = this;

		SlenderApi.loadApi(this);

		loadConfig();
		loadLang();

		if (!levelsFile.exists()) {
			saveResource("levels.yml",true);
		}

		this.playerManager = new PlayerManager();

		this.database = new Database();
		this.database.connect(Config.DATABASE_TYPE.toString());
		this.database.loadData();

		this.gameManager = new GameManager();
		this.partyManager = new PartyManager();

		this.lobby = new Lobby();

		this.perkManager = new PerkManager();

		this.levelManager = new LevelManager();

		new CommandHandler(this);

		if(Config.USE_PARTY.toBoolean()) {
			this.partyManager = new PartyManager();
			new PartyCommandHandler(this);
		}

		getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		getServer().getPluginManager().registerEvents(new GameListeners(), this);
	//	getServer().getPluginManager().registerEvents(new PerksListeners(), this);

		this.database.autoSaveData();

		new Metrics(this, 18471);

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> new UpdateChecker(this,109730).getVersion(version -> {
			if (getDescription().getVersion().equals(version)) {
				Util.sendPluginMessage("");
				Util.sendPluginMessage("&aYour version is up to date!");
				Util.sendPluginMessage("&aYour version: " + getDescription().getVersion());
				Util.sendPluginMessage("");
			} else {
				Util.sendPluginMessage("");
				Util.sendPluginMessage("&aThere is new Stop It Slender version!");
				Util.sendPluginMessage("&aYour version: " + getDescription().getVersion());
				Util.sendPluginMessage("&aNew version: " + version);
				Util.sendPluginMessage("");
			}
		}), 10L, 20L * 300);
	}

	@Override
	public void onDisable() {
		Bukkit.getWorlds().forEach(world -> world.getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove));

		this.database.saveData();
		this.database.disconnect();
	}

	private void loadConfig() {
		File config = new File(getDataFolder(), "config.yml");
		Util.createFile(config);

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(config);
		Stream.of(Config.values()).filter(setting -> conf.getString(setting.getPath()) == null)
				.forEach(setting -> conf.set(setting.getPath(), setting.getDefaultValue()));

		Config.setConfiguration(config);
		try {
			conf.save(config);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadLang() {
		File config = new File(getDataFolder(), "langauge.yml");
		Util.createFile(config);

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(config);
		Stream.of(Langauge.values()).filter(setting -> conf.getString(setting.getPath()) == null)
				.forEach(setting -> conf.set(setting.getPath(), setting.getDefaultMessage()));

		Langauge.setConfiguration(config);
		try {
			conf.save(config);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}