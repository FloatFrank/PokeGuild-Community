package com.piaofu.pokeguild.api.guildevent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 进入温泉事件
 */
public class JoinSpringEvent extends Event {

    private Player player;
    private final static HandlerList handles = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handles;
    }
    public JoinSpringEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
