package me.dreamdevs.github.slender.managers;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class MessagesManager {

    private final Map<String, String> messages;

    public MessagesManager(SlenderMain plugin) {
        messages = new HashMap<>();
        load(plugin);
    }

    public void load(SlenderMain plugin) {
        messages.clear();
        FileConfiguration config = plugin.getConfigManager().getConfig("messages.yml");
        ConfigurationSection section = config.getConfigurationSection("messages");
        section.getKeys(false).forEach(s -> messages.put(s, ColourUtil.colorize(section.getString(s))));
    }

    public String getMessage(String keyMessage) {
        return messages.get(keyMessage);
    }

}