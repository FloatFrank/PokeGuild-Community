package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;

/**
 * 给与贡献事件
 */
public class GiveContrationEvent extends GuildPlayerEvent {
    private int contration;
    public GiveContrationEvent(GuildPlayer guildPlayer, int con) {
        super(guildPlayer);
        contration = con;
    }


    public int getContration() {
        return contration;
    }
    public void setContration(int num) {
        contration = num;
    }
}
