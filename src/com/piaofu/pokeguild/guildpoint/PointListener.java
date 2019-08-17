package com.piaofu.pokeguild.guildpoint;


import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.main.PokeGuild;
import lk.vexview.api.VexViewAPI;
import lk.vexview.hud.VexTextShow;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Point包核心监听器
 */
public class PointListener implements Listener {
    public static boolean canBreak = false;

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(canBreak) {
                    VexViewAPI.sendHUD(event.getPlayer(), new VexTextShow(4125, -1 , 40, PointYAML.stringA, 0));
                }else {
                    VexViewAPI.sendHUD(event.getPlayer(), new VexTextShow(4125, -1 , 40, PointYAML.strings, 0));
                }
            }
        }.runTaskLater(PokeGuild.plugin, 5);
    }
    /**
     * 玩家PVP事件
     */
    @EventHandler
    public static void onDamage(EntityDamageByEntityEvent event){
        Entity shoot = event.getDamager();
        if (!(event.getEntity() instanceof Player)){return;}

        if (shoot instanceof Projectile) {
            Projectile pro = (Projectile)shoot;
            if(pro.getType() == EntityType.SNOWBALL) {
                return;
            }
            if(!(pro.getShooter() instanceof Player)) {
                return;
            }
            shoot = (Entity) pro.getShooter();
        }
        if (!(shoot instanceof Player)) {
            return;
        }

        Player damager = (Player) shoot;
        UUID damaged = event.getEntity().getUniqueId();
        if(DataLoader.pointPVP) {
            Guild guild = GuildTools.getPlayerGuild(damager.getUniqueId());
            if (guild != null && guild.hasPlayer(damaged)) {
                event.setCancelled(true);
                damager.sendMessage(Message.getMsg(Message.CANNOT_ATTACK_MEMBER));
            }
        }
    }

    /**
     * 玩家破坏方块监听器
     * @param event 破坏方块事件
     */
    @EventHandler
    public static void onBreakPointer(BlockBreakEvent event) {
        PointObject object;
        if ((object = PointTools.blockIs(event.getBlock())) == null) return;
        event.setCancelled(true);
        if(!canBreak) {
            event.getPlayer().sendMessage(Message.getMsg(Message.POINT_NOT_START));
            return;
        }
        Player player = event.getPlayer();
        Guild guild;
        if((guild = GuildTools.getPlayerGuild(player.getUniqueId())) == null) {
            event.getPlayer().sendMessage(Message.getMsg(Message.NONE_GUILD));
            return;
        }
        if(PointTools.getGuildPointNum(guild) > guild.getLevel().getPointNum()) {
            event.getPlayer().sendMessage(Message.getMsg(Message.POINT_IS_FULL));
            return;
        }
        object.attack(guild, event.getPlayer(), 20);
    }

    /**
     * 玩家对方块交互事件
     * @param event 交互事件
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public static void onInteractPointer(PlayerInteractEvent event) {
        PointObject object;
        if ((object = PointTools.blockIs(event.getClickedBlock())) == null) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            event.setCancelled(true);
            PointTools.sendInfoGui(event.getPlayer(), object);
        }
    }
}
