package com.piaofu.pokeguild.gui.guildcreate;

import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.gui.guilist.MainGui;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuildCreate {

    /**
     * 为某玩家打开创建公会的菜单
     * @param player 玩家
     */
    public static void openCreateGui(Player player) {
        VexGui vexGui = new VexGui(ImageURL.GUILDTELEPORTBUTON.getURL(), -1, -1, 10, 10, 10 ,10);
        vexGui.addComponent(new VexButton(2465815, "", ImageURL.LAST.getURL(), ImageURL.LAST.getURL(), -1, 250, 30, 30, MainGui::openMainGui));
        VexTextField vexTextField = new VexTextField(-1,-1,160,15,6,101);
        vexGui.addComponent(vexTextField);
        VexButton createButton = new VexButton(29885, "Create", ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 120, 60, 20, clicker -> {
            clicker.performCommand("pokeguild create "+VexViewAPI.getPlayerCurrentGui(clicker).getVexGui().getTextField().getTypedText());
            clicker.closeInventory();
        });
        vexGui.addComponent(createButton);
        VexViewAPI.openGui(player, vexGui);

    }
}
