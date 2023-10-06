package me.dreamdevs.slender.game;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.api.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@Getter @Setter
public class Lobby {

    private Location lobbyLocation;
    private File lobbyFile;
    private FileConfiguration configuration;

    public Lobby() {
        lobbyFile = new File(SlenderMain.getInstance().getDataFolder(), "lobby.yml");
        Util.createFile(lobbyFile);
        configuration = YamlConfiguration.loadConfiguration(lobbyFile);
        if(configuration.isConfigurationSection("lobby")) {
            lobbyLocation = Util.getStringLocation(configuration.getConfigurationSection("lobby").getString("location"), true);
        }
    }

    public void teleportPlayerToLobby(Player player) {
        if(lobbyLocation != null)
            player.teleport(lobbyLocation);
        else
            player.sendMessage(ColourUtil.colorize("&cLobby is not set!"));
    }

    public void saveLobby(Player player) {
        lobbyLocation = player.getLocation().clone();
        try {
            configuration.set("lobby.location", Util.getLocationString(player.getLocation(), true));
            configuration.save(lobbyFile);
        } catch (IOException e) {
            Util.sendPluginMessage("&cSomething went wrong, could not save lobby location!");
        }
    }

}