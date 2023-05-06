package me.dreamdevs.github.slender.database;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.database.IData;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.utils.Util;
import org.bukkit.Bukkit;

public class Database {

    private @Getter IData connector;

    public void connect(String databaseType) {
        Class<? extends IData> database = null;
        Util.sendPluginMessage("&aConnecting to database...");
        try {
            database = Class.forName("me.dreamdevs.github.slender.database.Database" + databaseType).asSubclass(IData.class);
            connector = database.newInstance();
            connector.connectDatabase();
            Util.sendPluginMessage("&aConnected to "+databaseType+" database.");
        } catch (Exception e) {
            Util.sendPluginMessage("&cDatabase type '"+databaseType+"' does not exist!");
        }
    }

    public void disconnect() {
        connector.disconnectDatabase();
        Util.sendPluginMessage("&aDisconnected from the database.");
    }

    public void autoSaveData() {
        for(GamePlayer gamePlayer : SlenderMain.getInstance().getPlayerManager().getPlayers()) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(SlenderMain.getInstance(), () -> saveData(gamePlayer), 0L, 20L * 300);
        }
    }

    public void saveData(GamePlayer gamePlayer) {
        connector.saveAllStatistics(gamePlayer);
    }

    public void loadData(GamePlayer gamePlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(SlenderMain.getInstance(), () -> {
            connector.loadAllStatistics(gamePlayer);
        });
    }

}