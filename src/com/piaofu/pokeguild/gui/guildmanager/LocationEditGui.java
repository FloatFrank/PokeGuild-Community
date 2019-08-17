package com.piaofu.pokeguild.gui.guildmanager;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class LocationEditGui {
    private static HashMap<UUID, Integer> telemap = new HashMap<>();

    public static void openChangeInfoGui(Player player, int loc) {
        telemap.put(player.getUniqueId(), loc);
        if ((GuildTools.getPlayerGuild(player.getUniqueId())) == null) return;
        GuildPlayer guildPlayer = GuildTools.getGuildPlayer(player.getUniqueId());
        if (!guildPlayer.getPost().canSetLocation()) return;
        VexGui vexGui = new VexGui(ImageURL.GUILDTELEPORTBUTON.getURL(), -1, -1, 10, 10, 10 ,10);
        VexTextField vexTextField = new VexTextField(-1,-1,160,15,6,101);
        vexGui.addComponent(vexTextField);
        VexButton createButton = new VexButton(298511, Message.getMsg(Message.SET_TELEPORT), ImageURL.GUILDTELEPORTBUTON.getURL(), ImageURL.GUILDTELEPORTBUTON.getURL(), -1, 120, 60, 20, clicker -> {
            int location = telemap.get(clicker.getUniqueId());
            GuildTools.addLocation(clicker, location, VexViewAPI.getPlayerCurrentGui(clicker).getVexGui().getTextField().getTypedText());
            clicker.closeInventory();
        });
        vexGui.addComponent(createButton);
        VexViewAPI.openGui(player, vexGui);
    }
}
