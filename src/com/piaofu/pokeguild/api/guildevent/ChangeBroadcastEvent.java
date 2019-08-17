package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;

/**
 * 改变公告事件
 */
public class ChangeBroadcastEvent extends GuildPlayerEvent{


    private String newInfo;
    private String oldInfo;

    public ChangeBroadcastEvent(GuildPlayer guildPlayer, String oldInfo, String newInfo) {
        super(guildPlayer);
        this.newInfo = newInfo;
        this.oldInfo = oldInfo;
    }


    public String getNewInfo() {
        return newInfo;
    }

    public String getOldInfo() {
        return oldInfo;
    }
}
