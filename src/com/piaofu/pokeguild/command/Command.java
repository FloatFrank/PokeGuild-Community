package com.piaofu.pokeguild.command;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.stream.IntStream;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        SenderType type = SenderType.CONSOLE;
        if (sender instanceof Player) {
            if (!sender.hasPermission(PokeGuild.plugin.getName() + ".use")) {
                sender.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
                return true;
            }
            type = SenderType.PLAYER;
        }
        if (args.length == 0) {
            String color = "&b";
            for (PlayerCommand sub : CommandsHolder.commandMap.keySet()) {
                if (this.contains(sub.type(), type) && sender.hasPermission(PokeGuild.plugin.getName() + "." + sub.cmd())) {
                    color = (color.equals("&b") ? "" : "&b");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(color + "/{0} {1}{2}&7 -&c {3}", label, sub.cmd(), sub.arg(), sub.msg())));
                }
            }
            return true;
        }
        for (PlayerCommand sub2 : CommandsHolder.commandMap.keySet()) {
            if (!sub2.cmd().equalsIgnoreCase(args[0])) {
                continue;
            }
            if (!this.contains(sub2.type(), type) || !sender.hasPermission(PokeGuild.plugin.getName() + "." + args[0])) {
                sender.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
                return true;
            }
            try {
                    CommandsHolder.commandMap.get(sub2).invoke(CommandsHolder.class, sender, args);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {

                ex2.printStackTrace();
            }
            return true;
        }
        sender.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
        return true;
    }
    private boolean contains(SenderType[] type1, SenderType type2) {
        return IntStream.range(0, type1.length).anyMatch(i -> type1[i].equals(SenderType.ALL) || type1[i].equals(type2));
    }
}
