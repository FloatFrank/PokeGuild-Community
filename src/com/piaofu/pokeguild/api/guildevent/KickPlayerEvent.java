package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
/**
 * 踢出玩家事件
 */
public class KickPlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private GuildPlayer kicker;
    private GuildPlayer kicked;
    private boolean cancelled;

    public KickPlayerEvent(GuildPlayer kicker, GuildPlayer kicked) {
        this.kicker = kicker;
        this.kicked = kicked;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public GuildPlayer getKicker() {
        return kicker;
    }

    public GuildPlayer getKicked() {
        return kicked;
    }
}
