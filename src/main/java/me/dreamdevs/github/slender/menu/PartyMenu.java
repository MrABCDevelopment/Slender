package me.dreamdevs.github.slender.menu;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.menu.Menu;
import me.dreamdevs.github.slender.api.menu.MenuItem;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.party.Party;
import me.dreamdevs.github.slender.game.party.PartyRole;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class PartyMenu
{

    public PartyMenu(Player player) {
        Menu menu = new Menu(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-title"), 3);

        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);

        List<String> createLore = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-create-party-lore"));
        List<String> joinRandomLore = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-create-party-lore"));

        if(SlenderMain.getInstance().getPartyManager().isInParty(gamePlayer)) {
            Party party = SlenderMain.getInstance().getPartyManager().getParty(gamePlayer);

            List<String> partyLore = ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-party-info-lore")
                    .replaceAll("%LEADER%", party.getLeader().getPlayer().getName())
                    .replaceAll("%MEMBERS_COUNT%", String.valueOf(party.getMembers().size()))
                    .replaceAll("%STATUS%", party.isOpen() ? SlenderMain.getInstance().getMessagesManager().getMessage("party-opened") : SlenderMain.getInstance().getMessagesManager().getMessage("party-closed")));

            MenuItem partyInfo = new MenuItem().material(Material.BOOK).name(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-party-info-name")).lore(partyLore).build();

            if(party.getLeader().equals(gamePlayer)) {
                MenuItem partyDelete = new MenuItem().material(Material.COAL).name(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-delete-party-name")).lore(ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-delete-party-lore"))).action(event -> {
                    player.closeInventory();
                    party.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-removed"));
                    SlenderMain.getInstance().getPartyManager().getParties().remove(party);
                }).build();
                MenuItem partyChangeStatus = new MenuItem().material(Material.ARROW).name(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-open-party-name")).lore(ColourUtil.colouredLore(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-open-party-lore"))).action(event -> {
                    player.closeInventory();
                    party.setOpen(!party.isOpen());
                    String status = (party.isOpen()) ? SlenderMain.getInstance().getMessagesManager().getMessage("party-opened") : SlenderMain.getInstance().getMessagesManager().getMessage("party-closed");
                    player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-changed-status").replaceAll("%STATUS%", status).replaceAll("%LEADER%", player.getName()));
                }).build();
                menu.setItem(4, partyInfo);
                menu.setItem(12, partyDelete);
                menu.setItem(14, partyChangeStatus);
            } else {
                menu.setItem(13, partyInfo);
            }

        } else {
            MenuItem createParty = new MenuItem().material(Material.ANVIL).name(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-create-party-name")).lore(createLore).action(event -> {
                Party party = new Party();
                party.getMembers().putIfAbsent(gamePlayer, PartyRole.LEADER);
                SlenderMain.getInstance().getPartyManager().getParties().add(party);
                player.closeInventory();
                new PartyMenu(player);
            }).build();

            MenuItem joinRandomParty = new MenuItem().material(Material.SLIME_BALL).name(SlenderMain.getInstance().getMessagesManager().getMessage("party-menu-join-party-name")).lore(joinRandomLore).action(event -> {
                Party party = SlenderMain.getInstance().getPartyManager().getRandomParty();
                player.closeInventory();
                if(party == null) {
                    player.sendMessage(SlenderMain.getInstance().getMessagesManager().getMessage("party-cannot-find-public-party"));
                    return;
                }
                SlenderMain.getInstance().getPartyManager().joinParty(player, party);
            }).build();

            menu.setItem(12, joinRandomParty);
            menu.setItem(14, createParty);
        }

        menu.open(player);
    }
}