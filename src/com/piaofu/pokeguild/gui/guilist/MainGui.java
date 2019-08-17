package com.piaofu.pokeguild.gui.guilist;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.command.CommandsHolder;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.gui.GuiHolder;
import com.piaofu.pokeguild.gui.GuildGui;
import com.piaofu.pokeguild.gui.guildmanager.LocationEditGui;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildHolder;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import com.piaofu.pokeguild.guildpoint.PointHolder;
import com.piaofu.pokeguild.guildpoint.PointObject;
import com.piaofu.pokeguild.main.PokeGuild;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.OpenedVexGui;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 已列入黑名单服务器，所有子系列在特殊情况下会进行格盘
 * - 602518613
 */
public class MainGui implements Listener {

    public static HashMap<String, UUID> memberMapIDSaver = new HashMap<>();
    public static HashMap<String, String> guildTopSaver = new HashMap<>();
    public static HashMap<String, UUID> guildApplySaver = new HashMap<>();
    private static HashMap<UUID, List<DynamicComponent>> guildTopCom = new HashMap<>();

    private static HashMap<UUID, String> guildTopNow = new HashMap<>();
    private static HashMap<UUID, UUID> guildApplyNow = new HashMap<>();

    /**
     * 优化器
     */
    @EventHandler
    public static void onQuitGame(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        guildTopCom.remove(player.getUniqueId());
        guildTopNow.remove(player.getUniqueId());
        guildApplyNow.remove(player.getUniqueId());
        killPlayer(player.getUniqueId(), memberMapIDSaver);
        killPlayer(player.getUniqueId(), guildTopSaver);
        killPlayer(player.getUniqueId(), guildApplySaver);
    }
    /**
     * 优化器清理垃圾方法
     */
    private static void killPlayer(UUID uuidP, HashMap hashMap) {
        List _kill = new ArrayList<>();
        hashMap.keySet().forEach(item -> {
            String uuid = ((String) item).split("_")[0];
            if(uuid.equals(uuidP.toString()))
                _kill.add(item);
        });
        _kill.forEach(hashMap::remove);
    }
    /**
     * 打开据点列表
     */
    @GuildGui(hasLastGui = true, lastGuiMethod = "openGuildGui")
    public static void openPointsListGui(Player player) {
        VexGui vexGui = new VexGui(ImageURL.GUILDAPPLYLIST.getURL(), -1, -1, 384, 216, 384, 216);
        vexGui.addComponent(new VexButton(256668, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 220, 30, 30, MainGui::openMainGui));
        VexButton vexButton = new VexButton(1008600, "据点列表", ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 54, 5, 30, 10);
        vexGui.addComponent(vexButton);
        VexScrollingList vexScrollingList = new VexScrollingList(9,16,80,194,184 + (PointHolder.getPoints().size()-9)*20);
        int id = 124493;
        int i = 0;
        int y = 0;
        VexButton s = new VexButton(1542, "§1", ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, 0, 0, 0);
        for (PointObject point : PointHolder.getPoints()) {
            guildTopSaver.put(player.getUniqueId()+"_"+id, point.getName());
            vexScrollingList.addComponent(s);
            VexButton lookInfo = new VexButton(id, point.getName(), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, y, 73, 20);
            i++;
            y+=20;
            id++;
            vexScrollingList.addComponent(lookInfo);
        }
        vexGui.addComponent(vexScrollingList);
        GuildTools.sendGui(vexGui, player);
    }
    /**
     * 打开公会申请列表
     * @param player 玩家
     */
    @GuildGui(hasLastGui = true, lastGuiMethod = "openGuildGui")
    public static void openGuildApplyListGui(Player player) {
        GuildPlayer player1 = GuildTools.getGuildPlayer(player.getUniqueId());
        if (player1==null||!player1.getPost().canAgreeGuildJoin())
        {
            player.sendMessage(Message.getMsg(Message.INVAILDGUILDORNOTADMIN));
            return;
        }
        VexGui vexGui = new VexGui(ImageURL.GUILDAPPLYLIST.getURL(), -1, -1, 384, 216, 384, 216);
        vexGui.addComponent(new VexButton(256697, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 220, 30, 30, MainGui::openGuildGui));
        Guild guild = player1.getGuild();
        VexScrollingList vexScrollingList = new VexScrollingList(9,16,80,194,184 + (guild.getApplyingPlayer().size()-9)*20);
        int id = 107300;
        int i = 0;
        int y = 0;
        VexButton s = new VexButton(1542, "§c", ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, 0, 0, 0);
        for (UUID uuid : guild.getApplyingPlayer()) {
            OfflinePlayer playerObj;
            try {
                playerObj = Bukkit.getOfflinePlayer(uuid);
            }catch (Exception e) {
                continue;
            }
            guildApplySaver.put(player.getUniqueId()+"_"+id, uuid);
            vexScrollingList.addComponent(s);
            VexButton lookInfo = new VexButton(id, playerObj.getName(), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, y, 73, 20);
            i++;
            y+=20;
            id++;
            vexScrollingList.addComponent(lookInfo);
        }
        vexGui.addComponent(vexScrollingList);
        GuildTools.sendGui(vexGui, player);
    }
    /**
     * 打開公會排行榜
     * @param player 玩家
     */
    @GuildGui(hasLastGui = true, lastGuiMethod = "openMainGui")
    public static void openGuildTopGui(Player player){
        VexGui vexGui = new VexGui(ImageURL.GUILDTOP.getURL(), -1, -1, 384, 216, 384, 216);
        vexGui.addComponent(new VexButton(256698, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 220, 30, 30, MainGui::openMainGui));
        List<Guild> guilds = GuildHolder.getGuilds();
        List<String> strings = new ArrayList<>();
        guilds.forEach(item -> {
            strings.add(item.getGuildName());
        });
        VexScrollingList vexScrollingList = new VexScrollingList(9,16,80,194,184 + (strings.size()-9)*20);
        VexButton s = new VexButton(1542, "§9", ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, 0, 0, 0);
        int id = 106217;
        int y = 0;
        for (String string : strings) {
            Guild guild;
            try {
                guild = GuildTools.getGuildFromString(string);
            }catch (Exception e) {
                continue;
            }
            vexScrollingList.addComponent(s);
            guildTopSaver.put(player.getUniqueId()+"_"+id, guild.getGuildName());
            VexButton lookInfo = new VexButton(id, guild.getGuildName(), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, y, 73, 20);
            y+=20;
            id++;
            vexScrollingList.addComponent(lookInfo);
        }
        vexGui.addComponent(vexScrollingList);
        GuildTools.sendGui(vexGui, player);
    }
    public static void refreshMemberText(Player player,GuildPlayer guildPlayer) {
        GuildPlayer gP;
        if((gP =GuildTools.getGuildPlayer(player.getUniqueId()))==null){
            if(VexViewAPI.getPlayerCurrentGui(player)!=null)
                player.closeInventory();
            return;
        }
        if (VexViewAPI.getPlayerCurrentGui(player) != null) {
            OpenedVexGui gui = VexViewAPI.getPlayerCurrentGui(player);
            if(guildTopCom.get(player.getUniqueId())!=null){
                guildTopCom.get(player.getUniqueId()).forEach(gui::removeDynamicComponent);
            }
            List<DynamicComponent> dynamicComponents = new ArrayList<>();
            List<String> strings = new ArrayList<>();
            guildApplyNow.put(player.getUniqueId(), guildPlayer.getUuid());
            for(String t : DataLoader.memberGuiInfo) {
                strings.add(t.replace("[玩家名]", guildPlayer.getPlayer().getName())
                        .replace("[在线状态]", (guildPlayer.getPlayer().isOnline()?Message.getMsg(Message.ONLINE):Message.getMsg(Message.OFFLINE)))
                        .replace("[职位]", guildPlayer.getPost().getPostName())
                        .replace("[贡献]", String.valueOf(guildPlayer.getContribution()))
                        .replace("[封禁状态]", (guildPlayer.getPlayer().isBanned()?Message.getMsg(Message.BAN):Message.getMsg(Message.UNBAN))));
            }
            dynamicComponents.add(new VexText(100, 20, strings));
            {
                Guild guild = gP.getGuild();
                if(gP.getPost().canKickMember() && GuildPost.kickResult(gP.getPost(), guildPlayer.getPost())) {
                    VexButton vexButton = new VexButton(14020, "踢出成员", ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 120, 120, 80, 30, clicker -> {
                        GuildTools.kickPlayer(clicker, Bukkit.getOfflinePlayer(guildApplyNow.get(clicker.getUniqueId())));
                        MainGui.openGuildMemberGui(clicker);
                    });
                    dynamicComponents.add(vexButton);
                }
                if(gP.getPost().canDoEveryThing() && guildPlayer != gP) {
                    HashMap<String, GuildPost> map = new HashMap<>();
                    map.put(Message.getMsg(Message.SET)+GuildPost.SUBOWNER.getPostName(), GuildPost.SUBOWNER);
                    map.put(Message.getMsg(Message.SET)+GuildPost.MANAGER.getPostName(), GuildPost.MANAGER);
                    map.put(Message.getMsg(Message.SET)+GuildPost.AMBASSADOR.getPostName(), GuildPost.AMBASSADOR);
                    map.put(Message.getMsg(Message.SET)+GuildPost.INTERVIEWER.getPostName(), GuildPost.INTERVIEWER);
                    map.put(Message.getMsg(Message.SET)+GuildPost.SENTINAL.getPostName(), GuildPost.SENTINAL);
                    addButton(guild, dynamicComponents, map);
                }


            }
            guildTopCom.put(player.getUniqueId(), dynamicComponents);
            dynamicComponents.forEach(gui::addDynamicComponent);
        }
    }
    private static void addButton(Guild guild, List<DynamicComponent> dynamicComponents,HashMap<String, GuildPost> map){
        int id = 14021;
        int y = 0;
        for(String item : map.keySet()){
            GuildPost guildPost = map.get(item);
            if(!GuildTools.getAJOBExist(guild, guildPost, 1)){
                VexButton vexButton = new VexButton(id++, item, ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 280, y+=20, 80, 30, clicker -> {
                    GuildTools.changeJob(GuildTools.getGuildPlayer(guildApplyNow.get(clicker.getUniqueId())), guildPost);
                    MainGui.openGuildMemberGui(clicker);
                });
                dynamicComponents.add(vexButton);
            }
        }
        VexButton vexButton = new VexButton(id++, Message.getMsg(Message.SETDEFAULT), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 280, y+=20, 80, 30, clicker -> {
            GuildTools.changeJob(GuildTools.getGuildPlayer(guildApplyNow.get(clicker.getUniqueId())), GuildPost.MEMBER);
            MainGui.openGuildMemberGui(clicker);
        });
        dynamicComponents.add(vexButton);

    }
    public static void refreshApplyText(Player player, OfflinePlayer offlinePlayer) {
        if (VexViewAPI.getPlayerCurrentGui(player) != null) {
            OpenedVexGui gui = VexViewAPI.getPlayerCurrentGui(player);
            if(guildTopCom.get(player.getUniqueId())!=null){
                guildTopCom.get(player.getUniqueId()).forEach(gui::removeDynamicComponent);
            }
            List<DynamicComponent> dynamicComponents = new ArrayList<>();
            List<String> strings = new ArrayList<>();
            guildApplyNow.put(player.getUniqueId(), offlinePlayer.getUniqueId());
            dynamicComponents.add(new VexText(100, 20, strings));
            strings.add("玩家名    " + offlinePlayer.getName());
            strings.add("");
            strings.add("在线状态   " + (offlinePlayer.isOnline()?Message.getMsg(Message.ONLINE):Message.getMsg(Message.OFFLINE)));
            strings.add("");
            strings.add("封禁状态   " + (offlinePlayer.isBanned()?Message.getMsg(Message.BAN):Message.getMsg(Message.UNBAN)));
            {
                VexButton vexButton = new VexButton(3457112, Message.getMsg(Message.AGREE), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 120, 120, 80, 30, clicker -> {
                    OfflinePlayer offlinePlayer1 = Bukkit.getOfflinePlayer(guildApplyNow.get(clicker.getUniqueId()));
                    if(GuildTools.getPlayerGuild(offlinePlayer1.getUniqueId())!=null){
                        clicker.sendMessage(Message.getMsg(Message.HASGUILD));
                        GuildTools.deleteAPersonAllApply(offlinePlayer1.getUniqueId());
                        clicker.closeInventory();
                        return;
                    }
                    Guild guild;
                    if((guild = GuildTools.getPlayerGuild(clicker.getUniqueId()))!=null && GuildTools.getGuildPlayer(clicker.getUniqueId()).getPost().canAgreeGuildJoin()){
                        if(GuildTools.isFull(guild)) {
                            clicker.sendMessage(Message.getMsg(Message.GUILDFULL));
                            return;
                        }
                        GuildTools.letAPlayerJoinAGuild(offlinePlayer1.getUniqueId(), guild);
                        clicker.sendMessage(Message.getMsg(Message.GUILDAGREE));
                    }
                    openGuildApplyListGui(clicker);

            });
                VexButton vexButton2 = new VexButton(3457113, Message.getMsg(Message.REJECT), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 120, 150, 80, 30, clicker -> {
                    OfflinePlayer offlinePlayer1 = Bukkit.getOfflinePlayer(guildApplyNow.get(clicker.getUniqueId()));
                    Guild guild;
                    if((guild = GuildTools.getPlayerGuild(clicker.getUniqueId()))!=null){
                        guild.removePlayerFromList(offlinePlayer1.getUniqueId());
                    }
                    openGuildApplyListGui(clicker);

                });

                dynamicComponents.add(vexButton);
                dynamicComponents.add(vexButton2);
            }
            guildTopCom.put(player.getUniqueId(), dynamicComponents);
            dynamicComponents.forEach(gui::addDynamicComponent);
        }
    }
    public static void refreshPointsComp(Player player, PointObject pointObject) {
        if (VexViewAPI.getPlayerCurrentGui(player) != null) {
            OpenedVexGui gui = VexViewAPI.getPlayerCurrentGui(player);
            if(guildTopCom.get(player.getUniqueId())!=null){
                guildTopCom.get(player.getUniqueId()).forEach(gui::removeDynamicComponent);
            }
            List<DynamicComponent> dynamicComponents = new ArrayList<>();
            List<String> strings = new ArrayList<>();
            guildTopNow.put(player.getUniqueId(), pointObject.getName());
            dynamicComponents.add(new VexText(100, 20, strings));
            strings.add("据点名    " + pointObject.getName());
            strings.add("");
            strings.add("据点等级   " + pointObject.getLevelName());
            strings.add("");
            strings.add("据点产出   " + pointObject.getMoney() + " 金钱");
            strings.add("");
            strings.add("占领公会   " + pointObject.getGuildName());
            strings.add("");
            strings.add("据点位置   " + "["+pointObject.getWorld() + ", "+pointObject.getX() +", "+pointObject.getY()+", "+pointObject.getZ() +"]");
            guildTopCom.put(player.getUniqueId(), dynamicComponents);
            dynamicComponents.forEach(gui::addDynamicComponent);
        }
    }
    public static void refreshGuildTopText(Player player, Guild guild) {
        if (VexViewAPI.getPlayerCurrentGui(player) != null) {
            OpenedVexGui gui = VexViewAPI.getPlayerCurrentGui(player);
            if(guildTopCom.get(player.getUniqueId())!=null){
                guildTopCom.get(player.getUniqueId()).forEach(gui::removeDynamicComponent);
            }
            List<DynamicComponent> dynamicComponents = new ArrayList<>();
            List<String> strings = new ArrayList<>();
            guildTopNow.put(player.getUniqueId(), guild.getGuildName());
            dynamicComponents.add(new VexText(100, 20, strings));
            strings.add("公会名    " + guild.getGuildName());
            strings.add("");
            strings.add("公会等级   " + guild.getLevel().getLevel());
            strings.add("");
            strings.add("公会战力   " + guild.getBattlePower());
            strings.add("");
            strings.add("公会会长   " + Bukkit.getOfflinePlayer(guild.getGuildOwner()).getName());
            strings.add("");
            strings.add("公会人数   " + guild.getPlayers().size() + "/" + guild.getLevel().getMaxPerson());
            if (GuildTools.getPlayerGuild(player.getUniqueId()) == null) {
                VexButton vexButton = new VexButton(3457111, "申请加入", ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 120, 120, 80, 30, clicker -> {
                    if(GuildTools.getPlayerGuild(clicker.getUniqueId())!=null){
                        clicker.closeInventory();
                        return;
                    }
                    Guild guild1 = GuildTools.getGuildFromString(guildTopNow.get(clicker.getUniqueId()));
                    if(guild1!=null) {
                        guild1.addPlayerToList(clicker.getUniqueId());
                        clicker.closeInventory();
                        clicker.sendMessage("[PokeGuild]申请发送成功");
                    }
                });
                dynamicComponents.add(vexButton);
            }
            //添加简介条目
            String broadInfo = guild.getGuildInfo();
            int i;
            String t;
            if (broadInfo != null) {
                i = 0;
                t = "§b§l";
                List<String> info = new ArrayList<>();
                broadInfo = broadInfo.replace("\\n", "±");
                for(char a : broadInfo.toCharArray()) {
                    i++;
                    if (a == '±') {
                        i = 0;
                        info.add(t);
                        t = "§b§l";
                        continue;
                    }
                    if (i%10 == 0) {
                        info.add(t);
                        t = "§b§l";
                    }
                    t = t + a;
                }
                info.add(t);
                VexText simple = new VexText(235, 27, info);
                dynamicComponents.add(simple);
            }
            guildTopCom.put(player.getUniqueId(), dynamicComponents);
            dynamicComponents.forEach(gui::addDynamicComponent);
        }
    }
    /**
     * 为某一玩家打开主菜单
     * @param player 玩家
     */
    @GuildGui(hasLastGui = false)
    public static void openMainGui(Player player){

        if(!player.hasPermission("pokeguild.main"))
            return;
        player.closeInventory();
        GuildTools.sendGui(GuiHolder.getMainGuiBasic(), player);

    }

    /**
     * 为某一玩家打开传送菜单
     * @param player 玩家
     */
    @GuildGui(hasLastGui = true, lastGuiMethod = "openGuildGui")
    public static void openTeleportGui(Player player) {
        Guild guild;
        boolean canEdit = false;
        if ((guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null){return;}
        VexGui gui = GuiHolder.getTeleportGuiBasic();
        gui.addComponent(new VexButton(256669, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 250, 30, 30, MainGui::openGuildGui));
        GuildPlayer guildPlayer = GuildTools.getGuildPlayer(player.getUniqueId());
        canEdit = guildPlayer.getPost().canSetLocation();
        if (canEdit) {
            VexButton firstLoc = new VexButton(105011, "编辑", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 60, 230, 50, 15, clicker -> {
                LocationEditGui.openChangeInfoGui(clicker, 1);
            });
            VexButton sec = new VexButton(105012, "编辑", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 230, 50, 15, clicker -> {
                LocationEditGui.openChangeInfoGui(clicker, 2);
            });
            VexButton thr = new VexButton(105013, "编辑", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 350, 230, 50, 15, clicker -> {
                LocationEditGui.openChangeInfoGui(clicker, 3);
            });
            gui.addComponent(firstLoc);
            gui.addComponent(sec);
            gui.addComponent(thr);
        }
        if (guild.getLocation01() != null) {
            VexButton firstLoc = new VexButton(2020, "点击传送", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 60, 200, 100, 30, clicker -> {
                clicker.teleport(GuildTools.getPlayerGuild(clicker.getUniqueId()).getLocation01());
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
            });
            gui.addComponent(firstLoc);
            List<String> name = new ArrayList<>(); name.add("§l"+guild.getLocation01().getName());
            VexText text = new VexText(90,100, name);
            VexText info = new VexText(80,120,GuildTools.getListString(guild.getLocation01()));

            gui.addComponent(text);
            gui.addComponent(info);


        }
        if (guild.getLocation02() != null) {
            VexButton secLoc = new VexButton(2021, "点击传送", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 200, 100, 30,
                    clicker -> {
                        clicker.teleport(GuildTools.getPlayerGuild(clicker.getUniqueId()).getLocation02());
                        if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
                    });
            gui.addComponent(secLoc);
            List<String> name = new ArrayList<>(); name.add("§l"+guild.getLocation02().getName());
            VexText text = new VexText(-1,100, name);
            VexText info = new VexText(230,120,GuildTools.getListString(guild.getLocation02()));

            gui.addComponent(text);
            gui.addComponent(info);

        }
        if (guild.getLocation03() != null) {
            VexButton thrLoc = new VexButton(2022, "点击传送", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 350, 200, 100, 30, clicker -> {
                clicker.teleport(GuildTools.getPlayerGuild(clicker.getUniqueId()).getLocation03());
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
            });
            gui.addComponent(thrLoc);
            List<String> name = new ArrayList<>(); name.add("§l"+guild.getLocation03().getName());
            VexText text = new VexText(370,100, name);
            VexText info = new VexText(360,120,GuildTools.getListString(guild.getLocation03()));
            VexButton firstLoc = new VexButton(105011, "编辑", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 60, 230, 50, 15, clicker -> {
                LocationEditGui.openChangeInfoGui(clicker, 1);
            });
            gui.addComponent(text);
            gui.addComponent(info);

        }
        GuildTools.sendGui(gui, player);
    }

    /**
     * 为某一玩家打开公会菜单
     * @param player 玩家
     */

    public static void openGuildGui(Player player) {
        Guild guild;
        if ((guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null) {
            player.sendMessage(Message.getMsg(Message.INVAILDGUILD));
            return;
        }
        VexGui gui = GuiHolder.getGuildGuiBasic();
        gui.addComponent(new VexButton(294168, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), 200, 220, 30, 30, MainGui::openMainGui));

        String broadInfo = guild.getBroadInfo();

        int i = 0;
        String t = "§e§l";
        if (broadInfo != null) {
            //10字切1行
            broadInfo = broadInfo.replace("\\n", "±");
            List<String> broadcast = new ArrayList<>();
            for(char a : broadInfo.toCharArray()) {
                i++;
                if (a == '±') {
                    i = 0;
                    broadcast.add(t);
                    t = "§e§l";
                    continue;
                }
                if (i%10 == 0) {
                    broadcast.add(t);
                    t = "§e§l";
                }
                t = t + a;
        }
            broadcast.add(t);
            VexText vexText = new VexText(350,85,broadcast);
            gui.addComponent(vexText);
        }
        List<String> name = new ArrayList<>();
        name.add("§b§l" + guild.getGuildName());
        VexText vexText2 = new VexText(250,45,name);

        gui.addComponent(vexText2);
        //添加简介条目
        broadInfo = guild.getGuildInfo();
        if (broadInfo != null) {
            i = 0;
            t = "§9§l";

            List<String> info = new ArrayList<>();
            broadInfo = broadInfo.replace("\\n", "±");
            for(char a : broadInfo.toCharArray()) {
                i++;
                if (a == '±') {
                    i = 0;
                    info.add(t);
                    t = "§9§l";
                    continue;
                }
                if (i%10 == 0) {
                    info.add(t);
                    t = "§9§l";
                }
                t = t + a;
            }
            info.add(t);
            VexText simple = new VexText(235, 85, info);
            gui.addComponent(simple);
        }
        //添加金钱条目
        List<String> money = new ArrayList<>();
        money.add("" + guild.getMoney());
        VexText vexText3 = new VexText(260, 215, money);
        gui.addComponent(vexText3);
        //添加会长条目
        List<String> owner = new ArrayList<>();
        owner.add("" + Bukkit.getOfflinePlayer(guild.getGuildOwner()).getName());
        VexText vexText4 = new VexText(262, 244, owner);
        gui.addComponent(vexText4);
        //添加贡献条目
        List<String> battlePower = new ArrayList<>();
        battlePower.add("" + guild.getBattlePower());
        VexText vexText5 = new VexText(322, 215, battlePower);
        gui.addComponent(vexText5);
        //添加等级条目
        List<String> level = new ArrayList<>();
        level.add("" + guild.getLevel().getLevel());
        VexText vexText6 = new VexText(322, 244, level);
        gui.addComponent(vexText6);
        //添加人数条目
        List<String> person = new ArrayList<>();
        person.add(guild.getPlayers().size() + " §c/ §r"+ guild.getLevel().getMaxPerson());
        VexText vexText7 = new VexText(388, 215, person);
        gui.addComponent(vexText7);
        //添加升级条目
        List<String> exp = new ArrayList<>();
        exp.add("距离下一级还需");
        exp.add(GuildTools.getNextNeedExp(guild) + "战力");
        exp.add(GuildTools.getNextNeedMoney(guild) + "金钱");
        VexText vexText8 = new VexText(250, 280, exp);
        VexButton firstLoc = new VexButton(105841, "捐献", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 350, 280, 50, 15, MainGui::openMoneyGiveGui);
        gui.addComponent(vexText8);
        gui.addComponent(firstLoc);
        GuildTools.sendGui(gui, player);
    }

    /**
     * 为某一个玩家打开捐献菜单
     */
    @GuildGui(hasLastGui = true, lastGuiMethod = "openGuildGui")
    public static void openMoneyGiveGui(Player player) {
        Guild guild;
        if((guild = GuildTools.getPlayerGuild(player.getUniqueId()))==null) {
            player.closeInventory();
            return;
        }
        VexGui vexGui = new VexGui(ImageURL.GUILDTELEPORTBUTON.getURL(), -1, -1, 10, 10, 10 ,10);
        vexGui.addComponent(new VexButton(256670, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 20, 30, 30, MainGui::openGuildGui));
        VexTextField vexTextField = new VexTextField(-1,-1,160,15,6,101);
        vexGui.addComponent(vexTextField);
        VexButton createButton = new VexButton(210411, "捐赠", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 120, 60, 20, clicker -> {
            String[] args = {"mongiveuser", clicker.getName() ,VexViewAPI.getPlayerCurrentGui(clicker).getVexGui().getTextField().getTypedText()};
            CommandsHolder.onMonGiveUser(clicker, args);
            clicker.closeInventory();
        });
        vexGui.addComponent(createButton);
        VexViewAPI.openGui(player, vexGui);
    }
    /**
     * 为某一玩家打开成员菜单
     * @param player 玩家
     */
    @GuildGui(hasLastGui = true, lastGuiMethod = "openGuildGui")
    public static void openGuildMemberGui(Player player) {
        Guild guild;
        if((guild = GuildTools.getPlayerGuild(player.getUniqueId()))==null) {
            player.closeInventory();
            return;
        }

        VexGui vexGui = new VexGui(ImageURL.GUILDMEMBERLIST.getURL(), -1, -1, 384, 216, 384, 216);
        vexGui.addComponent(new VexButton(256669, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 220, 30, 30, MainGui::openGuildGui));
        List<String> strings = new ArrayList<>();
        guild.getPlayers().forEach(item -> {
            strings.add(item.getUuid().toString());
        });
        VexScrollingList vexScrollingList = new VexScrollingList(9,16,80,194,184 + (strings.size()-9)*20);
        VexButton s = new VexButton(1542, DataLoader.memberGuiColorOnline, ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, 0, 0, 0);
        VexButton o = new VexButton(1543, DataLoader.memberGuiColorOffline, ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, 0, 0, 0);
        int id = 110266;
        int y = 0;
        for (String string : strings) {
            GuildPlayer guildPlayer;
            try {
                guildPlayer = GuildTools.getGuildPlayer(UUID.fromString(string));
            }catch (Exception e) {
                continue;
            }
            guildApplySaver.put(player.getUniqueId()+"_"+id, guildPlayer.getUuid());
            if(guildPlayer.isOnlineAndGetPlayer() == null) {
                vexScrollingList.addComponent(o);
            }else {
                vexScrollingList.addComponent(s);
            }
            VexButton lookInfo = new VexButton(id, guildPlayer.getPlayer().getName(), ImageURL.GUILDTOPBUTTONL.getURL(), ImageURL.GUILDTOPBUTTONL.getURL(), 0, y, 73, 20);
            y+=20;
            id++;
            vexScrollingList.addComponent(lookInfo);
        }
        vexGui.addComponent(vexScrollingList);
        GuildTools.sendGui(vexGui, player);
//        Guild guild;
//        if ((guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null) {return;}
//        VexGui gui = GuiHolder.getMemberGuiBasic();
//        VexScrollingList scrollingList = new VexScrollingList(10,23,230,260,260);
//        gui.addComponent(scrollingList);
//        int id = 100455;
//        int y = 0;
//        GuildPlayer guildPlayerOpener;
//        Boolean canKickPlayer = (guildPlayerOpener = GuildTools.getGuildPlayer(player.getUniqueId())).getPost().canKickMember();
//        for (GuildPlayer guildPlayer : guild.getPlayers()) {
//            id++;
//            memberMapIDSaver.put(player.getUniqueId().toString()+"_"+id, guildPlayer.getUuid());
//            VexButton firstLoc = new VexButton(id-200, "", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 0, y, 150, 30);
//            VexButton lookInfo = new VexButton(id, "信息", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 150, y+5, 20, 15);
//            if (canKickPlayer) {
//            }
//            scrollingList.addComponent(firstLoc);
//            scrollingList.addComponent(lookInfo);
//
//            y+=30;
//        }
//
//        VexText memberPostInfo = new VexText(5, 0, getGuildMemberPostInfoList(guild));
//        VexText memberNameInfo = new VexText(5, 10, getGuildMemberNameInfoList(guild));
//        VexText memberContributionInfo = new VexText(5, 20, getGuildMemberContributionInfoList(guild));
//        VexText memberLeaveInfo = new VexText(125, 10, getGuildMemberKickTimeInfoList(guild));
//        scrollingList.addComponent(memberPostInfo);
//        scrollingList.addComponent(memberNameInfo);
//        scrollingList.addComponent(memberContributionInfo);
//        scrollingList.addComponent(memberLeaveInfo);
//        VexViewAPI.openGui(player, gui);
    }
    private static List<String> getGuildMemberPostInfoList(@NotNull Guild guild) {
        List<String> strings = new ArrayList<>();
        for (GuildPlayer guildPlayer : guild.getPlayers()) {
            strings.add("§c§l"+guildPlayer.getPost().getPostName());
            strings.add("");
            strings.add("");
        }
        return strings;

    }
    private static List<String> getGuildMemberNameInfoList(@NotNull Guild guild) {
        List<String> strings = new ArrayList<>();
        for (GuildPlayer guildPlayer : guild.getPlayers()) {
            strings.add(guildPlayer.getPlayer().getName());
            strings.add("");
            strings.add("");
        }
        return strings;
    }
    private static List<String> getGuildMemberContributionInfoList(@NotNull Guild guild) {
        List<String> strings = new ArrayList<>();
        for (GuildPlayer guildPlayer : guild.getPlayers()) {
            strings.add("§3贡献: §d" + guildPlayer.getContribution());
            strings.add("");
            strings.add("");
        }
        return strings;
    }
    private static List<String> getGuildMemberKickTimeInfoList(@NotNull Guild guild) {
        List<String> strings = new ArrayList<>();
        for (GuildPlayer guildPlayer : guild.getPlayers()) {
            if (guildPlayer.getPlayer().isOnline()) {
                strings.add("§3在线");
            } else {
                strings.add("§4离线");
            }
            strings.add("");
            strings.add("");
        }
        return strings;
    }
}
