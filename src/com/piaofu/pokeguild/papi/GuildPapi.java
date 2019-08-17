package com.piaofu.pokeguild.papi;

import com.piaofu.pokeguild.command.CommandsHolder;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.item.GuildItemForPB;
import com.piaofu.pokeguild.item.ItemHolder;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class GuildPapi extends PlaceholderHook {

    public GuildPapi(){
        PlaceholderAPI.registerPlaceholderHook("pg", this);
        for (final Method method : PapiHolder.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PAPI.class)) {
                PapiHolder.hashMap.put(method.getAnnotation(PAPI.class), method);
            }
        }
    }

    @Override
    public String onPlaceholderRequest(Player player, String string) {
        Guild guild;
        guild = GuildTools.getPlayerGuild(player.getUniqueId());
        for(PAPI papi : PapiHolder.hashMap.keySet()) {
            if(papi.hookName().equals(string)) {
                if(guild == null && papi.needGuild()) {
                    return "无";
                }
                try {
                    return (String) PapiHolder.hashMap.get(papi).invoke(PapiHolder.class, player, guild);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return "无";
    }
}
