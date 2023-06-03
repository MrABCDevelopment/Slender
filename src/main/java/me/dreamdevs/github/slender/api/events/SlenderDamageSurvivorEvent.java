package me.dreamdevs.github.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class SlenderDamageSurvivorEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final GamePlayer slenderMan;
    private final GamePlayer survivor;
    private final Arena arena;
    private double damage;

    public SlenderDamageSurvivorEvent(GamePlayer slenderMan, GamePlayer survivor, Arena arena, double damage) {
        this.slenderMan = slenderMan;
        this.survivor = survivor;
        this.arena = arena;
        this.damage = damage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}