package me.dreamdevs.github.slender.database;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.database.IData;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.perks.Perks;
import me.dreamdevs.github.slender.utils.Util;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DatabaseYAML implements IData {

    private File dataDirectory;

    @Override
    public void connectDatabase() {
        dataDirectory = new File(SlenderMain.getInstance().getDataFolder(), "users");
        if (!dataDirectory.exists() || !dataDirectory.isDirectory()) dataDirectory.mkdirs();
    }

    @Override
    public void disconnectDatabase() {
        // Nothing to do with this type of database
    }

    @Override
    public void saveAllStatistics(GamePlayer gamePlayer) {
        File playerFile = new File(dataDirectory, gamePlayer.getPlayer().getUniqueId()+".yml");
        Util.createFile(playerFile);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        playerData.set("PlayerInfo.UUID", gamePlayer.getPlayer().getUniqueId().toString());
        playerData.set("PlayerInfo.Nick", gamePlayer.getPlayer().getName());
        playerData.set("Statistics.Wins", gamePlayer.getWins());
        playerData.set("Statistics.CollectedPages", gamePlayer.getCollectedPages());
        playerData.set("Statistics.Level", gamePlayer.getLevel());
        playerData.set("Statistics.Exp", gamePlayer.getExp());
        playerData.set("Statistics.KilledSurvivors", gamePlayer.getKilledSurvivors());
        playerData.set("Statistics.KilledSlenderMen", gamePlayer.getKilledSlenderMen());
        playerData.set("PlayerSettings.AutoArenaJoin", gamePlayer.isAutoJoinMode());
        playerData.set("PlayerSettings.ShowJoinArenaMessage", gamePlayer.isShowArenaJoinMessage());
        playerData.set("PlayerSettings.MessagesType", gamePlayer.getMessagesType());
        playerData.set("PlayerPerks.EquippedSurvivorPerk", gamePlayer.getEquippedSurvivorPerk().name());
        playerData.set("PlayerPerks.EquippedSlenderManPerk", gamePlayer.getEquippedSlenderManPerk().name());
        try {
            playerData.save(playerFile);
        } catch (Exception e) {}
    }

    @Override
    public void loadAllStatistics(GamePlayer gamePlayer) {
        File playerFile = new File(dataDirectory, gamePlayer.getPlayer().getUniqueId()+".yml");
        Util.createFile(playerFile);
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        gamePlayer.setWins(playerData.getInt("Statistics.Wins", 0));
        gamePlayer.setLevel(playerData.getInt("Statistics.Level", 0));
        gamePlayer.setCollectedPages(playerData.getInt("Statistics.CollectedPages", 0));
        gamePlayer.setExp(playerData.getInt("Statistics.Exp", 0));
        gamePlayer.setKilledSlenderMen(playerData.getInt("Statistics.KilledSlenderMen", 0));
        gamePlayer.setKilledSurvivors(playerData.getInt("Statistics.KilledSurvivors", 0));

        gamePlayer.setAutoJoinMode(playerData.getBoolean("PlayerSettings.AutoArenaJoin", false));
        gamePlayer.setShowArenaJoinMessage(playerData.getBoolean("PlayerSettings.ShowJoinArenaMessage", true));
        gamePlayer.setMessagesType(playerData.getString("PlayerSettings.MessagesType", "all"));

        gamePlayer.setEquippedSurvivorPerk(Perks.valueOf(playerData.getString("PlayerPerks.EquippedSurvivorPerk", "NONE")));
        gamePlayer.setEquippedSlenderManPerk(Perks.valueOf(playerData.getString("PlayerPerks.EquippedSlenderManPerk", "NONE")));
    }
}