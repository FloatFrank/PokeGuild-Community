package com.piaofu.pokeguild.data;

import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

/**
 * 图片URL枚举
 */
public enum ImageURL {
    //BUTTON组
    CREATECLUB_BUTTON, CREATECLUB_BUTTON_, GUILDTOP_BUTTON, GUILDTOP_BUTTON_,
    HELP_BUTTON, HELP_BUTTON_, MYCLUB_BUTTON, MYCLUB_BUTTON_, PLAYGROUND_BUTTON,
    PLAYGROUND_BUTTON_, GUILDTELEPORTBUTON,CK,CK_,CY,CY_,GL,GL_,JD,JD_,LT,LT_,
    WJ,WJ_, GUILDTOPBUTTONL
    //图片组
    ,CHATROOM, BACKGROUND, GUILDMAIN, GUILDTELEPORT, LAST, POINT, POINT_INFO
    ,NEED, GET, GUILDTOP, GUILDAPPLYLIST, GUILDMEMBERLIST;
    private static YamlConfiguration urlData;
    private static HashMap<String, String> map = new HashMap<>();
    private static String getStringUrl(ImageURL img) {
        if(map.containsKey(img.name()))
            return map.get(img.name());
        try {
            String s = urlData.getString(img.name());
            map.put(img.name(), s);
            return s;
        }catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§4" + img.name() + "图片配置出错或丢失！");
        }
        return null;
    }
    public static void loadTexture() {
        File file = new File(PokeGuild.plugin.getDataFolder(),"texture.yml");
        if(!file.exists()) {
            PokeGuild.plugin.saveResource("texture.yml", false);
        }
        urlData = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 获取该枚举的对应URL
     * @return 返回对应URL
     */

    public String getURL(){
        return getStringUrl(this);
    }
}
