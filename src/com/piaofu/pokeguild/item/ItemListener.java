package com.piaofu.pokeguild.item;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ItemListener implements Listener {
    public static List<UUID> blackList = new ArrayList<>();
    @EventHandler
    public static void onUseGuildItem(PlayerInteractEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
//        if(blackList.contains(uuid)) {
//            return;
//        }
//        if(!blackList.contains(uuid)) {
//            blackList.add(event.getPlayer().getUniqueId());
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    blackList.remove(uuid);
//                }
//            }.runTaskLater(PokeGuild.plugin, 1);
//        }
        Block block = event.getClickedBlock();
        Action action = event.getAction();
//        System.out.println(action.name());
        ItemStack itemStack = event.getItem();
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
//            System.out.println("debug hasNotMetaOrNull");
            return;
        }
        Player player = event.getPlayer();
        List<String> lore = event.getItem().getItemMeta().getLore();
//        System.out.println(lore);
        for(GuildItemForPB guildItem : ItemHolder.itemMap.keySet()) {
//            System.out.println(guildItem.name());
            if(!ItemTools.getActionListHasAnAction(guildItem.action(), action)) {
                continue;
            }
            ConfigurationSection section = PokeGuild.itemDataLoader.getConfigurationSection(guildItem.name());
//            System.out.println(section);
            if(section == null)
                return;
            if(!ItemTools.getLoreListHasAnLore(guildItem.name(), lore)) {
                continue;
            }
            Guild guild = null;

            if(guildItem.needGuild() && (guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null) {
                Message.getMsg(Message.NONE_GUILD);
                return;
            }

            try {
                if((boolean)ItemHolder.itemMap.get(guildItem).invoke(ItemHolder.class, player, guild, block ,section)) {
                    //执行成功，消耗物品
//                    System.out.println("debug success");
                    itemStack.setAmount(itemStack.getAmount() - 1);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return;

        }
    }
    @EventHandler
    public static void onUseGuildItem(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof Player)) {
            return;
        }
        ItemStack itemStack = event.getPlayer().getEquipment().getItemInHand();
        if(itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) {
//            System.out.println("debug hasNotMetaOrNull");
            return;
        }
        Player player = event.getPlayer();
        List<String> lore = itemStack.getItemMeta().getLore();
        for(GuildItemForEE guildItem : ItemHolder.itemMapEE.keySet()) {
            ConfigurationSection section = PokeGuild.itemDataLoader.getConfigurationSection(guildItem.name());
            if(section == null)
                return;
            if(!ItemTools.getLoreListHasAnLore(guildItem.name(), lore)) {
                continue;
            }
            Guild guild = null;
            if(guildItem.needGuild() && (guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null) {
                Message.getMsg(Message.NONE_GUILD);
                return;
            }
            try {
                if((boolean)ItemHolder.itemMapEE.get(guildItem).invoke(ItemHolder.class, player,(Player)event.getRightClicked(), guild ,section)) {
                    //执行成功，消耗物品
//                    System.out.println("debug success");
                    itemStack.setAmount(itemStack.getAmount() - 1);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }
    }
}
