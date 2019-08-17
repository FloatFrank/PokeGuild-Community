package com.piaofu.pokeguild.guild.guildrelation;

import com.piaofu.pokeguild.guild.Guild;
import com.sun.istack.internal.Nullable;

import java.util.List;

public class GuildRelation {
    private List<Guild> friendly;
    private List<Guild> hostility;

    public GuildRelation(@Nullable List<Guild> friendly, @Nullable List<Guild> hostility) {
        this.friendly = friendly;
        this.hostility = hostility;
    }

    /**
     * 为该公会关系链增加一个友好的公会
     * @param guild 操作公会
     */
    public void addFriendly(Guild guild){
        if (!friendly.contains(guild)) {
            friendly.add(guild);
        }
        delHostility(guild);
    }

    /**
     * 将公会关系链移至中立关系链
     * @param guild 操作公会
     */
    public void delFriendly(Guild guild){
        if (friendly.contains(guild)) {
            friendly.remove(guild);
        }
    }

    /**
     * 将公会关系链添加至敌对关系链
     * @param guild 操作公会
     */
    public void addHostility(Guild guild){
        if (!hostility.contains(guild)) {
            hostility.add(guild);
        }
        delFriendly(guild);
    }
    /**
     * 将公会关系链移至中立关系链
     * @param guild 操作公会
     */
    public void delHostility(Guild guild){
        if (hostility.contains(guild)) {
            hostility.remove(guild);
        }
    }
}
