package me.dreamdevs.slender.game;

import lombok.Getter;
import me.dreamdevs.slender.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum Perks {

    NONE(Material.BARRIER, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("No-Perk"), new ArrayList<>()),

    /**
     * SlenderMan's perks
     */

    SLENDERMAN_PERK_KILLER_INSTINCT(Material.GLOWSTONE, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Slender-Perks.Killer-Instinct.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Slender-Perks.Killer-Instinct.PerkLore")),
    SLENDERMAN_PERK_ENDLESS_AGONY(Material.SKELETON_SKULL, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Slender-Perks.Endless-Agony.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Slender-Perks.Endless-Agony.PerkLore")),
    SLENDERMAN_PERK_DARK_ABYSS(Material.VINE, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Slender-Perks.Dark-Abyss.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Slender-Perks.Dark-Abyss.PerkLore")),
    SLENDERMAN_PERK_FROM_THE_DARK(Material.COAL, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Slender-Perks.From-The-Dark.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Slender-Perks.From-The-Dark.PerkLore")),
    SLENDERMAN_PERK_PAGES_BELONGINGS(Material.PAPER, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Slender-Perks.Pages-Belongings.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Slender-Perks.Pages-Belongings.PerkLore")),
    /**
     * Survivor's perks
     */

    SURVIVOR_PERK_RUNAWAY(Material.TORCH, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Survivor-Perks.Runaway.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Survivor-Perks.Runaway.PerkLore")),
    SURVIVOR_PERK_BETTER_TOGETHER(Material.BRICKS, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Survivor-Perks.Better-Together.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Survivor-Perks.Better-Together.PerkLore")),
    SURVIVOR_PERK_ARCHAEOLOGIST(Material.PAPER, SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getString("Survivor-Perks.Archaeologist.PerkName"),
            SlenderMain.getInstance().getConfigManager().getConfig("perks.yml").getStringList("Survivor-Perks.Archaeologist.PerkLore"));

    private final Material perkIcon;
    private final String perkName;
    private final List<String> perkLore;

    Perks(Material perkIcon, String perkName, List<String> perkLore) {
        this.perkIcon = perkIcon;
        this.perkName = ColourUtil.colorize(perkName);
        this.perkLore = ColourUtil.colouredLore(perkLore);
        this.perkLore.replaceAll(s -> s.replaceAll("%PERK_NAME%", perkName));
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(getPerkIcon());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(getPerkName());
        itemMeta.setLore(getPerkLore());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static List<Perks> getSlenderManPerks() {
        List<Perks> slenderPerks = new ArrayList<>();
        for(Perks perks : values()) {
            if(perks.name().contains("SLENDERMAN_PERK")) {
                slenderPerks.add(perks);
            }
        }
        return slenderPerks;
    }

    public static List<Perks> getSurvivorPerks() {
        List<Perks> survivorPerks = new ArrayList<>();
        for(Perks perks : values()) {
            if(perks.name().contains("SURVIVOR_PERK")) {
                survivorPerks.add(perks);
            }
        }
        return survivorPerks;
    }

}