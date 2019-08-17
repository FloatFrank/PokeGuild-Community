package com.piaofu.pokeguild.papi;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.HashMap;

public class PapiHolder {
    public static HashMap<PAPI, Method> hashMap = new HashMap<>();

    @PAPI(hookName = "guildname")
    public static String getGuildName(Player player, Guild guild) {
        return (guild).getGuildName();
    }

    @PAPI(hookName = "guildowner")
    public static String getGuildOwner(Player player, Guild guild) {
        return Bukkit.getOfflinePlayer((guild).getGuildOwner()).getName();
    }

    @PAPI(hookName = "guildjob")
    public static String getGuildJob(Player player, Guild guild) {
        GuildPlayer guildPlayer = GuildTools.getGuildPlayerByGuild(guild, player.getUniqueId());
        return guildPlayer == null ? Message.getMsg(Message.EMPTY) : guildPlayer.getPost().getPostName();
    }
    @PAPI(hookName = "guildcontribution")
    public static String getGuildContribution(Player player, Guild guild) {
        GuildPlayer guildPlayer = GuildTools.getGuildPlayerByGuild(guild, player.getUniqueId());
        return guildPlayer == null ? Message.getMsg(Message.EMPTY) : String.valueOf(guildPlayer.getContribution());
    }
    @PAPI(hookName = "guildperson")
    public static String getGuildPerson(Player player, Guild guild) {
        return "" + guild.getPlayers().size();
    }
    @PAPI(hookName = "guildmaxperson")
    public static String getGuildMaxPerson(Player player, Guild guild) {
        return "" + guild.getLevel().getMaxPerson();
    }
    @PAPI(hookName = "guildlevel")
    public static String getGuildLevel(Player player, Guild guild) {
        return "" + guild.getLevel().getLevel();
    }
    @PAPI(hookName = "guildmoney")
    public static String getGuildMoney(Player player, Guild guild) {
        return "" + guild.getMoney();
    }

}
