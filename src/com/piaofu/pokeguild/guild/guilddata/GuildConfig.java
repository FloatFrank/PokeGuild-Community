package com.piaofu.pokeguild.guild.guilddata;

import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GuildConfig extends YamlConfiguration {
    private File dataFolder;
    private static Map<String, GuildConfig> configs;
    private File pconfl;
    private final Object saveLock;
    private String guildName;
    public static GuildConfig getConfig(Guild guild) {
        synchronized (GuildConfig.configs) {
            if (GuildConfig.configs.containsKey(guild)) {
                return GuildConfig.configs.get(guild);
            }
            GuildConfig config = new GuildConfig(guild);
            GuildConfig.configs.put(guild.getGuildName(), config);
            return config;
        }
    }
    public void deleteFile() {
        if(pconfl.exists()) {
            pconfl.delete();
        }
    }
    public static void unloadAll() {
        Collection<GuildConfig> oldConfs = new ArrayList<>(GuildConfig.configs.values());
        synchronized (GuildConfig.configs) {
            for ( GuildConfig config : oldConfs) {
                config.discard();
            }
        }
    }

    public GuildConfig( Guild guild) {
        this.dataFolder = PokeGuild.plugin.getDataFolder();
        this.pconfl = null;
        this.saveLock = new Object();
        this.pconfl = new File(this.dataFolder + File.separator + "guilddata" + File.separator + guild.getGuildName() + ".yml");
        try {
            this.load(this.pconfl);
        }
        catch (Exception ex) {}
    }

    private GuildConfig() {
        this.dataFolder = PokeGuild.plugin.getDataFolder();
        this.pconfl = null;
        this.saveLock = new Object();
        this.guildName = null;
    }

    public void forceSave() {
//        synchronized (this.saveLock) {
            try {
                this.save(this.pconfl);
            }
            catch (IOException ex) {}
//        }
    }

    public void discard() {
        this.forceSave();
        synchronized (GuildConfig.configs) {
            GuildConfig.configs.remove(this.guildName);
        }
    }

    static {
        GuildConfig.configs = new HashMap<>();
    }
}
