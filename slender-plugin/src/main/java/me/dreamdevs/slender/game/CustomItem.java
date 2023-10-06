package me.dreamdevs.slender.game;

import lombok.Getter;
import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.Langauge;
import me.dreamdevs.slender.api.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public enum CustomItem {

    ARENA_SELECTOR(Material.CHEST, Langauge.ITEMS_ARENA_SELECTOR_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_ARENA_SELECTOR_DISPLAY_LORE.toString())),

    LEAVE(Material.RED_BED, Langauge.ITEMS_LEAVE_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_ARENA_SELECTOR_DISPLAY_LORE.toString())),

    MY_PROFILE(Material.PLAYER_HEAD, Langauge.ITEMS_MY_PROFILE_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_MY_PROFILE_DISPLAY_LORE.toString())),

    PLAY_AGAIN(Material.PAPER, Langauge.ITEMS_PLAY_AGAIN_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_MY_PROFILE_DISPLAY_LORE.toString())),

    PARTY_MENU(Material.SLIME_BALL, Langauge.ITEMS_PARTY_MENU_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_PARTY_MENU_DISPLAY_LORE.toString())),

    PERKS(Material.FEATHER, Langauge.ITEMS_PERKS_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_PERKS_DISPLAY_LORE.toString())),

    //SHOP(Material.EMERALD, Langauge.,
    //        SlenderMain.getInstance().getConfigManager().getConfig("items.yml").getStringList("items.shop.Lore")),

    SPECTATOR_SETTINGS(Material.STICK, Langauge.ITEMS_SPECTATOR_SETTINGS_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_SPECTATOR_SETTINGS_DISPLAY_LORE.toString())),

    SPECTATOR_TELEPORTER(Material.COMPASS, Langauge.ITEMS_SPECTATOR_TELEPORT_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_SPECTATOR_TELEPORT_DISPLAY_LORE.toString())),

    SURVIVOR_WEAPON(Material.WOODEN_SWORD, Langauge.ITEMS_SURVIVOR_SWORD_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_SURVIVOR_SWORD_DISPLAY_LORE.toString())),

    // SlenderMan's items
    SLENDERMAN_WEAPON(Material.IRON_SWORD, Langauge.ITEMS_SLENDERMAN_SWORD_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_SLENDERMAN_SWORD_DISPLAY_LORE.toString())),

    SLENDERMAN_COMPASS(Material.COMPASS, Langauge.ITEMS_SLENDERMAN_COMPASS_DISPLAY_NAME.toString(),
            ColourUtil.colouredLore(Langauge.ITEMS_SLENDERMAN_COMPASS_DISPLAY_LORE.toString()));

    private final String displayName;
    private final Material material;
    private final List<String> lore;

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