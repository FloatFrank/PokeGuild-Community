package com.piaofu.pokeguild.command;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.gui.guilist.MainGui;
import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildBuilder;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class CommandsHolder {
    public static HashMap<PlayerCommand, Method> commandMap = new HashMap<>();

    @PlayerCommand(cmd = "test", type = { SenderType.PLAYER }, msg = "粒子特效测试指令")
    public static void onTest(CommandSender sender, String[] args) {
        Location location = ((Player)sender).getLocation();
        for (int degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double x = Math.cos(radians);
            double y = Math.sin(radians);
            location.add(x, 0, y);
            location.getWorld().playEffect(location, Effect.valueOf(args[1]), 1);
            location.subtract(x, 0, y);
        }
    }

    @PlayerCommand(cmd = "reload", type = { SenderType.ALL } , msg = "重载配置文件")
    public static void onReload(CommandSender sender, String[] args) {
        PokeGuild.plugin.reload();
    }
    @PlayerCommand(cmd = "delete", type = { SenderType.ALL } , msg = "删除公会")
    public static void onDelete(CommandSender sender, String[] args) {
        if(args.length != 2) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        Guild guild;
        if((guild = GuildTools.getGuildFromString(args[1])) == null) {
            sender.sendMessage("不存在该公会");
            return;
        }
        guild.removeGuild();
        return;
    }
    @PlayerCommand(cmd = "open", type = { SenderType.CONSOLE } , msg = "为某个玩家打开主菜单")
    public static void onOpen(CommandSender sender, String[] args) {
        if(args.length != 2) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        Player player;
        if((player = Bukkit.getPlayer(args[1])) == null) {
            sender.sendMessage(Message.getMsg(Message.INVAILD_PLAYER));
            return;
        }
        MainGui.openMainGui(player);
        return;
    }
    @PlayerCommand(cmd = "congiveguild", type = { SenderType.ALL } , msg = "直接给某个Guild战力值")
    public static void onConGiveGuild(CommandSender sender, String[] args) {
        if(args.length != 3) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        Guild guild;
        int num;
        try {
            guild = GuildTools.getGuildFromString(args[1]);
            num = Integer.valueOf(args[2]);
        }catch (Exception e) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        if(guild == null)
            return;
        GuildTools.giveCon(guild, num);
        return;
    }
    @PlayerCommand(cmd = "congiveuser", type = { SenderType.ALL } , msg = "以某为玩家的身份给其所在的公会贡献战力值")
    public static void onConGiveUser(CommandSender sender, String[] args) {
        if(args.length != 3) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        Guild guild;
        UUID p;
        int num;
        try {
            num = Integer.valueOf(args[2]);
            p = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
        }catch (Exception e) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        if(p == null || (guild = GuildTools.getPlayerGuild(p)) == null)
            return;
        GuildTools.giveCon(guild, num, p);
        return;
    }
    @PlayerCommand(cmd = "mongive", type = { SenderType.ALL } , msg = "直接给某个Guild金钱")
    public static void onMonGive(CommandSender sender, String[] args) {
        if(args.length != 3) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        Guild guild;
        int num;
        try {
            guild = GuildTools.getGuildFromString(args[1]);
            num = Integer.valueOf(args[2]);
        }catch (Exception e) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        if(guild == null)
            return;
        GuildTools.giveMoney(guild, num);
        return;

    }
    @PlayerCommand(cmd = "main", type = { SenderType.PLAYER } , msg = "打开主菜单")
    public static void onOpenMain(CommandSender sender, String[] args) {
        MainGui.openMainGui((Player) sender);
    }
    @PlayerCommand(cmd = "cbc", type = { SenderType.PLAYER } , msg = "修改公会公告")
    public static void onCBC(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        Guild commanderGuild = GuildTools.getPlayerGuild(player.getUniqueId());
        if (args.length != 2) {sender.sendMessage(Message.getMsg(Message.NONE_GUILD));return;}
        if (commanderGuild == null) {sender.sendMessage(Message.getMsg(Message.CREATE_ARGSINVAILD));return;}
        char as[] = args[1].toCharArray();
        int maxLength = 100;
        for (int i = 0; i < as.length ; i++) {
            if (as[i] == '\\') {
                if (as[i+1] == 'n') {
                    maxLength-=10;
                }
            }
        }
        if (args[1].length() > maxLength) {sender.sendMessage(Message.getMsg(Message.CHANGEBROAD_SOLONG));return;}
        String newArgs = args[1].replace("&", "§");
        GuildTools.changeBroadInfo(commanderGuild, GuildTools.getGuildPlayer(player.getUniqueId()), newArgs);
    }
    @PlayerCommand(cmd = "ci", type = { SenderType.PLAYER } , msg = "修改公会简介")
    public static void onCi(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        Guild commanderGuild = GuildTools.getPlayerGuild(player.getUniqueId());
        if (args.length != 2) {sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));return;}
        if (commanderGuild == null) {sender.sendMessage(Message.getMsg(Message.NONE_GUILD));return;}
        char as[] = args[1].toCharArray();
        int maxLength = 100;
        for (int i = 0; i < as.length ; i++) {
            if (as[i] == '\\') {
                if (as[i+1] == 'n') {
                    maxLength-=8;
                }
            }
        }
        if (args[1].length() > maxLength) {sender.sendMessage(Message.getMsg(Message.CHANGEBROAD_SOLONG));return;}
        String newArgs = args[1].replace("&", "§");
        GuildTools.changeGuildInfo(commanderGuild, GuildTools.getGuildPlayer(player.getUniqueId()), newArgs);
    }
    @PlayerCommand(cmd = "create", type = { SenderType.PLAYER } , msg = "创建公会")
    public static void onCreate(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        Guild commanderGuild = GuildTools.getPlayerGuild(player.getUniqueId());
        if(!player.hasPermission("pokeguild.create"))
            return;
        if (args.length != 2) {sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));return;}
        if (commanderGuild != null) {
            sender.sendMessage(Message.getMsg(Message.HASGUILD));
            return;
        }
        if (args[1].length() > 8 || args[1].length() < 2) {
            sender.sendMessage(Message.getMsg(Message.CREATE_ARGSINVAILD));
        }
        if (!PokeGuild.plugin.economy.has(player, DataLoader.getMoney())) {
            sender.sendMessage(Message.getMsg(Message.NONE_MONEY));
            return;
        }
        PokeGuild.plugin.economy.withdrawPlayer(player, DataLoader.getMoney());
        sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
        GuildBuilder.buildAFirstGuild(player, args[1]);
    }
    @PlayerCommand(cmd = "seen", type = { SenderType.PLAYER } , msg = "偷看某个公会的聊天栏")
    public static void onSeen(CommandSender sender, String[] args) {
        Player player = (Player)sender;
        if(args.length != 2) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        if(!player.hasPermission("pokeguild.seen")) {
            sender.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
            return;
        }
        Guild guild;
        if((guild = GuildTools.getGuildFromString(args[1])) == null) {
            sender.sendMessage(Message.getMsg(Message.INVAILDGUILD));
            return;
        }
        guild.getChatGui().openGui(player, false);
    }
    @PlayerCommand(cmd = "mongiveuser", type = { SenderType.ALL } , msg = "让某个玩家减少自己的金钱让公会增加金钱并按比例换算")
    public static void onMonGiveUser(CommandSender sender, String[] args) {

        if(args.length != 3) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        int num;
        try {
            num = Integer.valueOf(args[2]);
        }catch (Exception e) {
            sender.sendMessage(Message.getMsg(Message.ARGS_INVAILD));
            return;
        }
        OfflinePlayer player;
        if((player = (Bukkit.getOfflinePlayer(args[1]))) != null) {
            GuildTools.giveMoney(num, player, sender);
        }


    }
}
