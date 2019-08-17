package com.piaofu.pokeguild.gui;

import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.gui.guildcreate.GuildCreate;
import com.piaofu.pokeguild.gui.guildmanager.GuildManager;
import com.piaofu.pokeguild.gui.guilist.MainGui;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexTextField;
import org.bukkit.entity.Player;

public class GuiHolder {
    public static VexGui getChatGuiBasic() {
        return buildChatGuiBasic(ImageURL.CHATROOM.getURL(), -1, -1, 512, 288);
    }
    public static VexGui getGuildGuiBasic() {
        return buildGuildGuiBasic(ImageURL.GUILDMAIN.getURL(), -1, -1, 512, 288);
    }
    public static VexGui getTeleportGuiBasic() {
        return buildTeleportGuiBasic(ImageURL.GUILDTELEPORT.getURL(), -1, -1, 512, 288);
    }
    private static VexGui buildTeleportGuiBasic(String url, int x, int y, int w, int h){
        VexGui mainGui = new VexGui(url, x, y, w, h, w, h);
        return mainGui;
    }
    private static VexGui buildGuildGuiBasic(String url, int x, int y, int w, int h){
        VexGui mainGui = new VexGui(url, x, y, w, h, w, h);
        VexButton Button01 = new VexButton(2400, "", ImageURL.CY.getURL(), ImageURL.CY_.getURL(), 120, 40, 80, 26, new ButtonFunction() {
            @Override
            public void run(Player clicker) {
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}

                    MainGui.openGuildMemberGui(clicker);
            }
        });
        VexButton Button02 = new VexButton(2401, "", ImageURL.JD.getURL(), ImageURL.JD_.getURL(), 120, 75, 80, 26, new ButtonFunction() {
            @Override
            public void run(Player clicker) {
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
                    MainGui.openTeleportGui(clicker);
            }
        });
        VexButton Button03 = new VexButton(2402, "", ImageURL.CK.getURL(), ImageURL.CK_.getURL(), 120, 110, 80, 26, new ButtonFunction() {
            @Override
            public void run(Player clicker) {
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
                Guild guild;
                if ((guild = GuildTools.getPlayerGuild(clicker.getUniqueId())) == null) return;
                guild.openInventory(clicker);

            }
        });
        VexButton Button04 = new VexButton(2403, "", ImageURL.WJ.getURL(), ImageURL.WJ_.getURL(), 120, 145, 80, 26, new ButtonFunction() {
            @Override
            public void run(Player clicker) {
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
            }
        });

        VexButton Button05 = new VexButton(2404, "", ImageURL.GL.getURL(), ImageURL.GL_.getURL(), 120, 180, 80, 26, new ButtonFunction() {
            @Override
            public void run(Player clicker) {
                if (clicker.getOpenInventory()!=null){
                    clicker.closeInventory();
                }
                GuildManager.openManager(clicker);
            }
        });
        VexButton Button06 = new VexButton(2405, "", ImageURL.LT.getURL(), ImageURL.LT_.getURL(), 120, 215, 80, 26, new ButtonFunction() {
            @Override
            public void run(Player clicker) {
                if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
                Guild guild;
                if ((guild = GuildTools.getPlayerGuild(clicker.getUniqueId())) == null) return;
                    guild.getChatGui().openGui(clicker, true);
            }
        });
        mainGui.addComponent(Button01);
        mainGui.addComponent(Button02);
        mainGui.addComponent(Button03);
        mainGui.addComponent(Button04);
        mainGui.addComponent(Button05);
        mainGui.addComponent(Button06);
        return mainGui;
    }
    private static VexGui buildChatGuiBasic(String url, int x, int y, int w, int h) {
        VexGui mainGui = new VexGui(url, x, y, w, h, w, h);


        return mainGui;
    }
    public static VexGui getMainGuiBasic() {
        String url = ImageURL.BACKGROUND.getURL();
        int x = -1;
        int y = -1;
        int w = 384;
        int h = 216;
        VexGui mainGui = new VexGui(url, x, y, w, h, w, h);

        VexButton myClubButton = new VexButton(2100, "", ImageURL.MYCLUB_BUTTON.getURL(), ImageURL.MYCLUB_BUTTON_.getURL(), -1, 60, 100, 25, clicker -> {
            if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
                MainGui.openGuildGui(clicker);
        });
        VexButton createClubButton = new VexButton(2101, "", ImageURL.CREATECLUB_BUTTON.getURL(), ImageURL.CREATECLUB_BUTTON_.getURL(), -1, 85, 100, 25, clicker -> {
            if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
            GuildCreate.openCreateGui(clicker);
        });
        VexButton topButton = new VexButton(2102, "", ImageURL.GUILDTOP_BUTTON.getURL(), ImageURL.GUILDTOP_BUTTON_.getURL(), -1, 110, 100, 25, clicker -> {
            if (clicker.getOpenInventory()!=null){clicker.closeInventory();
                MainGui.openGuildTopGui(clicker);
            }
        });
        VexButton playgroundButton = new VexButton(2103, "", ImageURL.PLAYGROUND_BUTTON.getURL(), ImageURL.PLAYGROUND_BUTTON_.getURL(), -1, 135, 100, 25, clicker -> {
            if (clicker.getOpenInventory()!=null){clicker.closeInventory();
                clicker.performCommand("spring go");
            }
        });
        VexButton helpButton = new VexButton(2104, "", ImageURL.HELP_BUTTON.getURL(), ImageURL.HELP_BUTTON_.getURL(), -1, 160, 100, 25, clicker -> {
            if (clicker.getOpenInventory()!=null){clicker.closeInventory();}
            MainGui.openPointsListGui(clicker);
        });
        mainGui.addComponent(myClubButton);
        mainGui.addComponent(createClubButton);
        mainGui.addComponent(topButton);
        mainGui.addComponent(playgroundButton);
        mainGui.addComponent(helpButton);
        return mainGui;
    }
}
