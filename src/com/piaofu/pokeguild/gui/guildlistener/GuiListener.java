package com.piaofu.pokeguild.gui.guildlistener;

import com.piaofu.pokeguild.gui.guilist.MainGui;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guildpoint.PointObject;
import com.piaofu.pokeguild.guildpoint.PointTools;
import com.piaofu.pokeguild.spring.Listener;
import lk.vexview.event.ButtonClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class GuiListener extends Listener {
    /**
     * 点击据点按钮处理事件
     */
    @EventHandler
    public static void onClickPointsGuiButton(ButtonClickEvent event) {
        int id = (int)event.getButtonID();
        if (124493 > id || id > 124693)
            return;
        PointObject point;
        Player player = event.getPlayer();
        if((point = PointTools.getPointFromString(MainGui.guildTopSaver.get(player.getUniqueId()+"_" +id))) == null){
            player.closeInventory();
            return;
        }
        MainGui.refreshPointsComp(player, point);
    }
    /**
     * 点击成员按钮处理事件
     * @param event 事件
     */
    @EventHandler
    public static void onClickMemberGuiButton(ButtonClickEvent event) {
        int id = (int)event.getButtonID();
        if (100456 <= id && id < 100656) {

            //查看信息按钮
            Player player = event.getPlayer();
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(MainGui.memberMapIDSaver.get(player.getUniqueId() +"_" + id));

        }
        if (100656 <= id && (int)event.getButtonID() < 100856) {
            //踢出队伍按钮
            Player player = event.getPlayer();
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(MainGui.memberMapIDSaver.get(player.getUniqueId() +"_" + (id-200)));
            GuildTools.kickPlayer(player, targetPlayer);
        }
    }

    /**
     * 点击Top按钮处理事件
     */
    @EventHandler
    public static void onClickButton(ButtonClickEvent event) {
        int id = (int)event.getButtonID();
        if(!(id>106216 && id<107216))
            return;
        Guild guild;
        Player player = event.getPlayer();
        if((guild = GuildTools.getGuildFromString(MainGui.guildTopSaver.get(player.getUniqueId()+"_" +id))) == null){
            player.closeInventory();
            return;
        }
        MainGui.refreshGuildTopText(player, guild);
    }
    /**
     * 点击Apply按钮处理事件
     */
    @EventHandler
    public static void onClickApplyButton(ButtonClickEvent event) {
        int id = (int)event.getButtonID();
        if(!(id>107299 && id<108300))
            return;
        OfflinePlayer offlinePlayer;
        Player player = event.getPlayer();
        if((offlinePlayer = Bukkit.getOfflinePlayer(MainGui.guildApplySaver.get(player.getUniqueId()+"_" +id))) == null){
            player.closeInventory();
            return;
        }
        MainGui.refreshApplyText(player, offlinePlayer);
    }
    /**
     * 点击PlayerMember按钮处理事件
     */
    @EventHandler
    public static void onClickMemberButton(ButtonClickEvent event) {
        int id = (int)event.getButtonID();
        if(!(id>110265 && id<110365))
            return;
        GuildPlayer guildPlayer;
        Player player = event.getPlayer();
        if((guildPlayer = GuildTools.getGuildPlayer(MainGui.guildApplySaver.get(player.getUniqueId()+"_" +id))) == null){
            player.closeInventory();
            return;
        }
        MainGui.refreshMemberText(player, guildPlayer);
    }
}
