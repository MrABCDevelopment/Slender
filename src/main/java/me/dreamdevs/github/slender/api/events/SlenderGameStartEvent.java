package me.dreamdevs.github.slender.api.events;

import lombok.Getter;
import me.dreamdevs.github.slender.game.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SlenderGameStartEvent extends Event {

    private final HandlerList handlerList = new HandlerList();

    private @Getter Arena arena;

    public SlenderGameStartEvent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}