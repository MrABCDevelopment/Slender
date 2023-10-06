package me.dreamdevs.slender.managers;

import lombok.Getter;
import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.Statistic;
import me.dreamdevs.slender.api.events.SlenderPlayerExpGainEvent;
import me.dreamdevs.slender.api.events.SlenderPlayerLevelUpEvent;
import me.dreamdevs.slender.api.utils.Util;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.dreamdevs.slender.game.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
public class LevelManager {

    private final Map<Integer, Level> levels;
    private YamlConfiguration config;

    public LevelManager() {
        this.levels = new HashMap<>();
        load(SlenderMain.getInstance());
    }

    public void load(SlenderMain plugin) {
        this.levels.clear();
        config = YamlConfiguration.loadConfiguration(plugin.getLevelsFile());

        ConfigurationSection section = config.getConfigurationSection("Levels");

        for(String string : section.getKeys(false)) {
            Level level = new Level(section.getInt(string+".RequireExp"));
            levels.put(Integer.parseInt(string), level);
        }

        Util.sendPluginMessage("&aRegistered "+levels.size()+" levels!");
    }

    public void addExp(GamePlayer gamePlayer, int exp) {
        gamePlayer.setStatistic(Statistic.EXP, gamePlayer.getStatistic(Statistic.EXP)+exp);
        gamePlayer.getPlayer().sendMessage(Langauge.LEVEL_PLAYER_EXP_REWARD.toString().replace("%AMOUNT%", String.valueOf(exp)));
        SlenderPlayerExpGainEvent slenderPlayerExpGainEvent = new SlenderPlayerExpGainEvent(gamePlayer, exp);
        Bukkit.getServer().getPluginManager().callEvent(slenderPlayerExpGainEvent);
        if(gamePlayer.getStatistic(Statistic.EXP) >= gamePlayer.getStatistic(Statistic.LEVEL)*50) {
            int newLevel = gamePlayer.getStatistic(Statistic.LEVEL)+1;
            gamePlayer.setStatistic(Statistic.LEVEL, newLevel);
            gamePlayer.getPlayer().sendMessage(Langauge.LEVEL_PLAYER_LEVEL_UP.toString().replace("%LEVEL%", String.valueOf(newLevel)));
            SlenderPlayerLevelUpEvent slenderPlayerLevelUpEvent = new SlenderPlayerLevelUpEvent(gamePlayer, newLevel);
            Bukkit.getServer().getPluginManager().callEvent(slenderPlayerLevelUpEvent);
        }
    }

}