package com.piaofu.pokeguild.guild.guildgift;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class GiftListener implements Listener {
    @EventHandler
    public static void onPlayerKillMob(EntityDeathEvent event) {
        if(event.getEntity() instanceof Player) {
            return;
        }
        LivingEntity entity = event.getEntity();
        if(entity.getKiller() == null) {
            return;
        }
        Player player = entity.getKiller();
        GuildPlayer guildPlayer = GuildTools.getGuildPlayer(player.getUniqueId());
        if(guildPlayer == null) {
            return;
        }
        int g = 0;
        String name = entity.getCustomName();
//        System.out.println(name);
        ConfigurationSection configurationSection = PokeGuild.plugin.getConfig().getConfigurationSection("KillMobByName");
        for (String item : configurationSection.getKeys(false)) {
            if (configurationSection.getString(item + ".name").replace("&","§").equals(name)) {
                g = configurationSection.getInt(item + ".addc");
                break;
            }
        }
        if(g!=0) {
            guildPlayer.addContribution(g);
            guildPlayer.getGuild().setBattlePower(guildPlayer.getGuild().getBattlePower() + g);
            String msg =  Message.format("你击杀了怪物{0}§r, 获得了{1}§r点贡献", name, String.valueOf(g));
            player.sendMessage(msg);
        }
    }
}
