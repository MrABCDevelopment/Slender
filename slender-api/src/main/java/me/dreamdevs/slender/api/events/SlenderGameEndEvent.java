package me.dreamdevs.slender.api.events;

import lombok.Getter;
import me.dreamdevs.slender.api.game.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SlenderGameEndEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final IArena arena;

    public SlenderGameEndEvent(IArena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
