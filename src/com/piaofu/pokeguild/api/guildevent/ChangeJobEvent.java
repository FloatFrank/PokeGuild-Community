package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import org.bukkit.event.HandlerList;

/**
 * 改变职位事件
 */
public class ChangeJobEvent extends GuildPlayerEvent
{
    private static final HandlerList handlers = new HandlerList();
    private GuildPost changeTo;

    public ChangeJobEvent(GuildPlayer guildPlayer, GuildPost changeTo, GuildPost changeFrom) {
        super(guildPlayer);
        this.changeTo = changeTo;
        this.changeFrom = changeFrom;
    }

    private GuildPost changeFrom;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public GuildPost getChangeTo() {
        return changeTo;
    }

    public GuildPost getChangeFrom() {
        return changeFrom;
    }
}
