package me.dreamdevs.github.slender.api.events;

import lombok.Getter;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SlenderQuitArenaEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final GamePlayer gamePlayer;
    private final Arena arena;

    public SlenderQuitArenaEvent(GamePlayer gamePlayer, Arena arena) {
        this.gamePlayer = gamePlayer;
        this.arena = arena;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}