package com.piaofu.pokeguild.guild.guildinventory;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BackpackManager {
    public static void createBackpack(final Guild guild) {
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)null, 6 * 9, guild.getGuildName() + Message.getMsg(Message.STORGE));
        final Backpack backpack = new Backpack(guild, inventory);
        backpack.load();
        guild.setBackpack(backpack);
        return;
    }

    /**
     * 获取到某一公会的仓库
     * @param inventory 仓库
     * @return
     */
    public static Backpack getBackpack(final Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        if (inventory.getName() == null || !inventory.getName().contains(Message.getMsg(Message.STORGE))) {
            return null;
        }
        final String targetName = inventory.getName().split(Message.getMsg(Message.STORGE))[0];
        final Guild guild = GuildTools.getGuildFromString(targetName);
        if (guild == null) {
            return null;
        }
        return guild.getBackpack();
    }
}
