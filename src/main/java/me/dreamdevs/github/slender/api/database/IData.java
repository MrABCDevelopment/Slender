package me.dreamdevs.github.slender.api.database;

import me.dreamdevs.github.slender.game.GamePlayer;

public interface IData {

    void connectDatabase();

    void disconnectDatabase();

    void saveAllStatistics(GamePlayer gamePlayer);

    void loadAllStatistics(GamePlayer gamePlayer);

}