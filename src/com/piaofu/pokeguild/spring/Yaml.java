package com.piaofu.pokeguild.spring;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;

public class Yaml {

    private static YamlConfiguration locationData;

    private static PokeGuild plugin = PokeGuild.plugin;

    @Contract(pure = true)
    static YamlConfiguration getLocationData() {return locationData;}
    private static void refreshLocation(){
        String world = plugin.getConfig().getString("PoorDoor.world");
        int x = plugin.getConfig().getInt("PoorDoor.x");
        int y = plugin.getConfig().getInt("PoorDoor.y");
        int z = plugin.getConfig().getInt("PoorDoor.z");
        try {
            Storage.location = new Location(Bukkit.getWorld(world),x,y,z);
        } catch (Exception e) {
            plugin.getLogger().warning("休闲区入口配置发生错误,请认真检查!已关闭插件");
            plugin.getPluginLoader().disablePlugin(plugin);

        }
    }

    private static void loadExpGroup(){
        ConfigurationSection con = plugin.getConfig().getConfigurationSection("ExpConfig");
        for (String key : con.getKeys(false)){
            String permission = con.getString(key+".permission");
            Double money = con.getDouble(key+".money");
            int exp = con.getInt(key+".exp");
            Exp nE = new Exp(permission, money, exp);
            Storage.exps.add(nE);
        }

    }

    public static void loadAll(){
        loadLocationData();
        refreshLocation();
        loadExpGroup();
        Message.loadLangData();
        Storage.type = plugin.getConfig().getBoolean("ExpType");
    }

    public static void loadLocationData() {
        File file = new File(plugin.getDataFolder(),"locationdata.yml");
        if(!file.exists()) {
            plugin.saveResource("locationdata.yml", false);
        }
        locationData = YamlConfiguration.loadConfiguration(file);
    }
    static void saveLocationConfig() {
        File file = new File(plugin.getDataFolder(),"locationdata.yml");
        try {
            locationData.save(file);
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            System.out.println("保存文件出错！");
        }
    }
}
