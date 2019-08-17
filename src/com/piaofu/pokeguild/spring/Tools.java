package com.piaofu.pokeguild.spring;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.main.PokeGuild;
import com.piaofu.pokeguild.side.PokemonSide;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Tools {

    @PokemonSide
    private static void addExp(@org.jetbrains.annotations.NotNull Player player, int num) {
        UUID uuid = player.getUniqueId();
        if(PokeGuild.plugin.isPokemonServer)
            for (Pokemon pokemon : Pixelmon.storageManager.getParty(uuid).getTeam()) {
                pokemon.getLevelContainer().awardEXP(num);
            }
        else
            player.giveExp(num);
    }
    private static void addExpNMoney(Player player){
        for (Exp exp : Storage.exps){
            if (player.hasPermission(exp.getPermission())){
                addExp(player, exp.getExp());
                PokeGuild.plugin.economy.depositPlayer(player, exp.getMoney());
                player.sendMessage(Message.getMsg(Message.EXP_INFO, String.valueOf(exp.getExp()), String.valueOf(exp.getMoney())));
                Guild guild;
                if ((guild = GuildTools.getPlayerGuild(player.getUniqueId())) != null) {
                    int i;
                    int j;
                    addExp(player, (i = guild.getLevel().getAddExp()));
                    PokeGuild.plugin.economy.depositPlayer(player, (j = guild.getLevel().getAddMoney()));
                    player.sendMessage(Message.getMsg(Message.EXP_GUILD,String.valueOf(j),String.valueOf(i)));
                }
                return;
            }

        }
    }

    public static void runTimer(){
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Storage.springPlayer){
                    addExpNMoney(player);
                }
            }
        }.runTaskTimer(PokeGuild.plugin, 100, 1200);
    }
    /**
     * 让某一玩家加入休闲区
     * @param player 玩家
     */
    static void joinSpring(Player player){
        if (Storage.springPlayer.contains(player)) return;
        player.sendMessage(Message.getMsg(Message.SPRING_PLAYER_JOIN));
        writeLoc(player, false);
        player.teleport(Storage.location);
        Storage.springPlayer.add(player);
    }
    /**
     * 让某一玩家强制离开休闲区
     * @param player 玩家
     */
    static void leaveSpring(Player player){
        if (!Storage.springPlayer.contains(player)) return;
        player.sendMessage(Message.getMsg(Message.SPRING_PLAYER_LEAVE));
        Storage.springPlayer.remove(player);
        teleportPlayer(player);
    }

    /**
     * 写入某一玩家的记录坐标信息
     * @param player 玩家
     * @param tf 是否使用
     */
    private static void writeLoc(@org.jetbrains.annotations.NotNull Player player, Boolean tf) {
        YamlConfiguration yaml = Yaml.getLocationData();
        if (!yaml.getKeys(false).contains(player.getName())) {
            yaml.getKeys(false).add(player.getName());
            yaml.set(player.getName() + ".world", player.getWorld().getName());
            yaml.set(player.getName() + ".x", player.getLocation().getBlockX());
            yaml.set(player.getName() + ".y", player.getLocation().getBlockY());
            yaml.set(player.getName() + ".z", player.getLocation().getBlockZ());
            if (tf)
                yaml.set(player.getName() + ".used", true);
            else
                yaml.set(player.getName() + ".used", false);
        } else {
            yaml.set(player.getName() + ".world", player.getWorld().getName());
            yaml.set(player.getName() + ".x", player.getLocation().getBlockX());
            yaml.set(player.getName() + ".y", player.getLocation().getBlockY());
            yaml.set(player.getName() + ".z", player.getLocation().getBlockZ());
            if (tf)
                yaml.set(player.getName() + ".used", true);
            else
                yaml.set(player.getName() + ".used", false);
        }
        Yaml.saveLocationConfig();
        Yaml.loadLocationData();
    }
    static void teleportPlayer(@NotNull Player p) {
        if(!p.isOnline()){
            return;
        }
        YamlConfiguration yaml = Yaml.getLocationData();
        if (yaml.getKeys(false).contains(p.getName())) {
            if (!yaml.getBoolean(p.getName() + ".used")) {
                double x = yaml.getDouble(p.getName() + ".x");
                double y = yaml.getDouble(p.getName() + ".y");
                double z = yaml.getDouble(p.getName() + ".z");
                String world = yaml.getString(p.getName() + ".world");
                p.teleport(new Location(Bukkit.getWorld(world), x, y, z));
                yaml.set(p.getName() + ".used", true);
                Yaml.saveLocationConfig();
                Yaml.loadLocationData();
            }
        }
    }
}
