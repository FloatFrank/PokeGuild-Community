package com.piaofu.pokeguild.guild.playerdata;

import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * 公会对应玩家类型
 */
public class GuildPlayer {
    private int contribution;
    private UUID uuid;
    private GuildPost post;
    private Guild guild;

    public GuildPlayer(int contribution, UUID uuid, GuildPost post, Guild guild) {
        this.contribution = contribution;
        this.uuid = uuid;
        this.post = post;
        this.guild = guild;
    }
    public Player isOnlineAndGetPlayer() {
        if(Bukkit.getOfflinePlayer(uuid).isOnline())
            return Bukkit.getPlayer(uuid);
        return null;

    }

    public UUID getUuid() {
        return uuid;
    }

    public GuildPost getPost() {
        return post;
    }
    public void setPost(GuildPost post) {
        this.post = post;
    }
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(this.getUuid());
    }
    public void remove() {
        GuildTools.getPlayerGuild(uuid).removePlayer(this);
    }
    public Guild getGuild() {
        return guild;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public void addContribution(int contribution) {
        this.contribution+=contribution;
    }
}
