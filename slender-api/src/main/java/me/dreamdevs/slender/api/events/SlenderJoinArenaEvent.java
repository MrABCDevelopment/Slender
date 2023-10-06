package me.dreamdevs.slender.api.events;

import lombok.Getter;
import me.dreamdevs.slender.api.database.IGamePlayer;
import me.dreamdevs.slender.api.game.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SlenderJoinArenaEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final IGamePlayer gamePlayer;
    private final IArena arena;

    public SlenderJoinArenaEvent(IGamePlayer gamePlayer, IArena arena) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}