package me.dreamdevs.github.slender.utils;

import lombok.Getter;
import me.dreamdevs.github.slender.SlenderMain;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CustomItem {

    ARENA_SELECTOR(Material.CHEST, SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getString("items.arena-selector.DisplayName"),
            SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getStringList("items.arena-selector.Lore")),
    LEAVE(Material.RED_BED, SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getString("items.leave.DisplayName"),
            SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getStringList("items.leave.Lore")),
    MY_PROFILE(Material.PLAYER_HEAD, SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getString("items.profile.DisplayName"),
            SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getStringList("items.profile.Lore")),
    PLAY_AGAIN(Material.PAPER, SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getString("items.play-again.DisplayName"),
            SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getStringList("items.play-again.Lore")),
    SPECTATOR_TOOL(Material.STICK, "&bSpectator Menu", new ArrayList<>()),

    // SlenderMan's items
    SLENDERMAN_WEAPON(Material.IRON_SWORD, "&cSlenderMan's Sword", new ArrayList<>());

    private String displayName;
    private Material material;
    private List<String> lore;

    CustomItem(Material material, String displayName, List<String> lore) {
        this.material = material;
        this.displayName = ColourUtil.colorize(displayName);
        this.lore = ColourUtil.colouredLore(lore);
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}