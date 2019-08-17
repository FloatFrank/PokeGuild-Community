package com.piaofu.pokeguild.gui.guildmanager;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.gui.guilist.MainGui;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.entity.Player;

public class GuildManager {
    public static void openManager(Player player) {
        if (GuildTools.getPlayerGuild(player.getUniqueId()) == null) return;
        VexGui vexGui = new VexGui(ImageURL.GUILDTELEPORTBUTON.getURL(), -1, -1, 315/2, 387/2, 315/2 ,387/2);
        vexGui.addComponent(new VexButton(25434158, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 250, 30, 30, MainGui::openGuildGui));
        GuildPlayer play = GuildTools.getGuildPlayer(player.getUniqueId());
        GuildPost guildPost = play.getPost();
        if(guildPost.canChangeBroadcastAndInfo()) {

            vexGui.addComponent(new VexButton(52301, Message.getMsg(Message.CHANGEBROADCAST), ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 10, 60, 20, clicker -> {
                GuildChangeInfo.openChangeInfoGui(clicker, 0);
            }));
            vexGui.addComponent(new VexButton(52304, Message.getMsg(Message.CHANGEINFO), ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 40, 60, 20, clicker -> {
                GuildChangeInfo.openChangeInfoGui(clicker, 1);
            }));
        }
        if(guildPost.canAgreeGuildJoin()) {
            vexGui.addComponent(new VexButton(52302, Message.getMsg(Message.APPLY_LIST), ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 70, 60, 20, MainGui::openGuildApplyListGui));
        }
        if(guildPost.equals(GuildPost.OWNER)) {
            vexGui.addComponent(new VexButton(52303, Message.getMsg(Message.KILL_GUILD), ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 100, 60, 20, clicker -> {
                GuildTools.deleteGuild(GuildTools.getPlayerGuild(clicker.getUniqueId()));
                clicker.closeInventory();
            }));
        }else{
            vexGui.addComponent(new VexButton(52305, Message.getMsg(Message.EXIT_GUILD), ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 100, 60, 20, clicker -> {
                GuildTools.exitGuild(GuildTools.getGuildPlayer(clicker.getUniqueId()));
                clicker.closeInventory();
            }));
        }

        VexViewAPI.openGui(player, vexGui);

    }
}
