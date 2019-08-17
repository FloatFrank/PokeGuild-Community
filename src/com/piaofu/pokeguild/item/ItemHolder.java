package com.piaofu.pokeguild.item;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guildpoint.PointObject;
import com.piaofu.pokeguild.guildpoint.PointTools;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ItemHolder {
    public static HashMap<GuildItemForPB, Method> itemMap = new HashMap<>();
    public static HashMap<GuildItemForEE, Method> itemMapEE = new HashMap<>();
    @GuildItemForPB(name = "据点修复石", description = "本公会的公会成员持本石对公会的据点进行修理", action = Action.LEFT_CLICK_BLOCK)
    public static boolean pointRepair(Player player, Guild guild, Block block, ConfigurationSection section) {
        PointObject pointObject = PointTools.getBlockIsAGuildPoint(guild, block);
        if(pointObject == null) {
            player.sendMessage(Message.getMsg(Message.IS_NOT_A_POINT));
            return false;
        }
        int heal = section.getInt("Health");
        pointObject.heal(heal);
        player.sendMessage(Message.getMsg(Message.HEALTH_MESSAGE , String.valueOf(heal), String.valueOf(pointObject.getHealth()), String.valueOf(pointObject.getMaxHealth())));

        return true;
    }

    @GuildItemForPB(name = "据点防御石", description = "为公会的所有据点在N秒内加上一层防御，其他公会玩家攻击据点后将被强行击杀, 效果不可叠加", action = Action.LEFT_CLICK_BLOCK)
    public static boolean pointHelper(Player player, Guild guild, Block block, ConfigurationSection section) {
        PointObject pointObject = PointTools.getBlockIsAGuildPoint(guild, block);
        if(pointObject == null) {
            player.sendMessage(Message.getMsg(Message.IS_NOT_YOUR_POINT));
            return false;
        }
        int time = section.getInt("Time");
        boolean kill = section.getBoolean("KillPlayer");
        if(!pointObject.setProtect(time, kill)) {
            player.sendMessage(Message.getMsg(Message.FALL_USE_PROTECT));
            return false;
        }
        player.sendMessage(Message.getMsg(Message.SUCCESS_USE_PROTECT, String.valueOf(time)));
        return true;

    }

    @GuildItemForPB(name = "公会改名卡", description = "为公会修改名字，使用后会打开一个GUI", action = Action.RIGHT_CLICK_AIR)
    public static void guildChangeName(Player player, Guild guild, ConfigurationSection section) {
        player.sendMessage("测试成功");
    }

    @GuildItemForEE(name = "据点驱赶石", description = "本公会的公会成员在自己的公会据点内右键")
    public static boolean pointKillOther(Player player, Player targetPlayer, Guild guild, ConfigurationSection section) {
        PointObject pointObject = PointTools.isInAPoint(targetPlayer);
        if(pointObject == null) {
            player.sendMessage(Message.getMsg(Message.IS_NOT_A_POINT));
        }else if(pointObject.getGuild() != guild) {
            player.sendMessage(Message.getMsg(Message.IS_NOT_A_POINT));
        }else if(GuildTools.getPlayerGuild(targetPlayer.getUniqueId()) == guild) {
            player.sendMessage(Message.getMsg(Message.CANNOT_ATTACK_MEMBER));
        }else {
            player.sendMessage(Message.getMsg(Message.SUCCESS_KILL_PALYER));
            targetPlayer.sendMessage(Message.getMsg(Message.SUCCESS_KILL_PALYER_INFO, player.getName()));
            targetPlayer.setHealth(0);
            return true;
        }
        return false;
    }

}
