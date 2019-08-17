package com.piaofu.pokeguild.gui;


public @interface GuildGui {
    boolean hasLastGui() default false;
    String lastGuiMethod() default "";
}
