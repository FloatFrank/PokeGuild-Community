package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.Guild;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 公会相关事件
 */
public class GuildEvent extends Event implements Cancellable {
    private Guild guild;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public GuildEvent(Guild guild) {
        this.guild = guild;
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

    public Guild getGuild() {
        return guild;
    }
}
