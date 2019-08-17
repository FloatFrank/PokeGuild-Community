package com.piaofu.pokeguild.main;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.apilistener.onKickPlayer;
import com.piaofu.pokeguild.command.Command;
import com.piaofu.pokeguild.command.CommandsHolder;
import com.piaofu.pokeguild.command.PlayerCommand;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.gui.guildlistener.GuiListener;
import com.piaofu.pokeguild.guild.GuildBuilder;
import com.piaofu.pokeguild.guild.guildgift.GiftListener;
import com.piaofu.pokeguild.guild.guildgift.GiftListenerForPokemon;
import com.piaofu.pokeguild.guild.guildinventory.BackPackListener;
import com.piaofu.pokeguild.guildpoint.PointBuilder;
//import com.piaofu.pokeguild.kuq.SocketService;
import com.piaofu.pokeguild.item.*;
import com.piaofu.pokeguild.kuq.Server;
import com.piaofu.pokeguild.msgformat.ChatListener;
import com.piaofu.pokeguild.papi.GuildPapi;
import com.piaofu.pokeguild.spring.Listener;
import com.piaofu.pokeguild.spring.Storage;
import com.piaofu.pokeguild.spring.Tools;
import com.piaofu.pokeguild.spring.Yaml;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Method;


import static com.piaofu.pokeguild.main.TimingsScan.getTime;
import static com.piaofu.pokeguild.main.TimingsScan.setTime;

public class PokeGuild extends JavaPlugin {
    public boolean isPokemonServer;
    private DataLoader dataLoader;
    public static ItemDataLoader itemDataLoader;
    public GuildBuilder guildBuilder;
    private GuildPapi guildPapi;
    public static PokeGuild plugin;
    public Permission permission = null;
    public Economy economy = null;
    public Chat chat = null;
    public static boolean isPowerOn = false;
    public static boolean robotPower = true;

    private boolean hasVex(){
        try {
            Class.forName("lk.vexview.api.VexViewAPI");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }
    private boolean initVault(){
        boolean hasNull = false;
        //获取权限系统实例
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            if ((permission = permissionProvider.getProvider()) == null) hasNull = true;
        }
        //初始化聊天系统实例
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            if ((chat = chatProvider.getProvider()) == null) hasNull = true;
        }
        //初始化经济系统实例
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            if ((economy = economyProvider.getProvider()) == null) hasNull = true;
        }
        return !hasNull;
    }
    @Override
    public void onEnable() {
        setTime();
        Bukkit.getConsoleSender().sendMessage("§b====== §ePokeGuild §b<-> §eSTART §b======");
        Bukkit.getConsoleSender().sendMessage("§b|> §e欢迎使用PokeGuild公会插件!");
        isPokemonServer = this.getConfig().getBoolean("IsPokemonServer");
        Bukkit.getConsoleSender().sendMessage("§b|> §e配置文件配置是否为CatServer神奇宝贝服: " + isPokemonServer);
        //检测VexView
        if(!hasVex()) {
            Bukkit.getConsoleSender().sendMessage("[PokeGuild]§4[警告！] >> 未检测到Vexview插件，插件停止启用");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getConsoleSender().sendMessage("§b|> §3VexView    §e已兼容");
        plugin = this;
        saveDefaultConfig();
        if (!initVault()) {
            getLogger().severe(Message.getMsg(Message.NONE_VAULT));
            return;
        }
        Bukkit.getConsoleSender().sendMessage("§b|> §3Vault      §e已兼容");
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Listener(),this);
        pluginManager.registerEvents(new BackPackListener(),this);
        pluginManager.registerEvents(new GuiListener(),this);
        pluginManager.registerEvents(new ItemListener(),this);
        new Server().start();

        //注册原版事件
        pluginManager.registerEvents(new ChatListener(), this);
        //注册API事件
        pluginManager.registerEvents(new onKickPlayer(), this);
        //注册独特事件
        if(isPokemonServer)
            pluginManager.registerEvents(new GiftListenerForPokemon(), this);
        pluginManager.registerEvents(new GiftListener(), this);
        Tools.runTimer();
        Bukkit.getConsoleSender().sendMessage("§b|>§r 构建开始 ———————");
        Yaml.loadAll();
        Yaml.loadLocationData();
        dataLoader = new DataLoader(this);
        itemDataLoader = new ItemDataLoader().load();
        //加载所有方法
        for (final Method method : CommandsHolder.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PlayerCommand.class)) {
                CommandsHolder.commandMap.put(method.getAnnotation(PlayerCommand.class), method);
            }
        }
        //加载所有物品
        for (final Method method : ItemHolder.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GuildItemForPB.class)) {
                ItemHolder.itemMap.put(method.getAnnotation(GuildItemForPB.class), method);
            }
        }
        for (final Method method : ItemHolder.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GuildItemForEE.class)) {
                ItemHolder.itemMapEE.put(method.getAnnotation(GuildItemForEE.class), method);
            }
        }
        //挂钩PAPI
        guildPapi = new GuildPapi();
        Bukkit.getPluginCommand("pokeguild").setExecutor(new Command());
        Bukkit.getPluginCommand("spring").setExecutor(new com.piaofu.pokeguild.spring.Command());
        guildBuilder = new GuildBuilder();
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                for (Guild guild : GuildHolder.getGuilds())
//                    guild.firstSave();
//            }
//        }.runTaskTimerAsynchronously(this, 20*120, 20*60);
        //开启据点准备工具
        PointBuilder.buildAllPoint();
        Bukkit.getConsoleSender().sendMessage("§b|>§r 构建结束 ——耗时 " +getTime()+ "ms");
        Bukkit.getConsoleSender().sendMessage("§b|> §2作者: 漂浮§r | §2正版购买QQ: 739975004");
        Bukkit.getConsoleSender().sendMessage("§b====== §ePokeGuild §b<-> §eOVER §b======");
        Bukkit.getConsoleSender().sendMessage("§b====== §e第一版本无验证，只记录debug信息，下一版本会加入验证系统！======");
        isPowerOn = true;
    }
    public void reload() {
        try {
            plugin.getConfig().load(this.getDataFolder() + File.separator + "config.yml");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage("§b====== §ePokeGuild §b<-> §eReload §b======");
        Yaml.loadAll();
        DataLoader.load();
        itemDataLoader.load();
        Yaml.loadLocationData();

        PointBuilder.rebuildPoint();
        Bukkit.getConsoleSender().sendMessage("§b====== §ePokeGuild §b<-> §eComplete §b======");

    }
}
