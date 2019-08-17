package com.piaofu.pokeguild.guild.guildinventory;

import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InventoryConfig extends YamlConfiguration
{
    private File dataFolder;
    private static Map<String, InventoryConfig> configs;
    private File pconfl;
    private Object saveLock;
    private String guildName;

    public static InventoryConfig getConfig(Guild guild) {
        synchronized (InventoryConfig.configs) {
            if (InventoryConfig.configs.containsKey(guild)) {
                return InventoryConfig.configs.get(guild);
            }
            final InventoryConfig config = new InventoryConfig(guild);
            InventoryConfig.configs.put(guild.getGuildName(), config);
            return config;
        }
    }

    public static void unloadAll() {
        final Collection<InventoryConfig> oldConfs = new ArrayList<InventoryConfig>(InventoryConfig.configs.values());
        synchronized (InventoryConfig.configs) {
            for (final InventoryConfig config : oldConfs) {
                config.discard();
            }
        }
    }

    public InventoryConfig(final Guild guild) {
        this.dataFolder = PokeGuild.plugin.getDataFolder();
        this.pconfl = null;
        this.saveLock = new Object();
        this.pconfl = new File(this.dataFolder + File.separator + "inventorydata" + File.separator + guild.getGuildName() + ".yml");
        try {
            this.load(this.pconfl);
        }
        catch (Exception ex) {}
    }

    private InventoryConfig() {
        this.dataFolder = PokeGuild.plugin.getDataFolder();
        this.pconfl = null;
        this.saveLock = new Object();
        this.guildName = null;
    }

    public void forceSave() {
        synchronized (this.saveLock) {
            try {
                this.save(this.pconfl);
            }
            catch (IOException ex) {}
        }
    }

    public void discard() {
        this.forceSave();
        synchronized (InventoryConfig.configs) {
            InventoryConfig.configs.remove(this.guildName);
        }
    }

    static {
        InventoryConfig.configs = new HashMap<String, InventoryConfig>();
    }
}

