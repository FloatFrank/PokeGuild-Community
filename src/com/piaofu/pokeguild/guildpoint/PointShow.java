package com.piaofu.pokeguild.guildpoint;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.Tools;
import com.piaofu.pokeguild.data.ImageURL;
import com.piaofu.pokeguild.main.PokeGuild;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.components.VexImage;
import lk.vexview.gui.components.VexText;
import lk.vexview.hud.VexImageShow;
import lk.vexview.hud.VexTextShow;
import org.bukkit.entity.Player;

import java.util.*;

public class PointShow {
    public static void sendPointNameAndGuild(Player player, PointObject pointObject) {
        List<String> lore = PokeGuild.plugin.getConfig().getStringList("ViewConfig.Point.Name.Lore");
        List<String> lores = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("[GUILD]", pointObject.getGuildName());
        map.put("[POINTNAME]", pointObject.getName());
        map.put("[MONEY]", String.valueOf(pointObject.getMoney()));
        map.put("[POINTLEVEL]", pointObject.getLevelName());
        map.put("[PROTECT]", pointObject.protect ? Message.getMsg(Message.PROTECT) :Message.getMsg(Message.DEPROTECT));
        lores = Tools.replaceString(lore, map);
        VexViewAPI.sendHUD(player, new VexTextShow(10412, new VexText(PokeGuild.plugin.getConfig().getInt("ViewConfig.Point.Name.X"), PokeGuild.plugin.getConfig().getInt("ViewConfig.Point.Name.Y"), lores), 0));
    }
    public static void removeAllShow(Player player) {
        VexViewAPI.removeHUD(player, 10412);
        VexViewAPI.removeHUD(player, 10413);
        VexViewAPI.removeHUD(player, 10414);
        VexViewAPI.removeHUD(player, 10415);
        VexViewAPI.removeHUD(player, 10416);
    }
    public static void sendAll(Player player, PointObject pointObject) {
        if(PokeGuild.plugin.getConfig().getBoolean("ViewConfig.Point.Health.Switch")) {
            sendPointHealth(player, pointObject);
        }
        if(PokeGuild.plugin.getConfig().getBoolean("ViewConfig.Point.Name.Switch")) {
            sendPointNameAndGuild(player, pointObject);
        }
        if(PokeGuild.plugin.getConfig().getBoolean("ViewConfig.Point.AttackMap.Switch")) {
            sendAttackMap(player, pointObject);
        }
    }

    private static void sendAttackMap(Player player, PointObject pointObject) {
        List<String> strings = new ArrayList<>();
        int i = 0;
        for(String tt : pointObject.getAttackMap().keySet()) {
            String nT = (tt + Message.getMsg(Message.POINT_DAMAGE) + pointObject.getAttackMap().get(tt));
            if(i == 0) {
                nT = nT + Message.getMsg(Message.THE_POINT_IS_YOUR_MAX_DAMAGE);
            }
            strings.add(nT);
            i++;
        }
        int x = PokeGuild.plugin.getConfig().getInt("ViewConfig.Point.AttackMap.X");
        int y = PokeGuild.plugin.getConfig().getInt("ViewConfig.Point.AttackMap.Y");
        VexText vexText1 = new VexText(x+25,y,strings);
        VexViewAPI.sendHUD(player, new VexTextShow(10416, vexText1, 0));
    }

    private static void sendPointHealth(Player player, PointObject obj) {
        List<String> line = new ArrayList<>();
        double dou = (double)obj.getHealth()/(double)obj.getMaxHealth();
        int long1 = (int)(100 * dou);
        int long2 = 100 - long1;
        line.add("§c" + obj.getHealth() + " §f/ §c" + obj.getMaxHealth());
        int x = PokeGuild.plugin.getConfig().getInt("ViewConfig.Point.Health.X");
        int y = PokeGuild.plugin.getConfig().getInt("ViewConfig.Point.Health.Y");
        VexText vexText1 = new VexText(x+25,y,line);
        VexImage levelBar1 = new VexImage(ImageURL.GET.getURL()
                ,x,y+20,long1,10, long1, 10);
        VexImage levelBar2 = new VexImage(ImageURL.NEED.getURL()
                ,x+long1,y+20,long2,10 ,long2,10);
        VexViewAPI.sendHUD(player, new VexTextShow(10413, vexText1, 0));
        VexViewAPI.sendHUD(player, new VexImageShow(10414, levelBar1, 0));
        VexViewAPI.sendHUD(player, new VexImageShow(10415, levelBar2, 0));
    }

}
