package me.dreamdevs.github.slender;

import lombok.Getter;
import me.dreamdevs.github.slender.commands.CommandHandler;
import me.dreamdevs.github.slender.database.Database;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Lobby;
import me.dreamdevs.github.slender.listeners.GameListeners;
import me.dreamdevs.github.slender.listeners.InventoryListener;
import me.dreamdevs.github.slender.listeners.PlayerInteractListener;
import me.dreamdevs.github.slender.listeners.PlayerListeners;
import me.dreamdevs.github.slender.managers.ConfigManager;
import me.dreamdevs.github.slender.managers.GameManager;
import me.dreamdevs.github.slender.managers.MessagesManager;
import me.dreamdevs.github.slender.managers.PlayerManager;
import org.bukkit.Bukkit;
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

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.configManager.loadConfigFiles("items.yml", "messages.yml");

        this.database = new Database();
        this.database.connect("YAML");

        this.messagesManager = new MessagesManager(this);

        this.lobby = new Lobby();
        this.gameManager = new GameManager();
        this.playerManager = new PlayerManager();

        new CommandHandler(this);

        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream().filter(entity -> entity instanceof Item).forEach(entity -> entity.remove()));

        for(GamePlayer gamePlayer : getPlayerManager().getPlayers())
            this.database.saveData(gamePlayer);
        this.database.disconnect();
    }
}