package me.dreamdevs.slender.api.events;

import lombok.Getter;
import me.dreamdevs.slender.api.game.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SlenderGameStartEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private @Getter IArena arena;

    public SlenderGameStartEvent(IArena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}