package com.piaofu.pokeguild.guild;

import java.util.ArrayList;
import java.util.List;

public class GuildHolder {
    private static List<Guild> guilds = new ArrayList<>();

    public static List<Guild> getGuilds() {
        return guilds;
    }
    public static void addGuild(Guild guild){
        if (!guilds.contains(guild))
             guilds.add(guild);
    }
    public static void removeGuild(Guild guild){
        guilds.remove(guild);
    }
}
