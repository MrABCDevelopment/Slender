package me.dreamdevs.github.slender;

import lombok.Getter;
import me.dreamdevs.github.slender.commands.CommandHandler;
import me.dreamdevs.github.slender.commands.PartyCommandHandler;
import me.dreamdevs.github.slender.database.Database;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Lobby;
import me.dreamdevs.github.slender.game.perks.Perks;
import me.dreamdevs.github.slender.listeners.*;
import me.dreamdevs.github.slender.managers.*;
import me.dreamdevs.github.slender.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SlenderMain extends JavaPlugin {

    private @Getter static SlenderMain instance;
    private ConfigManager configManager;
    private GameManager gameManager;
    private PlayerManager playerManager;
    private Lobby lobby;
    private MessagesManager messagesManager;
    private Database database;
    private PartyManager partyManager;

    private boolean useLibsDisguises = false;
    private boolean usePerks;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.configManager.loadConfigFiles("items.yml", "messages.yml", "perks.yml");

        this.database = new Database();
        this.database.connect("YAML");

        this.messagesManager = new MessagesManager(this);

        if(getServer().getPluginManager().getPlugin("LibsDisguises") != null) {
            useLibsDisguises = true;
            Util.sendPluginMessage("&aLibsDisguises detected!");
        } else {
            Util.sendPluginMessage("&cLibsDisguises did not detected!");
        }
        usePerks = getConfigManager().getConfig("perks.yml").getBoolean("Enabled");

        this.lobby = new Lobby();
        this.gameManager = new GameManager();
        this.playerManager = new PlayerManager();

        this.partyManager = new PartyManager();

        new CommandHandler(this);
        new PartyCommandHandler(this);

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(), this);
        getServer().getPluginManager().registerEvents(new PerksListeners(), this);

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

        for(GamePlayer gamePlayer : getPlayerManager().getPlayers())
            this.database.saveData(gamePlayer);
        this.database.disconnect();

        gameManager.saveGames();
    }
}