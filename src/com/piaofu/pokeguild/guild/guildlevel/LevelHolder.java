package com.piaofu.pokeguild.guild.guildlevel;

import java.util.HashMap;
import java.util.Map;

public class LevelHolder {
    private static Map<Integer, GuildLevel> levels = new HashMap<>();
    public static void clear(){
        levels.clear();
    }
    public static void add(int level, GuildLevel guildLevel){
        levels.put(level, guildLevel);
    }
    public static GuildLevel get(int level) {
        return levels.get(level);
    }
}
