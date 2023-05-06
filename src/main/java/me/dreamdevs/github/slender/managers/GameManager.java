package me.dreamdevs.github.slender.managers;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.events.SlenderJoinArenaEvent;
import me.dreamdevs.github.slender.api.events.SlenderQuitArenaEvent;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.ArenaState;
import me.dreamdevs.github.slender.game.GamePlayer;
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
        if(arena.getArenaState() != ArenaState.WAITING || arena.getArenaState() != ArenaState.STARTING) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("game-running"));
            return;
        }
        if(arena.getPlayers().size() >= arena.getMaxPlayers()) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("game-full"));
            return;
        }
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
        if(gamePlayer.isInArena()) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("player-ingame"));
            return;
        }
        player.teleport(arena.getSlenderSpawnLocation());
        gamePlayer.clearInventory();
        player.getInventory().setItem(8, CustomItem.LEAVE.toItemStack());
        arena.getPlayers().put(player, null);
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            player.hidePlayer(SlenderMain.getInstance(), onlinePlayer);
            onlinePlayer.hidePlayer(SlenderMain.getInstance(), player);
        });

        arena.getPlayers().keySet().forEach(playerGame -> {
            player.showPlayer(SlenderMain.getInstance(), playerGame);
            playerGame.showPlayer(SlenderMain.getInstance(), player);
        });

        SlenderJoinArenaEvent slenderJoinArenaEvent = new SlenderJoinArenaEvent(gamePlayer, arena);
        Bukkit.getPluginManager().callEvent(slenderJoinArenaEvent);

        if(arena.getPlayers().size() >= arena.getMinPlayers()) {
            arena.setArenaState(ArenaState.STARTING);
            arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-starting-info"));
            arena.setTimer(30);
        }
    }

    public void leaveGame(Player player, Arena arena) {
        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
        if(!gamePlayer.isInArena()) {
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("not-ingame"));
            return;
        }
        SlenderMain.getInstance().getPlayerManager().sendToLobby(player);
        SlenderMain.getInstance().getPlayerManager().loadLobby(player);
        arena.getPlayers().remove(player);

        SlenderQuitArenaEvent slenderQuitArenaEvent = new SlenderQuitArenaEvent(gamePlayer, arena);
        Bukkit.getPluginManager().callEvent(slenderQuitArenaEvent);

        if(arena.getPlayers().size() < arena.getMinPlayers()) {
            arena.setArenaState(ArenaState.WAITING);
            arena.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("arena-stop-starting"));
            arena.setTimer(0);
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
        try {
            configuration.save(f);
        } catch (IOException e) { }
    }

    public boolean isInArena(Player player) {
        return arenas.stream().filter(arena -> arena.getPlayers().containsKey(player)).findFirst().orElse(null) != null;
    }

    public void saveGames() {
        Optional.ofNullable(arenas).ifPresent(games1 -> games1.forEach(this::saveGame));
    }

    public void forceRemovePlayer(Player player) {
        arenas.stream().filter(arena -> arena.getPlayers().containsKey(player)).forEach(arena -> arena.getPlayers().remove(player));
        Util.sendPluginMessage("&cPlayer "+player.getName()+" was removed from the game, because he left.");
    }

}