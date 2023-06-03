package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.perks.Perks;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PerksMenu extends Menu
{

    public PerksMenu(Player player) {
        super(SlenderMain.getInstance().getMessagesManager().getMessage("perks-menu-title"), 5);

        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        fill();

        for(int x = 0; x<Perks.getSurvivorPerks().size(); x++) {
            Perks perks = Perks.getSurvivorPerks().get(x);
            setItem(x, new MenuItem().name(perks.getPerkName()).lore(perks.getPerkLore()).material(perks.getPerkIcon()).action(event -> {
                gamePlayer.setEquippedSurvivorPerk(perks);
                player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("perks-selected").replaceAll("%PERK%", perks.getPerkName()).replaceAll("%TEAM%", SlenderMain.getInstance().getMessagesManager().getMessage("team-survivors")));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                player.closeInventory();
            }).build());
        }

        int slot = 18;

        for(int x = 0; x<Perks.getSlenderManPerks().size(); x++) {
            Perks perks = Perks.getSlenderManPerks().get(x);
            setItem(slot, new MenuItem().name(perks.getPerkName()).lore(perks.getPerkLore()).material(perks.getPerkIcon()).action(event -> {
                gamePlayer.setEquippedSlenderManPerk(perks);
                player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("perks-selected").replaceAll("%PERK%", perks.getPerkName()).replaceAll("%TEAM%", SlenderMain.getInstance().getMessagesManager().getMessage("team-slenderman")));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                player.closeInventory();
            }).build());
            slot++;
        }

        setItem(39, new MenuItem().name(gamePlayer.getEquippedSurvivorPerk().getPerkName()).material(gamePlayer.getEquippedSurvivorPerk().getPerkIcon()).lore(gamePlayer.getEquippedSurvivorPerk().getPerkLore()).action(event -> {
            gamePlayer.setEquippedSurvivorPerk(Perks.NONE);
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("perks-selected").replaceAll("%PERK%", Perks.NONE.getPerkName()).replaceAll("%TEAM%", SlenderMain.getInstance().getMessagesManager().getMessage("team-survivors")));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            player.closeInventory();
        }).build());
        setItem(41, new MenuItem().name(gamePlayer.getEquippedSlenderManPerk().getPerkName()).material(gamePlayer.getEquippedSlenderManPerk().getPerkIcon()).lore(gamePlayer.getEquippedSlenderManPerk().getPerkLore()).action(event -> {
            gamePlayer.setEquippedSlenderManPerk(Perks.NONE);
            player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("perks-selected").replaceAll("%PERK%", Perks.NONE.getPerkName()).replaceAll("%TEAM%", SlenderMain.getInstance().getMessagesManager().getMessage("team-slenderman")));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            player.closeInventory();
        }).build());

        open(player);
    }
}