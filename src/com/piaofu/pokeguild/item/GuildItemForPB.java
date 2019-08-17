package com.piaofu.pokeguild.item;

import org.bukkit.event.block.Action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GuildItemForPB {
    String name();
    String description();
    Action[] action() default Action.RIGHT_CLICK_BLOCK;
    boolean needGuild() default true;
}

