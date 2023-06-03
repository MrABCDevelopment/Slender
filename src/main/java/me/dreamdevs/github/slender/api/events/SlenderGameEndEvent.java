package me.dreamdevs.github.slender.api.events;

import lombok.Getter;
import me.dreamdevs.github.slender.game.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SlenderGameEndEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final Arena arena;

    public SlenderGameEndEvent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
