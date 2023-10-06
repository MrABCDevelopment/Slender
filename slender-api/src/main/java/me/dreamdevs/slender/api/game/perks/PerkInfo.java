package me.dreamdevs.slender.api.game.perks;

import me.dreamdevs.slender.api.game.Role;
import org.bukkit.Material;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PerkInfo {

	String name();

	Material icon();

	Role role();

	boolean free() default false;

}