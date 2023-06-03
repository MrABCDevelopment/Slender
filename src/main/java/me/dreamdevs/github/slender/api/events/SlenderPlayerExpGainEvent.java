package me.dreamdevs.github.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.github.slender.game.GamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class SlenderPlayerExpGainEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final GamePlayer gamePlayer;
    private int exp;

    public SlenderPlayerExpGainEvent(GamePlayer gamePlayer, int exp) {
        this.gamePlayer = gamePlayer;
        this.exp = exp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}