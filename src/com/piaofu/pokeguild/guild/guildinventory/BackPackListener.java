package com.piaofu.pokeguild.guild.guildinventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BackPackListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void invClose(final InventoryCloseEvent event) {
        final Backpack backpack = BackpackManager.getBackpack(event.getInventory());
        if (backpack == null) {
            return;
        }
        backpack.save();
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void invClick(final InventoryClickEvent event) {
        final Backpack backpack = BackpackManager.getBackpack(event.getInventory());
        if (backpack == null) {
            return;
        }
        backpack.save();
    }
}
