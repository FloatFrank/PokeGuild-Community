package com.piaofu.pokeguild.spring;

import com.piaofu.pokeguild.Message;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
    private boolean used = false;
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("spring")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length < 1) {
                    player.sendMessage(Message.getMsg(Message.SPRING_NOARGS_INFO));
                    return true;
                }
                switch (args[0]){
                    case "go" : {
                        if (!player.hasPermission("spring.go")){
                            player.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
                            return true;
                        }
                        if (!used){
                            Yaml.loadAll();
                            used = true;
                        }
                        Tools.joinSpring(player);
                        return true;
                    }
                    case "leave" : {
                        if (!player.hasPermission("spring.leave")){
                            player.sendMessage(Message.getMsg(Message.NONE_PERMISSION));
                            return true;
                        }
                        Tools.leaveSpring(player);
                        return true;
                    }
                    case "reload" : {
                        if (!player.hasPermission("spring.reload")){
                            return true;
                        }
                        Yaml.loadAll();
                        player.sendMessage(Message.getMsg(Message.EVEN_RELOAD));
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
