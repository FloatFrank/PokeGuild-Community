package com.piaofu.pokeguild.apilistener;

import com.piaofu.pokeguild.api.guildevent.KickPlayerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onKickPlayer implements Listener {
    @EventHandler
    /**
     * 实现监听器，用此来实现踢出玩家后发送信息
     */
    public static void onKick(KickPlayerEvent event) {
        event.getKicker().getGuild().sendMessage(event.getKicker().getPlayer().getName() +"将玩家"+event.getKicked().getPlayer().getName()
        + "移出公会！");
    }
}
