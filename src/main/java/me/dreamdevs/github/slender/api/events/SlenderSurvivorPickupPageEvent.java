package me.dreamdevs.github.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.game.Arena;
import me.dreamdevs.github.slender.game.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class SlenderSurvivorPickupPageEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final GamePlayer survivor;
    private final Arena arena;
    private int collectedPage;

    public SlenderSurvivorPickupPageEvent(GamePlayer survivor, Arena arena, int collectedPage) {
        this.survivor = survivor;
        this.arena = arena;
        this.collectedPage = collectedPage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}