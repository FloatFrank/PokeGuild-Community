package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;

/**
 * 公会玩家操作类
 */
public class GuildPlayerEvent extends GuildEvent{
    private GuildPlayer guildPlayer;
    public GuildPlayerEvent(GuildPlayer guildPlayer) {
        super(guildPlayer.getGuild());
        this.guildPlayer = guildPlayer;
    }
    public GuildPlayer getGuildPlayer() {
        return guildPlayer;
    }

}
