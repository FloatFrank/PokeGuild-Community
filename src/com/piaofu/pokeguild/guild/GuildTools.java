package com.piaofu.pokeguild.guild;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.api.guildevent.*;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.gui.GuiHolder;
import com.piaofu.pokeguild.guild.guildlevel.LevelHolder;
import com.piaofu.pokeguild.guild.locationplus.GuildLocation;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import com.piaofu.pokeguild.main.PokeGuild;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.crypto.Data;
import java.util.*;

public class GuildTools {
    private static int maxLevel = PokeGuild.plugin.getConfig().getInt("MAX_LEVEL");
    public static void letLevelUp(Guild guild) {
        while(canLevelUp(guild)) {
            guild.setMoney(guild.getMoney() - guild.getLevel().getNeedMoney());
            guild.setLevel(LevelHolder.get(guild.getLevel().getLevel()+1));
            Bukkit.broadcastMessage(Message.getMsg(Message.GUILD_LEVEL_UP,guild.getGuildName() , String.valueOf(guild.getLevel().getLevel())));
        }

    }
    public static boolean hasPlayer(Guild guild, UUID p) {
        for (GuildPlayer item : guild.getPlayers()) {
            if (item.getUuid().equals(p)) {
                return true;
            }
        }
        return false;
    }
    public static GuildPlayer getGuildPlayerByGuild(Guild guild, UUID p) {
        for (GuildPlayer item : guild.getPlayers()) {
            if (item.getUuid().equals(p)) {
                return item;
            }
        }
        return null;
    }
    /**
     * 获得还剩多少金钱升级
     */
    public static String getNextNeedMoney(Guild guild) {
        int level = guild.getLevel().getLevel();
        if(level >= maxLevel)
            return "MAX";
        int needMoney = guild.getLevel().getNeedMoney();
        int money = guild.getMoney();
        return String.valueOf((needMoney - money) > 0 ? (needMoney - money) : 0);
    }
    /**
     * 获得还剩多少经验升级
     */
    public static String getNextNeedExp(Guild guild) {
        int level = guild.getLevel().getLevel();
        if(level >= maxLevel)
            return "MAX";
        int needPower = guild.getLevel().getNeedBattlePower();
        int power = guild.getBattlePower();
        return String.valueOf((needPower - power) > 0 ? (needPower - power) : 0);
    }

    /**
     * 给与经验给公会
     */
    public static void giveCon(Guild guild, int num, UUID player) {
        GuildPlayer guildPlayer = getGuildPlayer(player);
        GiveContrationEvent giveContrationEvent = new GiveContrationEvent(guildPlayer, num);
        Bukkit.getPluginManager().callEvent(giveContrationEvent);
        if(giveContrationEvent.isCancelled()) {
            return;
        }
        guild.setBattlePower(guild.getBattlePower() + num);
        Objects.requireNonNull(guildPlayer).addContribution(num);
        guild.sendMessage(Message.getMsg(Message.PLAYER_ADD_GUILD_CON, guildPlayer.getPlayer().getName(), String.valueOf(num)));
        letLevelUp(guild);
        guild.firstSave();
    }
    public static void giveCon(Guild guild, int num) {
        guild.setBattlePower(guild.getBattlePower() + num);
        guild.sendMessage(Message.getMsg(Message.ADD_GUILD_CON, String.valueOf(num)));
        letLevelUp(guild);
        guild.firstSave();
    }
    /**
     * 给与金钱给公会
     */
    public static void giveMoney(Guild guild, int num) {
        guild.addMoney(num);
        letLevelUp(guild);
    }
    public static boolean giveMoney(int num, OfflinePlayer player, CommandSender sender) {
        Guild guild;
        if((guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null) {
            guild.sendMessage(Message.getMsg(Message.NONE_GUILD));
            return false;
        }
        if (num * DataLoader.getMoneyRate() < 1.0) {
            sender.sendMessage(Message.getMsg(Message.CANNOT_ADD_CON, String.valueOf(1.0 / DataLoader.getMoneyRate())));
            return false;
        }
        if (!PokeGuild.plugin.economy.has(player, num)) {
            guild.sendMessage(Message.getMsg(Message.NONE_MONEY));
            return false;
        }
        PokeGuild.plugin.economy.withdrawPlayer(player, num);
        guild.addMoney(num);
        int addNum = (int) (num * DataLoader.getMoneyRate());
        GuildTools.getGuildPlayer(player.getUniqueId()).addContribution(addNum);
        guild.sendMessage(Message.getMsg(Message.ADD_MONEY, player.getName(), String.valueOf(num) , String.valueOf(addNum)));
        letLevelUp(guild);
        return true;
    }
    /**
     * 判断一个公会是否能升级并返回boolean值
     */
    public static boolean canLevelUp(Guild guild) {
        int level = guild.getLevel().getLevel();
        int power = guild.getBattlePower();
        int money = guild.getMoney();
        int needPower = guild.getLevel().getNeedBattlePower();
        int needMoney = guild.getLevel().getNeedMoney();
        if(level >= maxLevel)
            return false;
        if(power < needPower)
            return false;
        if(money < needMoney){
            return false;
        }

        return true;
    }

    /**
     * 玩家自主退出公会方法
     */
    public static void exitGuild(GuildPlayer player) {
        if(player!=null)
        if(!player.getPost().canDoEveryThing()){
            player.getGuild().sendMessage(Message.getMsg(Message.PLAYER_EXIT_GUILD_INFO, player.getPlayer().getName()));
            player.getGuild().removePlayer(player);
        }
    }
    /**
     * 将一个玩家移动到公会的某个职位
     */
    public static void changeJob(GuildPlayer guildPlayer, GuildPost guildPost) {
        ChangeJobEvent event = new ChangeJobEvent(guildPlayer, guildPost, guildPlayer.getPost());
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if(guildPost.equals(GuildPost.MEMBER)) {
            guildPlayer.setPost(guildPost);
            return;
        }
        if(!getAJOBExist(guildPlayer.getGuild(), guildPost, 1))
            guildPlayer.setPost(guildPost);

        guildPlayer.getGuild().onlySavePlayers();
    }
    /**
     * 判断一个公会是否已经有了某个职位并返回结果
     */
    public static boolean getAJOBExist(Guild guild, GuildPost guildPost, int num) {
        int sum = 0;
        for(GuildPlayer item : guild.getPlayers()) {
            if (item.getPost().equals(guildPost)) {
                sum++;
            }
        }
        if(sum<num)
            return false;
        else
            return true;
    }
    /**
     * 将一个玩家移动到某个公会的申请列表里
     * @param uuid 玩家
     * @param guild 公会
     */
    public static void moveAPlayerToGuildList(UUID uuid, Guild guild) {
        Guild guildU = GuildTools.getPlayerGuild(uuid);
        if(guildU!=null)
            return;
        guild.addPlayerToList(uuid);
    }

    /**
     * 将一个玩家从某个公会申请列表移出
     * @param uuid 玩家
     * @param guild 公会
     */
    public static void removeAPlayerFromGuildList(UUID uuid, Guild guild) {
        guild.removePlayerFromList(uuid);
    }
    /**
     * 通过UUID获取到某个玩家所在的公会
     * @param uuid UUID
     * @return 公会
     */
    @Nullable
    public static Guild getPlayerGuild(@NotNull UUID uuid){
        for (Guild guild : GuildHolder.getGuilds()) {
            for (GuildPlayer tt : guild.getPlayers()) {
                if (tt.getUuid().equals(uuid))
                    return guild;
            }
        }
        return null;
    }
    /**
     * 通过UUID获取到某个公会玩家对象
     * @param uuid UUID
     * @return 玩家
     */
    @Nullable
    public static GuildPlayer getGuildPlayer(UUID uuid) {
        if(getPlayerGuild(uuid)==null)
            return null;
        for (GuildPlayer player : getPlayerGuild(uuid).getPlayers()) {
            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    /**
     * 清除所有公会的有关一个人的申请
     */
    public static void deleteAPersonAllApply(UUID uuid) {
        HashMap<Guild, UUID> map = new HashMap<>();
        GuildHolder.getGuilds().forEach(item -> {
            if(item.getApplyingPlayer()!=null)
            item.getApplyingPlayer().forEach(p -> {
                if(p.equals(uuid)) {
                    map.put(item, p);
                }
            });
        });
        for(Guild guild : map.keySet()) {
            guild.getApplyingPlayer().remove(map.get(guild));
            guild.onlySaveApplyList();
        }

    }
    /**
     * 通过公会地址返回二维字符串
     * @param guildLocation 公会地址
     * @return
     */
    public static List<String> getListString(@NotNull GuildLocation guildLocation) {
        List<String> list = new ArrayList<>();
        list.add("§b§lX:     §3§l"+guildLocation.getBlockX());
        list.add("§b§lY:     §3§l"+guildLocation.getBlockY());
        list.add("§b§lZ:     §3§l"+guildLocation.getBlockZ());
        return list;
    }
    /**
     * 将某一玩家加入到某一公会 (外界使用不安全)
     */
    public static void letAPlayerJoinAGuild(UUID uuid, Guild guild) {
        GuildPlayer guildPlayer = new GuildPlayer(0, uuid, GuildPost.MEMBER, guild);
        guild.getPlayers().add(guildPlayer);
        guild.onlySavePlayers();
        deleteAPersonAllApply(uuid);
        guild.sendMessage(Message.getMsg(Message.GUILD_WELCOME_MESSAGE, guildPlayer.getPlayer().getName()));
    }

    /**
     * 通过公会名获取公会对象
     * @param string 公会名
     * @return
     */
    @Nullable
    public static Guild getGuildFromString(String string) {
        for (Guild guild : GuildHolder.getGuilds()) {
            if (guild.getGuildName().equals(string))
                return guild;
        }
        return null;
    }
    public static boolean isFull(Guild guild) {
        return guild.getPlayers().size() >= guild.getLevel().getMaxPerson();
    }

    /**
     * 将某玩家直接从对应的公会踢出
     * @param kicked 被踢出玩家
     */
    private static void removePlayerFromGuild(@NotNull OfflinePlayer kicked) {
        getGuildPlayer(kicked.getUniqueId()).remove();
    }
    /**
     * 延时发送GUI界面
     */
    public static void sendGui(VexGui vexGui, Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if(player == null || !player.isOnline())
                    return;
                VexViewAPI.openGui(player, vexGui);
            }
        }.runTaskLater(PokeGuild.plugin, 5);
    }

    /**
     * 以某人的名义将某个玩家从他们的公会移除
     * @param kicker 踢出者
     * @param kicked 被踢出者
     */
    public static void kickPlayer(@NotNull Player kicker, @NotNull OfflinePlayer kicked) {
        GuildPlayer kickerP;
        GuildPlayer kickedP;

        if ((kickerP = getGuildPlayer(kicker.getUniqueId())) == null) {
            return;
        }
        if ((kickedP = getGuildPlayer(kicked.getUniqueId())) == null) {
            return;
        }
        if (!getGuildPlayer(kicker.getUniqueId()).getPost().canKickMember()) {
            kicker.sendMessage(Message.getMsg(Message.CANNOT_KICK_MESSAGE));
            return;
        }
        if (GuildPost.kickResult(kickerP.getPost(), kickedP.getPost())) {
            KickPlayerEvent doKick = new KickPlayerEvent(kickerP, kickedP);
            Bukkit.getServer().getPluginManager().callEvent(doKick);
            if(!doKick.isCancelled()){
                removePlayerFromGuild(kicked);
                kicker.sendMessage(Message.getMsg(Message.SUCCESS_KICK_MESSAGE));
            }
            kicker.closeInventory();
            return;
        }
        kicker.sendMessage(Message.getMsg(Message.CANNOT_KICK_MORE_MESSAGE));
        return;
    }

    /**
     * 以某人的名义改变公会的信息
     * @param guild 公会
     * @param player 玩家
     * @param newInfo 新信息
     */
    public static void changeGuildInfo(Guild guild, GuildPlayer player, String newInfo) {
        if (guild == null) return;
        if (!player.getPost().canChangeBroadcastAndInfo()) {
            player.getPlayer().getPlayer().sendMessage(Message.getMsg(Message.CANNOT_KICK_MESSAGE));
            return;
        }
        ChangeInfomationEvent event = new ChangeInfomationEvent(player, guild.getBroadInfo(), newInfo);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            guild.setGuildInfo(newInfo);
            guild.onlySaveInfo();
            if (player.getPlayer().getPlayer().isOnline()) {
                player.getPlayer().getPlayer().sendMessage(Message.getMsg(Message.EDIT_SUCCESS));
            }
        }
    }

    /**
     * 以某人的名义改变公会的公告
     * @param guild 公会
     * @param player 玩家
     * @param newInfo 新信息
     */
    public static void changeBroadInfo(Guild guild, GuildPlayer player, String newInfo) {
        if (guild == null) return;
        if (!player.getPost().canChangeBroadcastAndInfo()) {
            player.getPlayer().getPlayer().sendMessage(Message.getMsg(Message.CANNOT_KICK_MESSAGE));
        }
        ChangeBroadcastEvent event = new ChangeBroadcastEvent(player, guild.getBroadInfo(), newInfo);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            guild.setBroadInfo(newInfo);
            guild.onlySaveBroadcast();
            if (player.getPlayer().getPlayer().isOnline()) {
                player.getPlayer().getPlayer().sendMessage(Message.getMsg(Message.EDIT_SUCCESS));
            }
        }
    }

    /**
     * 解散公会
     *
     */
    public static void deleteGuild(Guild guild) {
        if (guild!=null) {
            guild.removeGuild();
        }
    }
    /**
     * 新增公会的某个传送点
     * @param player 玩家
     * @param loc 第几个点
     * @param name 名字
     */
    public static void addLocation(Player player, int loc, String name) {
        Guild guild;
        if ((guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null ) {
            return;
        }
        GuildPlayer guildPlayer = getGuildPlayer(player.getUniqueId());
        if (!guildPlayer.getPost().canSetLocation())
            return;

        Location location =  player.getLocation();
        GuildLocation guildLocation = new GuildLocation(player.getWorld(),location.getX(), location.getY(), location.getZ(), name);
        ChangeLocationEvent event = new ChangeLocationEvent(guildPlayer, guildLocation);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()) {
            return;
        }
        switch (loc) {
            case 1 : {
                guild.setLocation01(guildLocation);
                break;
            }
            case 2 : {
                guild.setLocation02(guildLocation);
                break;
            }
            case 3 : {
                guild.setLocation03(guildLocation);
                break;
            }
        }
    }

}
