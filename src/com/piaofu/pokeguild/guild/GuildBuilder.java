package com.piaofu.pokeguild.guild;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.api.guildevent.CreateGuildEvent;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.guild.guilddata.GuildConfig;
import com.piaofu.pokeguild.guild.guildlevel.GuildLevel;
import com.piaofu.pokeguild.guild.guildlevel.LevelHolder;
import com.piaofu.pokeguild.guild.guildrelation.GuildRelation;
import com.piaofu.pokeguild.guild.locationplus.GuildLocation;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.piaofu.pokeguild.data.DataLoader.getRegister;

public class GuildBuilder {
    public static void buildAllGuild() {
        GuildHolder.getGuilds().clear();
        List<String> register = DataLoader.getRegister();
        if (register == null || register.size() == 0) {return;}
        for (String g : register) {
            try {
                Guild guild = new Guild(g);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§4请检查配置文件夹guilddata里的" + g + ".yml文件，在不影响数据的情况下，建议将其删除");
                DataLoader.delGuildRegister(g);
            }
        }
    }
    public static boolean buildAFirstGuild(Player player, String name) {
        if (DataLoader.getRegister().contains(name)) {
            player.sendMessage(Message.getMsg(Message.HASGUILDSAMENAME));
            return false;
        }
        CreateGuildEvent event = new CreateGuildEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return false;
        }
        new Guild(name, player);
        return true;
    }

}
