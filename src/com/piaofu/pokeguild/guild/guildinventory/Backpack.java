package com.piaofu.pokeguild.guild.guildinventory;

import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *  背包类
 */
public class Backpack {
        private Inventory inventory;
        private Guild guild;
        private int Level;

        public Backpack (Guild guild, final Inventory inventory) {
            this.inventory = inventory;
            this.guild = guild;
        }
        public Inventory getInventory() {
            return this.inventory;
        }

        public int getSize() {
            return this.getInventory().getSize() / 9;
        }

        public void clear() {
            for (int slot = 0; slot < this.inventory.getSize(); ++slot) {
                this.inventory.setItem(slot, (ItemStack)null);
            }
            this.save();
        }

        public void save() {
            final InventoryConfig config = InventoryConfig.getConfig(this.guild);
            for (int slot = 0; slot < this.inventory.getSize(); ++slot) {
                final ItemStack item = this.inventory.getItem(slot);
                config.set("backpack.item." + slot, (Object)item);
            }
            config.set("backpack.last-known-size", (Object)this.getSize());
            config.forceSave();
        }

        public void load() {
            final InventoryConfig config = InventoryConfig.getConfig(this.guild);
            for (int slot = 0; slot < this.inventory.getSize(); ++slot) {
                final ItemStack item = config.getItemStack("backpack.item." + slot);
                if (item != null) {
                    this.inventory.setItem(slot, item);
                }
            }
        }
    }

