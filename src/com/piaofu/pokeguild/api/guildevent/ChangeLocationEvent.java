package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.locationplus.GuildLocation;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import org.bukkit.event.HandlerList;

/**
 * 改变基地事件
 */
public class ChangeLocationEvent extends GuildPlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private GuildPlayer player;
    private GuildLocation changTo;

    public ChangeLocationEvent(GuildPlayer player, GuildLocation changTo) {
        super(player);
        this.player = player;
        this.changTo = changTo;
    }



    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public GuildPlayer getPlayer() {
        return player;
    }

    public GuildLocation getChangTo() {
        return changTo;
    }
}
