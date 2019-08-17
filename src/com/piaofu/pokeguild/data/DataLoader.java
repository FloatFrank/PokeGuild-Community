package com.piaofu.pokeguild.data;

import com.piaofu.pokeguild.guild.GuildBuilder;
import com.piaofu.pokeguild.guild.guildlevel.GuildLevel;
import com.piaofu.pokeguild.guild.guildlevel.LevelHolder;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 数据加载器
 */
public class DataLoader {
    private static PokeGuild plugin;
    private static FileConfiguration config = PokeGuild.plugin.getConfig();
    public static String memberGuiColorOnline;
    public static String memberGuiColorOffline;
    public static List<String> memberGuiInfo;
    private static int money;
    private static double moneyRate;
    private static int killBoss;
    public static boolean pointPVP;
    private static int killNormal;
    private static int key;
    private static String effectString;
    private static String effectKickString;
    private static boolean effectSwitch;
    private static List<String> guildRegister;


    public static void loadEffectString() {
        effectString = plugin.getConfig().getString("PointEffect");
        effectKickString = plugin.getConfig().getString("PointKickEffect");
        effectSwitch = plugin.getConfig().getBoolean("PointSwitch");
    }

    public DataLoader(PokeGuild plugin){
        DataLoader.plugin = plugin;
        load();
    }
    public static void load() {
        loadMemberGui();
        loadGuildRegister();
        loadGuildLevel();
        loadNeedMoney();
        loadKey();
        loadGiftData();
        loadKillNormal();
        loadCanPVP();
        loadEffectString();
        loadMoneyRate();
        ImageURL.loadTexture();
        GuildBuilder.buildAllGuild();
    }
    private static void loadMemberGui() {
        memberGuiColorOnline = config.getString("Gui.memberGui.GuiButtonColorOnline");
        memberGuiColorOffline = config.getString("Gui.memberGui.GuiButtonColorOffline");
        memberGuiInfo = config.getStringList("Gui.memberGui.Info");

    }
    private static  void loadGiftData() { killBoss = config.getInt("KBOSS");}
    private static  void loadKillNormal() { killNormal = config.getInt("KNORMAL");}

    /**
     * 加载打开键位
     */

    private static  void loadKey() { key = config.getInt("KEY");}
    private static  void loadMoneyRate() { moneyRate = config.getDouble("moneyRate");}
    private static  void loadCanPVP() {

        pointPVP = config.getBoolean("PointPVP");
        Bukkit.getConsoleSender().sendMessage("§b|> §3Protect-PVP：" + pointPVP);
    }
    /**
     * 消耗品数据加载存储
     */
    private static void loadNeedMoney() {
        money = config.getInt("NeedMoney");
    }
    /**
     * 加载公会等级配置
     */
    private static void loadGuildLevel() {
        LevelHolder.clear();
        int maxLevel = config.getInt("MAX_LEVEL");
        ConfigurationSection configurationSection = config.getConfigurationSection("LEVELUP");
        int i = 1;
        try {
            for (;i < maxLevel+1 ;i++) {
                int needMoney = configurationSection.getInt("lv"+i+".needMoney");
                int needBattlePower = configurationSection.getInt("lv"+i+".needBattlePower");
                int addExp = configurationSection.getInt("lv"+i+".addExp");
                int addMoney = configurationSection.getInt("lv"+i+".addMoney");
                int maxPerson = configurationSection.getInt("lv"+i+".maxPerson");
                int limitPoint = configurationSection.getInt("lv"+i+".limitPoint");

                GuildLevel level;
                if (i != maxLevel) {
                    level = new GuildLevel(limitPoint, needMoney, needBattlePower, addExp, addMoney, true, maxPerson, i);
                }else {
                    level = new GuildLevel(limitPoint, needMoney, needBattlePower, addExp, addMoney, false, maxPerson, i);
                }
                LevelHolder.add(i, level);
                Bukkit.getConsoleSender().sendMessage("§b|> §r[等级构建成功] lv."+ i);
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("§b|> §e[BUG!]等级配置项第"+i+"项出现错误，请检查配置文件，即将关闭插件");
            Bukkit.getPluginManager().disablePlugin(PokeGuild.plugin);
        }
    }

    /**
     * 加载公会注册表
     */
    private static void loadGuildRegister() {
        guildRegister = new ArrayList<>();
        File file = new File(PokeGuild.plugin.getDataFolder() + File.separator + "guilddata");
        if(file.exists()) {
            if(file.list() != null)
                for(String t : Objects.requireNonNull(file.list())) {
                    getRegister().add(t.split(".yml")[0]);
                }
        }

    }
    /**
     * 获得公会注册表
     */
    public static List<String> getRegister() {
        return guildRegister;
    }
    /**
     * 删除某公会注册表
     */
    public static void delGuildRegister(String guild) {
        if (guildRegister.contains(guild)) {
            guildRegister.remove(guild);
        }
    }
    /**
     * 添加某公会至注册表
     */
    public static void addGuildRegister(String guild) {
        if (!guildRegister.contains(guild)) {
            guildRegister.add(guild);
        }
    }

    public static int getMoney() {
        return money;
    }

    public static int getKey() {
        return key;
    }

    public static int getKillBoss() {
        return killBoss;
    }

    public static int getKillNormal() {
        return killNormal;
    }

    public static double getMoneyRate() {
        return moneyRate;
    }

    public static String getEffectString() {
        return effectString;
    }

    public static boolean getEffectSwitch() {
        return effectSwitch;
    }

    public static String getEffectKickString() {
        return effectKickString;
    }
}
