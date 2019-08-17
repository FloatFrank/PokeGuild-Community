package com.piaofu.pokeguild.spring;

import com.piaofu.pokeguild.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public static void onChat(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        if (!Storage.springPlayer.contains(player)){
            return;
        }
        if (event.getMessage().equalsIgnoreCase("/spring leave")){
            return;
        }
        event.setCancelled(true);
        player.sendMessage(Message.getMsg(Message.SPRING_INFO));

    }
    @EventHandler
    public static void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Tools.leaveSpring(player);
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Tools.teleportPlayer(player);
    }

    @EventHandler
    public static void onDeath(EntityDeathEvent event){
        if (!(event.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) event.getEntity();
        Tools.leaveSpring(player);
    }
    @EventHandler
    public static void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Tools.leaveSpring(player);
    }
    @EventHandler
    public static void onChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Tools.leaveSpring(player);
    }
    @EventHandler
    public static void onFoodChange(FoodLevelChangeEvent event){
        if (event.getEntity() instanceof Player){
            Player player = (Player)event.getEntity();
            if (Storage.springPlayer.contains(player)){
                event.setCancelled(true);
            }
        }

    }

}
