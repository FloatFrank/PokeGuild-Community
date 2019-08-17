package com.piaofu.pokeguild.gui.guildmanager;

import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.guild.GuildTools;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.entity.Player;

public class GuildChangeInfo {
    /**
     * 为某一玩家打开改变信息或公告的GUI
     * @param player 玩家
     * @param way (way == 0 : 改公告， way == 1 : 改简介)
     */
    public static void openChangeInfoGui(Player player, int way) {
        if (GuildTools.getPlayerGuild(player.getUniqueId()) == null) return;
        VexGui vexGui = new VexGui(ImageURL.GUILDTELEPORTBUTON.getURL(), -1, -1, 10, 10, 10 ,10);
        VexTextField vexTextField = new VexTextField(-1,-1,600,20,80,101);
        if (way == 0) {
            vexGui.addComponent(vexTextField);
            VexButton createButton = new VexButton(35221, "Change", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 120, 60, 20, clicker -> {
                clicker.performCommand("pokeguild cbc "+ VexViewAPI.getPlayerCurrentGui(clicker).getVexGui().getTextField().getTypedText());
                clicker.closeInventory();
            });
            vexGui.addComponent(createButton);
        }else {
            vexGui.addComponent(vexTextField);
            VexButton createButton = new VexButton(35222, "Change", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 120, 60, 20, clicker -> {
                clicker.performCommand("pokeguild ci "+ VexViewAPI.getPlayerCurrentGui(clicker).getVexGui().getTextField().getTypedText());
                clicker.closeInventory();
            });
            vexGui.addComponent(createButton);
        }
        VexViewAPI.openGui(player, vexGui);

    }
}
