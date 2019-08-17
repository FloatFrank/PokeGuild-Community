package com.piaofu.pokeguild.gui.guildchat;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.gui.GuiHolder;
import com.piaofu.pokeguild.gui.guilist.MainGui;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.OpenedVexGui;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexScrollingList;
import lk.vexview.gui.components.VexText;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class ChatGui{
    private List<String> message;
    private Guild guild;
    public ChatGui(Guild guild) {
        this.guild = guild;
        message = new ArrayList<>();
        message.add("");
    }
    private int getLong() {
        if (message.size() < 20) {
            return 250;
        }
        return 250 + (message.size() - 20) * 10;
    }

    /**
     * 为某一为玩家打开公会的聊天窗口
     * @param player 玩家
     */
    public void openGui(Player player, boolean canEdit) {
        VexGui vexGui = GuiHolder.getChatGuiBasic();
        vexGui.addComponent(new VexButton(2485612, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 250, 30, 30, MainGui::openGuildGui));

        if(canEdit) {
            VexTextField textField = new VexTextField(13,-1-20,512-60,20,60,5);
            vexGui.addComponent(textField);
            VexButton sendMessageButton = new VexButton(2920, "Send", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), 470, -1-20, 35, 20, clicker -> {
                try {
                    Guild guild;
                    if ((guild = GuildTools.getPlayerGuild(clicker.getUniqueId())) == null) {
                        clicker.closeInventory();
                        return;
                    }

                    guild.getChatGui().addMessage(clicker, VexViewAPI.getPlayerCurrentGui(clicker).getVexGui().getTextField().getTypedText());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            vexGui.addComponent(sendMessageButton);
        }

        VexScrollingList textSL = new VexScrollingList(10, 20, 480, 245, getLong());
        VexText msgText = new VexText(20, 20, message);
        textSL.addComponent(msgText);
        vexGui.addComponent(textSL);
        List<String> titles = new ArrayList<>();
        titles.add(Message.getMsg(Message.CHATROOMTITLE, guild.getGuildName()));
        VexText title = new VexText(-1, 10, titles);
        vexGui.addComponent(title);
        VexViewAPI.openGui(player, vexGui);
    }

    /**
     * 为某一玩家所在的公会添加聊天信息
     * @param player 玩家
     * @param msg 消息
     */
    public void addMessage(Player player, String msg) {
        String overString = Message.getMsg(Message.CHATROOMFORMAT, getJob(player.getUniqueId()), player.getName(), msg);
        message.add(0, overString);
        if (message.size() >= 100) {
            message.remove(99);
        }
        addMessage();
        refreshMessage(player);
        player.closeInventory();
        openGui(player, true);
    }
    private String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }
    private void addMessage() {
        String overString = getDateTime();
        message.add(0, overString);
        if (message.size() >= 100) {
            message.remove(99);
        }
    }
    public String getJob(UUID uuid) {
        return GuildTools.getGuildPlayer(uuid).getPost().getPostName();
    }
    private void refreshMessage(Player sender) {
        for (GuildPlayer guildPlayer : guild.getPlayers()) {
            if (!Bukkit.getOfflinePlayer(guildPlayer.getUuid()).isOnline()) {
                continue;
            }
            if (guildPlayer.getUuid().equals(sender.getUniqueId())) {
                continue;
            }
            Player player = Bukkit.getOfflinePlayer(guildPlayer.getUuid()).getPlayer();
            OpenedVexGui openedVexGui = VexViewAPI.getPlayerCurrentGui(player);
            if (openedVexGui == null)
                return;
            if (openedVexGui.getVexGui().getButtonById(2920) == null)
                return;
            openedVexGui.getVexGui().getTextField().getTypedText();
            player.closeInventory();
            openGui(player, true);
        }
    }
}
