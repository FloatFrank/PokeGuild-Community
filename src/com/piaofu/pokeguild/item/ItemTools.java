package com.piaofu.pokeguild.item;

import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;
import java.util.List;

public class ItemTools {
    public static boolean getActionListHasAnAction(Action[] actions, Action action) {
        for(Action a : actions) {
            if (action.equals(a)) {
                return true;
            }
        }
        return false;
    }
    public static boolean getHandListHasAnHand(EquipmentSlot[] actions, EquipmentSlot action) {
        for(EquipmentSlot a : actions) {
            if (action.equals(a)) {
                return true;
            }
        }
        return false;
    }
    public static boolean getLoreListHasAnLore(String itemName, List<String> lore) {
        return PokeGuild.itemDataLoader.getStringList(itemName + ".Lore").equals(lore);
    }

}
