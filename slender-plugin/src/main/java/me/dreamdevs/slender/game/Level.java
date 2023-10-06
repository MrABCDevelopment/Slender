package me.dreamdevs.slender.game;

import lombok.Getter;

@Getter
public class Level {

    private final int requireExp;

    public Level(int requireExp) {
        this.requireExp = requireExp;
    }

}