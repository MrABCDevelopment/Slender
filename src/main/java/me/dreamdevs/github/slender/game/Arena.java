package me.dreamdevs.github.slender.game;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.SlenderMain;
import me.dreamdevs.github.slender.api.events.SlenderGameEndEvent;
import me.dreamdevs.github.slender.api.events.SlenderGameStartEvent;
import me.dreamdevs.github.slender.utils.ColourUtil;
import me.dreamdevs.github.slender.utils.Util;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Arena extends BukkitRunnable {

    private String id;
    private int minPlayers;
    private int maxPlayers;
    private int gameTime;
    private int timer;
    private Location slenderSpawnLocation;
    private List<Location> survivorsLocations;
    private ArenaState arenaState;
    private Map<Player, Role> players;
    private Player slenderMan;

    private BossBar bossBar;

    private int collectedPages;

    private Scoreboard scoreboard;
    private Objective objective;

    private File file;

    public Arena(String id) {
        this.id = id;
        this.arenaState = ArenaState.WAITING;
        this.players = new ConcurrentHashMap<>();
        this.survivorsLocations = new ArrayList<>();
        this.bossBar = Bukkit.createBossBar(ColourUtil.colorize("&c&lStop It Slender!"), BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
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
        if(arenaState == ArenaState.WAITING) {
            if(players.isEmpty()) return;
            sendTitleToAllPlayers("", "Waiting for players...", 0, 25, 25);
            return;
        }

        if(arenaState == ArenaState.STARTING) {
            sendTitleToAllPlayers("", "Starting in "+timer+" seconds...", 0, 25, 25);
            if(timer == 0) {
                this.bossBar.setTitle(ColourUtil.colorize("&cTime left: "+timer+" seconds..."));
                sendTitleToAllPlayers("&c&lStop It Slender!", "&4Good Luck!", 10, 30, 10);
                sendPlayersToGame();
                setArenaState(ArenaState.RUNNING);
                setTimer(gameTime);
                SlenderGameStartEvent slenderGameStartEvent = new SlenderGameStartEvent(this);
                Bukkit.getPluginManager().callEvent(slenderGameStartEvent);
                return;
            }
            timer--;
        }

        if(arenaState == ArenaState.RUNNING) {
            this.bossBar.setTitle(ColourUtil.colorize("&cTime left: "+timer+" seconds..."));
            sendActionBar(SlenderMain.getInstance().getMessagesManager().getMessage("arena-collected-pages").replaceAll("%CURRENT%", Integer.toString(collectedPages)));
            if(timer == 0) {
                if(getCollectedPages() < 8) {
                    sendTitleToAllPlayers("&c&lStop It Slender!", "&cSlenderMan won the game!", 10, 50, 10);

                    GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(slenderMan);
                    gamePlayer.setWins(gamePlayer.getWins()+1);

                } else {
                    sendTitleToAllPlayers("&c&lStop It Slender!", "&aSurvivors won the game!", 10, 50, 10);
                    getPlayers().entrySet().stream().filter(playerRoleEntry -> playerRoleEntry.getValue() == Role.SURVIVOR).forEach(playerRoleEntry -> {
                        GamePlayer gamePlayer = SlenderMain.getInstance().getPlayerManager().getPlayer(playerRoleEntry.getKey());
                        gamePlayer.setWins(gamePlayer.getWins()+1);
                    });
                }

                setArenaState(ArenaState.ENDING);
                setTimer(15);
                SlenderGameEndEvent slenderGameEndEvent = new SlenderGameEndEvent(this);
                Bukkit.getPluginManager().callEvent(slenderGameEndEvent);
                return;
            }
            timer--;
        }

        if(arenaState == ArenaState.ENDING) {
            if(timer == 0) {
                restart();
            }
            timer--;
        }

        if(arenaState == ArenaState.RESTARTING) {
            this.bossBar.setTitle(ColourUtil.colorize("&c&lStop It Slender!"));
            this.scoreboard.getTeams().forEach(team -> team.getPlayers().clear());
            this.slenderMan = null;
            Bukkit.getScheduler().runTaskLater(SlenderMain.getInstance(), () -> setArenaState(ArenaState.WAITING), 100L);
        }

    }

    private void sendPlayersToGame() {
        List<Player> tempList = new ArrayList<>(players.keySet());
        this.slenderMan = tempList.get(Util.getRandomNumber(tempList.size()));
        tempList.remove(slenderMan);
        tempList.forEach(player -> players.put(player, Role.SURVIVOR));
        players.put(slenderMan, Role.SLENDER);

        slenderMan.getScoreboard().getTeam("slenderman").addPlayer(slenderMan);

        tempList.forEach(player -> {
            player.teleport(survivorsLocations.get(Util.getRandomNumber(survivorsLocations.size())));
            player.getScoreboard().getTeam("survivors").addPlayer(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
        });
        DisguiseAPI.disguiseToAll(slenderMan, new MobDisguise(DisguiseType.ENDERMAN));

        slenderMan.teleport(slenderSpawnLocation);
        slenderMan.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    private void sendTitleToAllPlayers(String title, String subTitle, int fadeIn, int stayIn, int fadeOut) {
        players.keySet().forEach(player -> player.sendTitle(ColourUtil.colorize(title), ColourUtil.colorize(subTitle), fadeIn, stayIn, fadeOut));
    }

    private void sendActionBar(String message) {
        players.keySet().forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColourUtil.colorize(message))));
    }

    public void restart() {
        players.keySet().forEach(player -> {
            player.sendMessage(ColourUtil.colorize(SlenderMain.getInstance().getMessagesManager().getMessage("arena-game-stopped")));
            SlenderMain.getInstance().getGameManager().leaveGame(player, this);
        });
        setArenaState(ArenaState.RESTARTING);
        players.clear();
        setCollectedPages(0);
    }

    public void sendMessage(String message) {
        players.keySet().forEach(player -> player.sendMessage(ColourUtil.colorize(message)));
    }

}