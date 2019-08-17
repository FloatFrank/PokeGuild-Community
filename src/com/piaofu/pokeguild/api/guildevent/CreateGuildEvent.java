package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 创建公会事件
 */
public class CreateGuildEvent extends Event implements Cancellable {
    private Player creator;
    private boolean cancelled;
    private final static HandlerList handers = new HandlerList();

    public CreateGuildEvent(Player player) {
        this.creator = player;
    }

    public Player getCreator() {
        return creator;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handers;
    }
}
