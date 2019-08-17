package com.piaofu.pokeguild.item;


import org.bukkit.inventory.EquipmentSlot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface  GuildItemForEE {
    String name();
    String description();
    boolean needGuild() default true;
}
