package com.piaofu.pokeguild.guildpoint;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * YAML存储器
 */
final class PointYAML extends YamlConfiguration {
    private ConfigurationSection configurationSection;
    private ConfigurationSection configurationSectionL;
    public static List<String> strings = Collections.singletonList(Message.getMsg(Message.DEBATTLING));
    public static List<String> stringA = Collections.singletonList(Message.getMsg(Message.BATTLING));


    private File file = new File(PokeGuild.plugin.getDataFolder(), "points.yml");
    private void loadLevelConfig() {
        this.configurationSectionL.getKeys(false).forEach(item -> {
            int level = Integer.valueOf(item.split("v")[1]);
            PointHolder.addLevel(level, new PointLevel(level, getHealth(item), getMoney(item), getNameL(item)));
        });
    }
    PointYAML() {

            try {
                writeFileOut();
                this.load(file);
                configurationSection = this.getConfigurationSection("points");
                configurationSectionL = this.getConfigurationSection("PointsSetting");
                loadLevelConfig();
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
    }
    private void writeFileOut() {
        if(!file.exists()) {
            PokeGuild.plugin.saveResource("points.yml", false);
        }
    }
    public List<String> getTimeList() {return this.getStringList("StartTime");}
    public int getHealth(String keyL) {
        return configurationSectionL.getInt(keyL + ".health");
    }
    public int getMoney(String keyL) {
        return configurationSectionL.getInt(keyL + ".money");
    }
    public String getNameL(String keyL) {
        return configurationSectionL.getString(keyL + ".name");
    }

    public String getName(String key) {
        return configurationSection.getString(key + ".name");
    }
    public String getWorld(String key) {
        return configurationSection.getString(key + ".world");
    }
    public String getGuild(String key) {
        return configurationSection.getString(key + ".guild");
    }
    public int getX(String key) {
        return configurationSection.getInt(key + ".x");
    }
    public int getY(String key) {
        return configurationSection.getInt(key + ".y");
    }
    public int getZ(String key) {
        return configurationSection.getInt(key + ".z");
    }
    public int getLevel(String key) {
        return configurationSection.getInt(key + ".level");
    }

    public void setGuild(String key, String guild) {
        configurationSection.set(key + ".guild", guild);
        try {
            this.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
