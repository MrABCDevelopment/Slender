package me.dreamdevs.slender.api.database;

public interface IDatabase {

	void connect();

	void disconnect();

	void saveStatistics();

	void loadStatistics();

}