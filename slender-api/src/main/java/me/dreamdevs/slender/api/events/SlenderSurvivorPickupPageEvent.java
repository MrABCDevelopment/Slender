package me.dreamdevs.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.slender.api.database.IGamePlayer;
import me.dreamdevs.slender.api.game.IArena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class SlenderSurvivorPickupPageEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final IGamePlayer survivor;
    private final IArena arena;
    private int collectedPage;

    public SlenderSurvivorPickupPageEvent(IGamePlayer survivor, IArena arena, int collectedPage) {
        this.survivor = survivor;
        this.arena = arena;
        this.collectedPage = collectedPage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}