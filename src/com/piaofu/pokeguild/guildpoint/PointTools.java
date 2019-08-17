package com.piaofu.pokeguild.guildpoint;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import lk.vexview.VexView;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.VexText;
import lk.vexview.hud.VexImageShow;
import lk.vexview.hud.VexTextShow;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 据点外包接入工具类
 */
public class PointTools {
    //是否在公会的存储器
    public static HashMap<UUID,PointObject> inPointMap = new HashMap<>();

    public static void deleteGuildAllPoint(Guild guild) {
        PointHolder.getPoints().forEach(item-> {
            if(item.getGuild() == guild) {
                item.setGuildName(Message.getMsg(Message.EMPTY));
                item.setGuild(null);
            }
        });
    }
    public static PointObject getPointFromString(String name) {
        for (PointObject item : PointHolder.getPoints()){
            if(item.getName().equals(name))
                return item;
        }
        return null;
    }

    /**
     * 清除一个据点的所有攻击MAP
     * @return
     */
    public static void clearAPointAllMap(PointObject point) {
        point.getAttackMap().clear();
    }

    /**
     * 清除所有据点的攻击MAP
     * @return
     */
    public static void clearAllPointAttackMap() {
        PointHolder.getPoints().forEach(item -> item.getAttackMap().clear());
    }
    public static int[] getWHH() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w <= 0)
            w = 7;
        SimpleDateFormat f = new SimpleDateFormat("HH_HH");
        String t = f.format(date);
        int h = Integer.valueOf(t.split("_")[0]);
        int m = Integer.valueOf(t.split("_")[1]);
        return new int[]{w,h,m};
    }
    /**
     * 获取该时间段是否已经过去
     */
    public static boolean getTimePassB(PointTime time) {
        int[] times = getWHH();
        return time.isIn(times);
    }
    /**
     * 获取当前时间是否在据点战时间段内
     *
     */
    public static PointTime getNowPoint() {
        int[] times = getWHH();
        for(PointTime item : PointHolder.getTimes()) {
            if (item.isIn(times)) {
                return item;
            }
        }
        return null;
    }

    /**
     * 判断一个玩家是否在某个点位里，并给予返回据点对象
     * @param player 玩家对象
     * @return 据点对象
     */
    public static PointObject isInAPoint(Player player) {
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        for (PointObject object : PointHolder.getPoints()) {
            int px = object.getX();
            int py = object.getY();
            int pz = object.getZ();
            if (Math.abs(x-px) < 10 && Math.abs(y-py) < 10 && Math.abs(z-pz) < 10) {
                if (inPointMap.get(player.getUniqueId())==null) {
                    sendTitle(player, object);
                    sendHUD(player, object);
                    object.addPlayer(player.getUniqueId());
                    inPointMap.put(player.getUniqueId(), object);
                }
                return object;
            }
        }
        if(inPointMap.get(player.getUniqueId()) != null) {
            //发送清除器
            PointShow.removeAllShow(player);
        }
        inPointMap.put(player.getUniqueId(), null);
        return null;
    }
    /**
     * 获取一个Block是否是一个Guild的据点
     */
    public static PointObject getBlockIsAGuildPoint(Guild guild, Block block) {
        PointObject pointObject = blockIs(block);
        if(pointObject != null && pointObject.getGuild() == guild) {
            return pointObject;
        }
        return null;
    }

    /**
     * 获取一个Guild的pointNum
     */
    public static int getGuildPointNum(Guild guild) {
        int num = 0;
        for(PointObject point : PointHolder.getPoints()) {
            if(point.getGuild() == guild) {
                num++;
            }
        }
        return num;
    }
    /**
     * 向某个玩家发送某个据点的信息标题
     * @param player 玩家
     * @param obj 据点对象
     */
    public static void sendTitle(Player player, PointObject obj) {
        player.sendTitle(obj.getName(), Message.getMsg(Message.POINT_OWNER_GUILD)+obj.getGuildName());
    }
    /**
     * 向某个玩家发送某个据点的信息HUD
     * @param player 玩家
     * @param obj 据点对象
     */
    public static void sendHUD(Player player, PointObject obj) {
        VexViewAPI.sendHUD(player, new VexImageShow(3124, ImageURL.POINT.getURL(), 100 , 50, 64, 64, 64, 64, 3));
        List<String> strings = new ArrayList<>();
        strings.add("§b§l" + obj.getName());
        VexViewAPI.sendHUD(player, new VexTextShow(4124, 120 , 100, strings, 3));


    }
    /**
     * 向某个玩家发送某个据点的信息GUI
     * @param player 玩家
     * @param obj 据点对象
     */
    public static void sendInfoGui(Player player, PointObject obj) {
        VexGui vexGui = new VexGui(ImageURL.POINT_INFO.getURL(), -1, -1, 280, 280, 300, 300);
        List<String> msg = new ArrayList<>();
        msg.add(Message.getMsg(Message.POINT_INFO_POINT)+   "§9" + obj.getName());
        msg.add("");
        msg.add(Message.getMsg(Message.POINT_OWNER_GUILD)+"§3" + obj.getGuildName());
        msg.add("");
        msg.add("§l§a" + obj.getLevelName());
        msg.add("");
        msg.add(Message.getMsg(Message.POINT_MAKE_MONEY) + obj.getMoney());
        VexText vexText = new VexText(175,180,msg);
        List<String> line = new ArrayList<>();
        double dou = (double)obj.getHealth()/(double)obj.getMaxHealth();
        int long1 = (int)(100 * dou);
        int long2 = 100 - long1;
        line.add("§c" + obj.getHealth() + " §f/ §c" + obj.getMaxHealth());
        VexText vexText1 = new VexText(200,260,line);
        VexImage levelbar1 = new VexImage(ImageURL.GET.getURL()
                ,175,280,long1,10, long1, 10);
        VexImage levelbar2 = new VexImage(ImageURL.NEED.getURL()
                ,175+long1,280,long2,10 ,long2,10);
        vexGui.addComponent(vexText);
        vexGui.addComponent(levelbar1);
        vexGui.addComponent(levelbar2);
        vexGui.addComponent(vexText1);
        VexViewAPI.openGui(player, vexGui);
    }

    /**
     * 判断某个方块是否是一个核心方块
     * @param block
     * @return
     */
    public static PointObject blockIs(Block block) {
        if (block == null) return null;
        for (PointObject object : PointHolder.getPoints()) {
            if (block.getX() == object.getX() && block.getY() == object.getY() && object.getZ() == block.getZ()) {
                return object;
            }
        }
        return null;
    }
}
