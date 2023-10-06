package me.dreamdevs.slender.api.events;

import lombok.Getter;
import lombok.Setter;
import me.dreamdevs.slender.api.database.IGamePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter @Setter
public class SlenderPlayerLevelUpEvent extends Event {

    private @Getter static final HandlerList handlerList = new HandlerList();

    private final IGamePlayer gamePlayer;
    private int newLevel;

    public SlenderPlayerLevelUpEvent(IGamePlayer gamePlayer, int newLevel) {
        this.gamePlayer = gamePlayer;
        this.newLevel = newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}