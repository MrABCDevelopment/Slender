package me.dreamdevs.github.slender.listeners;

import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.events.*;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import me.dreamdevs.github.slender.game.Role;
import me.dreamdevs.github.slender.game.perks.Perks;
import me.dreamdevs.github.slender.utils.ColourUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class PerksListeners implements Listener
{

    /**
     * SlenderMan perks
     */

    private final Map<UUID, Integer> tokens = new HashMap<>();
    private final Map<UUID, Boolean> darkAbyss = new HashMap<>();
    private final Map<UUID, List<Player>> betterTogether = new HashMap<>();

    @EventHandler
    public void startArena(SlenderGameStartEvent event) {
        GamePlayer slender = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getArena().getSlenderMan());
        if(slender.getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_ENDLESS_AGONY))
            tokens.putIfAbsent(slender.getPlayer().getUniqueId(), 0);
        if(slender.getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_DARK_ABYSS))
            darkAbyss.putIfAbsent(slender.getPlayer().getUniqueId(), false);
    }

    @EventHandler
    public void endArena(SlenderGameEndEvent event) {
        tokens.remove(event.getArena().getSlenderMan().getUniqueId());
    }

    @EventHandler
    public void damageEvent(SlenderDamageSurvivorEvent event) {
        if (event.getSlenderMan().getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_KILLER_INSTINCT)) {
            event.getSurvivor().getPlayer().setGlowing(true);
            Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> event.getSurvivor().getPlayer().setGlowing(false), 20*100);
        }
        if(event.getSlenderMan().getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_ENDLESS_AGONY)) {
            event.setDamage(event.getDamage()+(0.5*tokens.get(event.getSlenderMan().getPlayer().getUniqueId())));
        }
        if(event.getSlenderMan().getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_FROM_THE_DARK)) {
            for(Entity entity : event.getSurvivor().getPlayer().getWorld().getNearbyEntities(event.getSurvivor().getPlayer().getLocation(), 5, 5, 5)) {
                if(entity instanceof Player) {
                    Player player = (Player) entity;
                    if(event.getArena().getPlayers().get(player).equals(Role.SURVIVOR)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*10, 0));
                    }
                }
            }
        }
    }

    @EventHandler
    public void deathEvent(SlenderKillSurvivorEvent event) {
        if(event.getSlenderMan().getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_ENDLESS_AGONY)) {
            GamePlayer slender = event.getSlenderMan();
            if(tokens.get(slender.getPlayer().getUniqueId()) < 3) {
                tokens.put(slender.getPlayer().getUniqueId(), tokens.get(slender.getPlayer().getUniqueId())+1);
                slender.getPlayer().sendMessage(ColourUtil.colorize("&cYour power is growing up... &b(+1 token)"));
            }
        }
        if(event.getSlenderMan().getEquippedSlenderManPerk().equals(Perks.SLENDERMAN_PERK_DARK_ABYSS)) {
            GamePlayer slender = event.getSlenderMan();
            if(darkAbyss.containsKey(slender.getPlayer().getUniqueId()) && !darkAbyss.get(slender.getPlayer().getUniqueId())) {
                darkAbyss.put(slender.getPlayer().getUniqueId(), true);
            } else if(darkAbyss.containsKey(slender.getPlayer().getUniqueId()) && darkAbyss.get(slender.getPlayer().getUniqueId()))
                darkAbyss.remove(slender.getPlayer().getUniqueId());
        }
    }

    /**
     * Survivors
     */

    @EventHandler(priority = EventPriority.MONITOR)
    public void interactPlayer(PlayerInteractEvent event) {
        if(event.getItem() == null)
            return;
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();
            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(event.getPlayer());
            if(event.getItem().getType() == Material.TORCH) {
                if(!gamePlayer.isInArena())
                    return;
                Arena arena = gamePlayer.getArena();
                if(arena.getPlayers().get(gamePlayer.getPlayer()) != Role.SURVIVOR)
                    return;

                GamePlayer slender = SlenderMain.getInstance().getPlayerManager().getPlayer(arena.getSlenderMan());
                if(darkAbyss.containsKey(slender.getPlayer().getUniqueId()) && darkAbyss.get(slender.getPlayer().getUniqueId())) {
                    gamePlayer.getPlayer().sendMessage(ColourUtil.colorize("&cYou cannot use your torch while Dark Abyss is active!"));
                    return;
                }

                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
                gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 40, 0));
                ItemStack itemStack1 = gamePlayer.getPlayer().getInventory().getItemInMainHand();
                gamePlayer.getPlayer().getInventory().getItemInMainHand().setAmount(itemStack1.getAmount()-1);
                if(gamePlayer.getEquippedSurvivorPerk().equals(Perks.SURVIVOR_PERK_RUNAWAY))
                    gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*2, 0));
                if(gamePlayer.getEquippedSurvivorPerk().equals(Perks.SURVIVOR_PERK_BETTER_TOGETHER)) {
                    List<Player> players = new ArrayList<>();
                    for(Entity entity : gamePlayer.getPlayer().getWorld().getNearbyEntities(gamePlayer.getPlayer().getLocation(), 4, 4, 4)) {
                        if(entity instanceof Player) {
                            Player player = (Player) entity;
                            if(arena.getPlayers().get(player).equals(Role.SURVIVOR)) {
                                player.removePotionEffect(PotionEffectType.BLINDNESS);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20*2, 0));
                            }
                            players.add(player);
                        }
                    }
                    betterTogether.put(gamePlayer.getPlayer().getUniqueId(), players);
                }
                Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> {
                    gamePlayer.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
                    gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
                    gamePlayer.getPlayer().setSprinting(false);
                    if(gamePlayer.getEquippedSurvivorPerk().equals(Perks.SURVIVOR_PERK_BETTER_TOGETHER)) {
                        for(Player player : betterTogether.get(gamePlayer.getPlayer().getUniqueId())) {
                            if(!arena.getPlayers().containsKey(player) && arena.getPlayers().get(player) != Role.SURVIVOR)
                                continue;
                            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
                            player.setSprinting(false);
                        }
                    }
                    betterTogether.remove(gamePlayer.getPlayer().getUniqueId());
                }, 40L);
            }
        }
    }

    @EventHandler
    public void pickupPageEvent(SlenderSurvivorPickupPageEvent event) {
        if(event.getSurvivor().getEquippedSurvivorPerk().equals(Perks.SURVIVOR_PERK_ARCHAEOLOGIST)) {
            event.getSurvivor().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*3, 0));
            event.getSurvivor().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*3, 0));
        }
    }

}