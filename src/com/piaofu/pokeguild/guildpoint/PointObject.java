package com.piaofu.pokeguild.guildpoint;


import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.main.PokeGuild;
import com.piaofu.pokeguild.particle.Particle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 据点对象
 * @author 漂浮
 */
public class PointObject {
    public BukkitRunnable bukkitTask;
    public boolean protect = false;
    private String key;
    private String name;
    private String levelName;
    private int x;
    private int y;
    private int z;
    private int level;
    private int money;
    private boolean killAttackPlayer = false;
    private String world;
    private String guildName;
    private Guild guild;
    private int health;
    private Location location;
    private int maxHealth;
    private List<UUID> playerList = new ArrayList<>();
    private HashMap<String, Integer> attackMap = new HashMap<>();
    public void refreshMap() {
        attackMap = (HashMap<String, Integer>) sortMap(attackMap);
    }
    public boolean setProtect(int time, boolean kill) {
        if(protect) {
            return false;
        }
        bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
                    protect = false;
                }
            };
        killAttackPlayer = kill;
        protect = true;
        bukkitTask.runTaskLater(PokeGuild.plugin, 20*time);
        return true;
    }

    public void heal(int num) {
        int sum = health + num;
        if(sum > maxHealth) {
            health = maxHealth;
        }else {
            health = sum;
        }
    }
    public int getMaxHealth() {
        return this.maxHealth;
    }
    public int getHealth() {
        return this.health;
    }
    void attack(Guild guild, Player player, int num) {
        if (guild.getGuildName().equals(this.guildName)) {
            player.sendMessage(Message.getMsg(Message.CANNOT_ATTACK_SELF_GUILD));
            return;
        }
        if (protect) {
            player.sendMessage(Message.getMsg(Message.CANNOT_ATTACK_PROTECT));
            if(killAttackPlayer)
                player.setHealth(0);
            return;
        }
        int sum = 0;
        if (attackMap.containsKey(guild.getGuildName())) {
            sum = attackMap.get(guild.getGuildName());
        }
        attackMap.put(guild.getGuildName(), sum+=num);
        health-=num;
        if(health > 0) {
            player.sendMessage(Message.getMsg(Message.ATTACK_INFO, String.valueOf(this.name), String.valueOf(num), String.valueOf(this.health), String.valueOf(this.maxHealth)));
            refreshMap();
        }else {
            Guild guild1 = GuildTools.getGuildFromString((String) attackMap.keySet().toArray()[0]);
            if(guild1 == null) {
                this.guild = null;
                this.guildName = Message.getMsg(Message.EMPTY);
                health = maxHealth;
                saveGuild();
                PointTools.clearAPointAllMap(this);
                return;
            }
            if(DataLoader.getEffectSwitch()) {
                Particle.showPointBoom(location, DataLoader.getEffectKickString());
            }
            Bukkit.broadcastMessage(Message.getMsg(Message.ATTACK_FINISH_INFO, this.name, player.getName()));
            this.guild = guild1;
            this.guildName = guild1.getGuildName();
            health = maxHealth;
            saveGuild();
            PointTools.clearAPointAllMap(this);
        }

    }
    void addPlayer(UUID player) {
        if(!playerList.contains(player)) {
            playerList.add(player);
        }
    }
    public static <K, V extends Comparable<? super V>> Map<K, V> sortMap(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        list.sort((o1, o2) -> {
            int compare = (o1.getValue()).compareTo(o2.getValue());
            return -compare;
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    public void defPlayer(UUID player) {
        playerList.remove(player);
    }
    PointObject(String world, String name, int x, int y, int z,int level, String guildName, String key) {
        this.world = world;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
        PointLevel level1 = PointHolder.getLevel().get(level);
        this.maxHealth = level1.getHealth();
        this.health = level1.getHealth();
        this.money = level1.getMoney();
        this.levelName = level1.getName();
        this.key = key;
        World world1 =Bukkit.getWorld(world);
        if (world1 == null) {
            Bukkit.getConsoleSender().sendMessage(("§4" + key + "世界名出错"));
            return;
        }
        Location location = new Location(world1,x,y,z);
        this.location = location;
        Block block = world1.getBlockAt(location);
        block.setType(Material.OBSIDIAN);

        if (guildName == null || GuildTools.getGuildFromString(guildName) == null) {
            this.guildName = "无";
            Bukkit.getConsoleSender().sendMessage("§b|> §r[据点构建成功] 名."+ name + " 属.无");
        }else {
            this.guildName = guildName;
            guild = GuildTools.getGuildFromString(guildName);
            Bukkit.getConsoleSender().sendMessage("§b|> §r[据点构建成功] 名."+ name + " 属." + guildName);
        }
    }
    public void saveGuild() {
        PointBuilder.getPointYAML().setGuild(key, guildName);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }
    void addToList() {
        PointHolder.addPoint(this);
    }

    public HashMap<String, Integer> getAttackMap() {
        return attackMap;
    }

    public void setAttackMap(HashMap<String, Integer> attackMap) {
        this.attackMap = attackMap;
    }

    public String getKey() {
        return key;
    }

    public int getLevel() {
        return level;
    }

    public String getWorld() {
        return world;
    }

    public Guild getGuild() {
        return guild;
    }
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getLevelName() {
        return levelName;
    }

    public Location getLocation() {
        return location;
    }
}
