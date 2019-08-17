package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import org.bukkit.event.HandlerList;

/**
 * 改变信息事件
 */
public class ChangeInfomationEvent extends GuildPlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private String newInfo;
    private String oldInfo;

    public ChangeInfomationEvent(GuildPlayer player, String oldInfo, String newInfo) {
        super(player);

        this.newInfo = newInfo;
        this.oldInfo = oldInfo;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getNewInfo() {
        return newInfo;
    }

    public String getOldInfo() {
        return oldInfo;
    }
}
