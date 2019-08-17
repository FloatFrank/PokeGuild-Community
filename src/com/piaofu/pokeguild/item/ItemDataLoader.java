package com.piaofu.pokeguild.item;

import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemDataLoader extends YamlConfiguration {
    private File file = new File(PokeGuild.plugin.getDataFolder(), "item.yml");
    public ItemDataLoader load() {
        writeFileOut();
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return this;
    }
    private void writeFileOut() {
        if(!file.exists()) {
            PokeGuild.plugin.saveResource("item.yml", false);
        }
    }
}
