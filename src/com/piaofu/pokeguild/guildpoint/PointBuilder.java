package com.piaofu.pokeguild.guildpoint;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.main.PokeGuild;
import com.piaofu.pokeguild.particle.Particle;
import lk.vexview.api.VexViewAPI;
import lk.vexview.hud.VexTextShow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class PointBuilder {
    private static PointYAML pointYAML;

    public static PointYAML getPointYAML() {
        return pointYAML;
    }

    public static void build() {
        pointYAML.getConfigurationSection("points").getKeys(false)
                .forEach(item ->
                {
                    try {
                        new PointObject(pointYAML.getWorld(item), pointYAML.getName(item), pointYAML.getX(item), pointYAML.getY(item), pointYAML.getZ(item),pointYAML.getLevel(item), pointYAML.getGuild(item), item).addToList();
                    }catch (Exception e) {
                        Bukkit.getLogger().warning("据点" + item + "配置出错，请认真检查");
                        e.printStackTrace();
                    }
                });
    }
    public static void rebuildPoint() {
        PointHolder.getPoints().clear();
        pointYAML = new PointYAML();
        build();
        timeBuilder();
    }
    /**
     * Point据点功能核心启动方法， 不可或缺
     */
    public static void buildAllPoint() {
        pointYAML = new PointYAML();
        build();
        startPointTimetask();
        Bukkit.getPluginManager().registerEvents(new PointListener(), PokeGuild.plugin);
        startGiftTimetask();
        startMsgRefresher();
        if(DataLoader.getEffectSwitch())
            startParticle();
        timeBuilder();
        startPointBattleTask();
    }
    private    static List<String> removeDuplicate(List list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).equals(list.get(i)))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    /**
     * 开启据点战开关调度改变器
     */
    private static void startPointBattleTask() {
        new BukkitRunnable() {

            @Override
            public void run() {
                if(PointHolder.nowTime == null) {
                    PointTime pointTime;
                    if((pointTime = PointTools.getNowPoint()) != null) {
                        PointHolder.nowTime = pointTime;
                        PointListener.canBreak = true;
                        PointTools.clearAllPointAttackMap();
                        Bukkit.broadcastMessage(Message.getMsg(Message.POINT_BATTLE_START));
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            VexViewAPI.sendHUD(player, new VexTextShow(4125, -1 , 40, PointYAML.stringA, 0));
                        }
                    }
                }else{
                    if(!PointHolder.nowTime.isIn(PointTools.getWHH())) {
                        PointHolder.nowTime = null;
                        PointListener.canBreak = false;
                        Bukkit.broadcastMessage(Message.getMsg(Message.POINT_BATTLE_END));
                        PointTools.clearAllPointAttackMap();
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            VexViewAPI.sendHUD(player, new VexTextShow(4125, -1 , 40, PointYAML.strings, 0));
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(PokeGuild.plugin, 20, 20*60);
    }
    /**
     *  - '1_20_21'
     *  - '3_20_21'
     *  - '5_20_21'
     *  - '7_20_21'
     */
    private static void timeBuilder() {
        PointHolder.getTimes().clear();
        for (String item : removeDuplicate(pointYAML.getTimeList())) {
            String[] strings = item.split("_");
            if(strings.length != 3) {
                Bukkit.getConsoleSender().sendMessage("§4|>> 据点占领时间" + item + "配置错误" + "原因: 参数长度错误");
                continue;
            }
            int w, h, m;
            try {
                if((w = Integer.valueOf(strings[0])) > 7 || w < 1) {
                    Bukkit.getConsoleSender().sendMessage("§4|>> 据点占领时间" + item + "配置错误" + "原因: 参数星期超出上限，范围需在[1-7]");
                    continue;
                }
                if((h = Integer.valueOf(strings[1])) > 23 || h < 0) {
                    Bukkit.getConsoleSender().sendMessage("§4|>> 据点占领时间" + item + "配置错误" + "原因: 参数开始时间超出上限，范围需在[0-23]");
                    continue;
                }
                if((m = Integer.valueOf(strings[2])) > 23 || m < 0) {
                    Bukkit.getConsoleSender().sendMessage("§4|>> 据点占领时间" + item + "配置错误" + "原因: 参数结束时间超出上限，范围需在[0-23]");
                    continue;
                }
            }catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§4|>> 据点占领时间" + item + "配置错误" + "原因: 你TM是把数字填成什么了，骚货");
                continue;
            }
            if(h >= m) {
                Bukkit.getConsoleSender().sendMessage("§4|>> 据点占领时间" + item + "配置错误" + "原因: 开始时间必须< 结束时间");
                continue;
            }
            new PointTime(w,h,m).addToList();
            Bukkit.getConsoleSender().sendMessage("§b|> §r[据点时间段读取成功] 星期."+ w + "["+h+"-"+m+"]");
        }
    }
    /**
     * 开启给与奖励的调度器
     */
    private static void startGiftTimetask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                PointHolder.getPoints().forEach(item -> {
                    Guild guild;
                    if((guild = item.getGuild()) != null) {
                        GuildTools.giveMoney(guild, item.getMoney());
                    }
                });
            }
        }.runTaskTimerAsynchronously(PokeGuild.plugin, 10, 20*60);
    }

    /**
     * 开启检测玩家到达某一区域的调度器
     */
    private static void startPointTimetask() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PointTools.isInAPoint(p);
                }
            }
        }.runTaskTimerAsynchronously(PokeGuild.plugin, 100, 5);
    }

    /**
     * 开启消息刷新器
     */
    private static void startMsgRefresher() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for(UUID uuid : PointTools.inPointMap.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if(player == null) {
                        PointTools.inPointMap.remove(uuid);
                        continue;
                    }
                    PointObject point = PointTools.inPointMap.get(uuid);
                    if(point == null) {
                        continue;
                    }
                    PointShow.sendAll(player, PointTools.inPointMap.get(uuid));
                }
            }
        }.runTaskTimerAsynchronously(PokeGuild.plugin, 200, 20);
    }

    /**
     * 开启特效刷新器
     */
    private static void startParticle() {
        new BukkitRunnable() {

            @Override
            public void run() {
                PointHolder.getPoints().forEach(item-> {
                    Location location = item.getLocation();
                    Particle.showPointBoom(location, DataLoader.getEffectString());
                });
            }
        }.runTaskTimerAsynchronously(PokeGuild.plugin, 200, 60);
    }
}
