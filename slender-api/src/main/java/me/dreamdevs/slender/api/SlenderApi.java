package me.dreamdevs.slender.api;

import me.dreamdevs.slender.api.inventory.handlers.ItemMenuListener;
import me.dreamdevs.slender.api.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SlenderApi {

	public static JavaPlugin plugin;

	public static boolean isLibsDisguisedEnabled;

	public static void loadApi(JavaPlugin plugin) {
		SlenderApi.plugin = plugin;

		Util.sendPluginMessage("&aLoading Stop It Slender API...");

		ItemMenuListener.getInstance().register(plugin);

		isLibsDisguisedEnabled = Bukkit.getPluginManager().getPlugin("LibsDisguises") != null;
		if (isLibsDisguisedEnabled) {
			Util.sendPluginMessage("&aLibsDisguises detected!");
		} else {
			Util.sendPluginMessage("&cLibsDisguises did not detected!");
		}

		Util.sendPluginMessage("&aSuccessfully registered Stop It Slender API!");
	}

}