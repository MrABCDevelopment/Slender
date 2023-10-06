package me.dreamdevs.slender.game;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.slender.SlenderMain;
import me.dreamdevs.slender.api.*;
import me.dreamdevs.slender.api.Statistic;
import me.dreamdevs.slender.api.events.SlenderGameEndEvent;
import me.dreamdevs.slender.api.events.SlenderGameStartEvent;
import me.dreamdevs.slender.api.game.ArenaState;
import me.dreamdevs.slender.api.game.IArena;
import me.dreamdevs.slender.api.game.Role;
import me.dreamdevs.slender.api.utils.ColourUtil;
import me.dreamdevs.slender.api.utils.Util;
import me.dreamdevs.slender.database.data.GamePlayer;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter @Setter
public class Arena extends BukkitRunnable implements IArena {

    private final String id;
    private int minPlayers;
    private int maxPlayers;
    private int gameTime;
    private int timer;
    private Location slenderManSpawnLocation;
    private List<Location> survivorsLocations;
    private List<Location> pagesLocations;
    private ArenaState arenaState;
    private Map<Player, Role> players;
    private Player slenderMan;

    private BossBar bossBar;

    private int collectedPages;

    private Scoreboard scoreboard;
    private Objective objective;

    private File file;

    private BukkitTask radiusTask;

    public Arena(String id) {
        this.id = id;
        this.arenaState = ArenaState.WAITING;
        this.players = new ConcurrentHashMap<>();
        this.survivorsLocations = new ArrayList<>();
        this.pagesLocations = new ArrayList<>();
        this.bossBar = Bukkit.createBossBar(Langauge.ARENA_BOSS_BAR_WAITING_TITLE.toString(), BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
        this.slenderMan = null;

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.scoreboard.registerNewObjective(id, "dummy", id);

        this.scoreboard.registerNewTeam("survivors");
        this.scoreboard.registerNewTeam("slenderman");

        this.scoreboard.getTeam("survivors").setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        this.scoreboard.getTeam("slenderman").setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public void startGame() {
        runTaskTimer(SlenderMain.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        switch (arenaState) {
            case WAITING:
                sendTitleToAllPlayers("", "Waiting for players", 0, 25, 25);
                break;
            case STARTING:
                sendTitleToAllPlayers(Langauge.EMPTY.toString(), Langauge.ARENA_STARTING_SUBTITLE.toString().replace("%TIME%", String.valueOf(timer)), 0, 25, 25);
                if (timer == 0) {
                    start();
                    break;
                }
                timer--;
                break;
            case RUNNING:
                this.bossBar.setTitle(Langauge.ARENA_BOSS_BAR_RUNNING_TITLE.toString().replace("%TIME%", String.valueOf(timer)));
                sendActionBar(Langauge.ARENA_COLLECTED_PAGES.toString().replace("%CURRENT%", Integer.toString(collectedPages)));
                if(timer == 0) {
                    endGame();
                    return;
                }
                timer--;
                break;
            case ENDING:
                this.bossBar.setTitle(Langauge.ARENA_BOSS_BAR_ENDING_TITLE.toString().replace("%TIME%", String.valueOf(timer)));
                if(timer == 0) {
                    restart();
                    return;
                }
                timer--;
                break;
            case RESTARTING:
                Bukkit.getWorlds().forEach(world -> world.getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove));
                this.bossBar.setTitle(Langauge.ARENA_BOSS_BAR_WAITING_TITLE.toString());
                this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                this.scoreboard.registerNewObjective(id, "dummy", id);

                this.scoreboard.registerNewTeam("survivors");
                this.scoreboard.registerNewTeam("slenderman");
                this.slenderMan = null;
                Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> setArenaState(ArenaState.WAITING), 100L);
                break;
        }
    }

    public void start() {
        this.bossBar.setTitle(ColourUtil.colorize(Langauge.ARENA_BOSS_BAR_RUNNING_TITLE.toString().replace("%TIME%", String.valueOf(timer))));
        sendTitleToAllPlayers(Langauge.ARENA_TITLE.toString(), Langauge.ARENA_STARTED_SUBTITLE.toString(), 10, 30, 10);
        sendPlayersToGame();
        setArenaState(ArenaState.RUNNING);
        setTimer(gameTime);
        spawnPage();
        if(Config.USE_TERROR_RADIUS.toBoolean())
            Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> radiusTask = Bukkit.getScheduler().runTaskTimer(SlenderMain.getInstance(), this::terrorRadius, 10L, 10L), 20L);
        SlenderGameStartEvent slenderGameStartEvent = new SlenderGameStartEvent(this);
        Bukkit.getPluginManager().callEvent(slenderGameStartEvent);
    }

    private void sendPlayersToGame() {
        List<Player> tempList = new ArrayList<>(players.keySet());
        int size = tempList.size();
        int random = Util.getRandomNumber(size);
        this.slenderMan = tempList.get(random);
        tempList.remove(slenderMan);
        tempList.forEach(player -> players.put(player, Role.SURVIVOR));
        players.put(slenderMan, Role.SLENDER);

        slenderMan.getScoreboard().getTeam("slenderman").addPlayer(slenderMan);
        slenderMan.getInventory().clear();
        slenderMan.getInventory().setItem(0, CustomItem.SLENDERMAN_WEAPON.toItemStack());
        slenderMan.getInventory().setItem(1, CustomItem.SLENDERMAN_COMPASS.toItemStack());
        GamePlayer gameSlenderMan = SlenderMain.getInstance().getPlayerManager().getPlayer(slenderMan);
        //slenderMan.getInventory().setItem(4, gameSlenderMan.getEquippedSlenderManPerk().toItemStack());

        tempList.forEach(player -> {
            player.teleport(survivorsLocations.get(Util.getRandomNumber(survivorsLocations.size())));
            player.getScoreboard().getTeam("survivors").addPlayer(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
            player.getInventory().clear();
            player.getInventory().setItem(0, CustomItem.SURVIVOR_WEAPON.toItemStack());
            player.getInventory().setItem(1, new ItemStack(Material.TORCH, 3));
            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(player);
           // player.getInventory().setItem(4, gamePlayer.getEquippedSurvivorPerk().toItemStack());
        });

        slenderMan.teleport(slenderManSpawnLocation);
        slenderMan.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        slenderMan.setHealth(40);
        slenderMan.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE));
        if(SlenderApi.isLibsDisguisedEnabled) {
            DisguiseAPI.disguiseToAll(slenderMan, new MobDisguise(DisguiseType.ENDERMAN));
            DisguiseAPI.setActionBarShown(slenderMan, false);
            DisguiseAPI.setViewDisguiseToggled(slenderMan, false);
        }

    }

    private void sendTitleToAllPlayers(String title, String subTitle, int fadeIn, int stayIn, int fadeOut) {
        if (players.isEmpty())
            return;
        players.keySet().forEach(player -> player.sendTitle(ColourUtil.colorize(title), ColourUtil.colorize(subTitle), fadeIn, stayIn, fadeOut));
    }

    private void sendActionBar(String message) {
        players.keySet().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColourUtil.colorize(message))));
    }

    public void restart() {
        Bukkit.getWorlds().forEach(world -> world.getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove));

        SlenderMain.getInstance().getPlayerManager().getPlayers().stream().filter(gamePlayer -> gamePlayer.isInArena() && gamePlayer.getArena().equals(this) && (Boolean) gamePlayer.getSetting(Setting.AUTO_JOIN_MODE)).forEach(gamePlayer -> {
            SlenderMain.getInstance().getGameManager().leaveGame(gamePlayer.getPlayer(), this);
            Arena arena = SlenderMain.getInstance().getGameManager().getAvailableArena();
            if(arena == null) {
                gamePlayer.getPlayer().sendMessage(Langauge.ARENA_NO_AVAILABLE_ARENAS.toString());
                return;
            }
            SlenderMain.getInstance().getGameManager().joinGame(gamePlayer.getPlayer(), arena);
        });

        players.keySet().forEach(player -> SlenderMain.getInstance().getGameManager().leaveGame(player, this));

        setArenaState(ArenaState.RESTARTING);
        this.scoreboard = null;
        players.clear();
        setCollectedPages(0);
    }

    public void endGame() {
        this.bossBar.setTitle(Langauge.ARENA_BOSS_BAR_RUNNING_TITLE.toString().replace("%TIME%", String.valueOf(timer)));
        this.radiusTask.cancel();
        if(getCollectedPages() < 8) {
            sendTitleToAllPlayers(Langauge.ARENA_TITLE.toString(), Langauge.ARENA_WIN_SLENDERMAN_SUBTITLE.toString(), 10, 50, 10);

            GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(slenderMan);
            gamePlayer.setStatistic(Statistic.WINS, gamePlayer.getStatistic(Statistic.WINS)+1);

        } else {
            sendTitleToAllPlayers(Langauge.ARENA_TITLE.toString(), Langauge.ARENA_WIN_SURVIVORS_SUBTITLE.toString(),10, 50, 10);
            getPlayers().entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() == Role.SURVIVOR).forEach(playerRoleEntry -> {
                GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(playerRoleEntry.getKey());
                gamePlayer.setStatistic(Statistic.WINS, gamePlayer.getStatistic(Statistic.WINS)+1);
            });
        }

        getPlayers().keySet().forEach(player -> {
            getPlayers().put(player, Role.NONE);
            player.getInventory().clear();
            player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
            player.getInventory().setItem(7, CustomItem.PLAY_AGAIN.toItemStack());
            player.getInventory().setItem(8, CustomItem.LEAVE.toItemStack());
        });

        setArenaState(ArenaState.ENDING);
        setTimer(15);
        SlenderGameEndEvent slenderGameEndEvent = new SlenderGameEndEvent(this);
        Bukkit.getPluginManager().callEvent(slenderGameEndEvent);
    }

    public void spawnPage() {
        ItemStack itemStack = new ItemStack(Material.PAPER);

        Item item = slenderManSpawnLocation.getWorld().dropItem(getPagesLocations().get(Util.getRandomNumber(getPagesLocations().size())), itemStack);
        item.setCustomName(Langauge.ARENA_COLLECTED_PAGES.toString().replace("%NUMBER%", String.valueOf((collectedPages+1))));
        item.setCustomNameVisible(true);

        sendMessage(Langauge.ARENA_PAGE_SPAWNED_INFO.toString());
    }

    public int getSurvivorsAmount() {
        return (int) players.entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() == Role.SURVIVOR).count();
    }

    public void sendMessage(String message) {
        if (players.isEmpty()) {
            return;
        }
        players.keySet().forEach(player -> player.sendMessage(ColourUtil.colorize(message)));
    }

    public boolean isRunning() {
        return getArenaState() == ArenaState.RUNNING || getArenaState() == ArenaState.ENDING;
    }

    public List<Player> getSurvivors() {
        return players.entrySet().stream()
                .filter(playerRoleEntry -> playerRoleEntry.getValue().equals(Role.SURVIVOR))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void terrorRadius() {
        if(getArenaState() == ArenaState.RUNNING) {
            World world = slenderMan.getWorld();
            world.getNearbyEntities(slenderMan.getLocation(), Config.TERROR_RADIUS.toInt(), Config.TERROR_RADIUS.toInt(), Config.TERROR_RADIUS.toInt())
                    .stream().filter(Player.class::isInstance)
                    .map(Player.class::cast)
                    .filter(player -> getPlayers().containsKey(player) && getPlayers().get(player) != Role.SLENDER)
                    .forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_SNOW_BREAK, 2f, 2f));
        } else {
            this.radiusTask.cancel();
        }
    }

    @Override
    public Location getSlenderManSpawnLocation() {
        return slenderManSpawnLocation;
    }
}