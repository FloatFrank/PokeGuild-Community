package com.piaofu.pokeguild.msgformat;

import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private static String msg = "title [id] actionbar [text]";

    public static void sendActionTitle(Player player, String text) {
        String name = player.getName();
        msg = msg.replace("[id]", name).replace("[text]", text);
    }

    @EventHandler
    public static void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Guild guild;
        if (( guild = GuildTools.getPlayerGuild(player.getUniqueId())) != null) {
            event.setFormat("§2[§e"+guild.getGuildName()+"§2]"+GuildTools.getGuildPlayer(player.getUniqueId()).getPost().getPostName() + event.getFormat());

        }
    }

}
