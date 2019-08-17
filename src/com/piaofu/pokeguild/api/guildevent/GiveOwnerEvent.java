package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;

/**
 * 给予会长事件
 */
public class GiveOwnerEvent extends GuildPlayerEvent {
    private GuildPlayer give;
    public GiveOwnerEvent(GuildPlayer guildPlayer, GuildPlayer give) {
        super(guildPlayer);
        this.give = give;
    }


    public GuildPlayer getGive() {
        return give;
    }
}
