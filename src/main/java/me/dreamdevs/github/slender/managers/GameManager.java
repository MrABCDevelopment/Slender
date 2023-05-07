package me.dreamdevs.github.slender.managers;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.events.SlenderJoinArenaEvent;
import me.dreamdevs.github.slender.api.events.SlenderQuitArenaEvent;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.ArenaState;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Role;
import me.dreamdevs.github.slender.utils.CustomItem;
import me.dreamdevs.github.slender.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class GameManager {

    private final List<Arena> arenas;

    public GameManager() {
        this.arenas = new ArrayList<>();
        File file = new File(SlenderMain.getInstance().getDataFolder(), "arenas");
        if(!file.exists() || !file.isDirectory())
            file.mkdirs();

        Optional.ofNullable(file.listFiles(((dir, name) -> name.endsWith(".yml")))).ifPresent(files -> Arrays.asList(files).forEach(this::loadGame));
    }

    public void joinGame(Player player, Arena arena) {
        if(arena.getArenaState() == ArenaState.WAITING || arena.getArenaState() == ArenaState.STARTING) {
            if(arena.getPlayers().size() >= arena.getMaxPlayers()) {
                player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-no-slots"));
                return;
            }
            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
            if(gamePlayer.isInArena()) {
                player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-player-in-game"));
                return;
            }

            player.teleport(arena.getSlenderSpawnLocation());
            gamePlayer.clearInventory();
            player.setScoreboard(arena.getScoreboard());
            player.getInventory().setItem(8, CustomItem.LEAVE.toItemStack());
            arena.getPlayers().put(player, Role.NONE);
            arena.getBossBar().addPlayer(player);
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                player.hidePlayer(SlenderMain.getInstance(), onlinePlayer);
                onlinePlayer.hidePlayer(SlenderMain.getInstance(), player);
            });

            arena.getPlayers().keySet().forEach(playerGame -> {
                player.showPlayer(SlenderMain.getInstance(), playerGame);
                playerGame.showPlayer(SlenderMain.getInstance(), player);
            });

            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-join-game-info"));

            SlenderJoinArenaEvent slenderJoinArenaEvent = new SlenderJoinArenaEvent(gamePlayer, arena);
            Bukkit.getPluginManager().callEvent(slenderJoinArenaEvent);

            if(arena.getPlayers().size() >= arena.getMinPlayers()) {
                arena.setArenaState(ArenaState.STARTING);
                arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-starting-info"));
                arena.setTimer(30);
            }
        } else {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-still-running"));
        }
    }

    public void leaveGame(Player player, Arena arena) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        if(!forceRemovePlayerFromGame(gamePlayer)) {
            gamePlayer.getPlayer().sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("not-ingame"));
            return;
        }

        SlenderQuitArenaEvent slenderQuitArenaEvent = new SlenderQuitArenaEvent(gamePlayer, arena);
        Bukkit.getPluginManager().callEvent(slenderQuitArenaEvent);

        if(arena.getPlayers().size() < arena.getMinPlayers()) {
            if(arena.getArenaState() == ArenaState.STARTING) {
                arena.setArenaState(ArenaState.WAITING);
                arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-stop-starting"));
                arena.setTimer(0);
            }
        }
    }

    public void loadGame(File file) {
        Util.createFile(file);
        String fileName = file.getName();
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        fileName = fileName.substring(0, fileName.length()-4);
        Arena arena = new Arena(fileName);
        arena.setFile(file);
        arena.setMinPlayers(configuration.getInt("GameSettings.MinPlayers"));
        arena.setMaxPlayers(configuration.getInt("GameSettings.MaxPlayers"));
        arena.setGameTime(configuration.getInt("GameSettings.Time"));
        arena.setSlenderSpawnLocation(Util.getStringLocation(configuration.getString("GameSettings.SlenderStartLocation"), true));
        List<Location> locations = new ArrayList<>();
        for(String s : configuration.getStringList("GameSettings.SurvivorsLocations"))
            locations.add(Util.getStringLocation(s, true));
        arena.setSurvivorsLocations(locations);
        List<Location> locations1 = new ArrayList<>();
        for(String s : configuration.getStringList("GameSettings.PagesLocations"))
            locations1.add(Util.getStringLocation(s, true));
        arena.setPagesLocations(locations1);
        arena.startGame();
        arenas.add(arena);
    }

    public void saveGame(Arena arena) {
        String fileName = arena.getId()+".yml";
        File f = new File(SlenderMain.getInstance().getDataFolder(), "arenas/"+fileName);
        Util.createFile(f);

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(f);
        configuration.set("GameSettings.Time", arena.getGameTime());
        configuration.set("GameSettings.MinPlayers", arena.getMinPlayers());
        configuration.set("GameSettings.MaxPlayers", arena.getMaxPlayers());
        configuration.set("GameSettings.SlenderStartLocation", Util.getLocationString(arena.getSlenderSpawnLocation(), true));
        List<String> locations = new ArrayList<>();
        for(Location location : arena.getSurvivorsLocations()) {
            String line = Util.getLocationString(location, true);
            locations.add(line);
        }
        configuration.set("GameSettings.SurvivorsLocations", locations);
        List<String> locations1 = new ArrayList<>();
        for(Location location : arena.getPagesLocations()) {
            String line = Util.getLocationString(location, true);
            locations1.add(line);
        }
        configuration.set("GameSettings.PagesLocations", locations1);
        try {
            configuration.save(f);
        } catch (IOException e) { }
    }

    public boolean isInArena(Player player) {
        return arenas.stream().filter(arena -> arena.getPlayers().containsKey(player)).findFirst().orElse(null) != null;
    }

    public Arena getArena(String id) {
        return arenas.stream().filter(arena -> arena.getId().equalsIgnoreCase(id)).findAny().orElse(null);
    }

    public void saveGames() {
        Optional.ofNullable(arenas).ifPresent(games1 -> games1.forEach(this::saveGame));
    }

    public boolean forceRemovePlayerFromGame(GamePlayer gamePlayer) {
        if(!gamePlayer.isInArena()) {
            Util.sendPluginMessage("&cCould not remove player from the game!");
            return false;
        }
        Arena arena = gamePlayer.getArena();
        SlenderMain.getInstance().getPlayerManager().sendToLobby(gamePlayer.getPlayer());
        SlenderMain.getInstance().getPlayerManager().loadLobby(gamePlayer.getPlayer());
        arena.getPlayers().remove(gamePlayer.getPlayer());
        arena.getBossBar().removePlayer(gamePlayer.getPlayer());
        if(arena.getPlayers().size() == 0 && arena.getArenaState() == ArenaState.RUNNING) {
            arena.endGame();
        }
        return true;
    }

}