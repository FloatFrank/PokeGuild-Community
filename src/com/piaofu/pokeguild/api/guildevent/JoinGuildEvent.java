package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;

/**
 * 加入公会事件
 */
public class JoinGuildEvent extends GuildPlayerEvent {
    public JoinGuildEvent(GuildPlayer guildPlayer) {
        super(guildPlayer);
    }

}
