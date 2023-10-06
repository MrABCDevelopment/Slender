package me.dreamdevs.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.slender.api.database.IGamePlayer;
import me.dreamdevs.slender.api.game.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class SlenderKillSurvivorEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final IGamePlayer slenderMan;
    private final IGamePlayer survivor;
    private final IArena arena;

    public SlenderKillSurvivorEvent(IGamePlayer slenderMan, IGamePlayer survivor, IArena arena) {
        this.slenderMan = slenderMan;
        this.survivor = survivor;
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}