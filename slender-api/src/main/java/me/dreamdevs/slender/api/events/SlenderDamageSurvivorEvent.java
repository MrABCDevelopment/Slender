package me.dreamdevs.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.slender.api.database.IGamePlayer;
import me.dreamdevs.slender.api.game.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class SlenderDamageSurvivorEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final IGamePlayer slenderMan;
    private final IGamePlayer survivor;
    private final IArena arena;
    private double damage;

    public SlenderDamageSurvivorEvent(IGamePlayer slenderMan, IGamePlayer survivor, IArena arena, double damage) {
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