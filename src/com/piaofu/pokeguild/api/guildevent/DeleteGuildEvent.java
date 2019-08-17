package com.piaofu.pokeguild.api.guildevent;

import com.piaofu.pokeguild.guild.Guild;
import org.bukkit.entity.Player;

/**
 * 删除公会事件
 */
public class DeleteGuildEvent extends GuildEvent{
    private Player deletor;

    public DeleteGuildEvent(Guild created, Player player) {
        super(created);
        this.deletor = player;
    }

    public Player getCreator() {
        return deletor;
    }

}
