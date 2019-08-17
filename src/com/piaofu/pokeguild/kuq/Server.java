package com.piaofu.pokeguild.kuq;

import com.piaofu.pokeguild.guild.Guild;
import com.piaofu.pokeguild.guild.GuildHolder;
import com.piaofu.pokeguild.guild.GuildTools;
import com.piaofu.pokeguild.guildpoint.PointHolder;
import com.piaofu.pokeguild.guildpoint.PointObject;
import com.piaofu.pokeguild.guildpoint.PointTime;
import com.piaofu.pokeguild.guildpoint.PointTools;
import com.piaofu.pokeguild.main.PokeGuild;
import com.sun.org.apache.regexp.internal.RE;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    public static final int PORT = 8411;//监听的端口号

    public void run() {
        System.out.println("服务器启动...\n");
        Server server = new Server();
        server.init();
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
            }
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }

    private class HandlerThread implements Runnable {
        private Socket socket;
        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            try {
                // 读取客户端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                // 处理客户端数据
                System.out.println("客户端发过来的内容:" + clientInputStr);

                // 向客户端回复信息
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                if(clientInputStr.contains("switchPower")) {
                    PokeGuild.robotPower = !PokeGuild.robotPower;
                    out.writeUTF("机器人开启状态:" + PokeGuild.robotPower);
                    out.close();
                    input.close();
                    return;
                }
                StringBuilder s = new StringBuilder();
                if(!PokeGuild.robotPower) {
                    out.close();
                    input.close();
                    return;
                }
                if(PokeGuild.isPowerOn) {
                    if (clientInputStr.contains("pointList")) {
                        for (PointObject point : PointHolder.getPoints()) {
                            s.append("-  ").append(point.getName()).append("\n");
                        }
                        s.append("发送/看据点 据点名 可以查看该据点的详细情况");
                    } else if (clientInputStr.contains("pointBattleTime")) {
                        for (PointTime pt : PointHolder.getTimes()) {
                            s.append("-  ").append("星期").append(pt.getDay()).append("[").append(pt.getStartTime()).append("——").append(pt.getOverTime()).append("]").append("\n");
                        }
                    } else if (clientInputStr.contains("serverInfo")) {
                        s.append("作者: 漂浮 \n" + "人数:").append(Bukkit.getOnlinePlayers().size()).append("\n 赶快上线吧！让服务器人数更多一点兄弟~");

                    } else if (clientInputStr.contains("guildList")) {
                        for (Guild guild : GuildHolder.getGuilds()) {
                            s.append("-  ").append(guild.getGuildName()).append("\n");
                        }
                        s.append("发送/看公会 公会名 可以查看该公会的详细情况");
                    } else if (clientInputStr.contains("gh")) {
                        String p = clientInputStr.split(" ")[1];
                        Guild guild = GuildTools.getGuildFromString(p);
                        if (guild == null)
                            s.append("不存在该公会");
                        else {
                            s.append("公会名 - ").append(guild.getGuildName()).append("\n");
                            s.append("等级 - ").append(guild.getLevel().getLevel()).append("\n");
                            s.append("会长 - ").append(Bukkit.getOfflinePlayer(guild.getGuildOwner()).getName()).append("\n");
                            s.append("人数 - ").append(guild.getPlayers().size()).append("\n");
                            s.append("战力值 - ").append(guild.getBattlePower()).append("\n");
                        }
                    } else if (clientInputStr.contains("sp")) {
                        String p = clientInputStr.split(" ")[1];
                        PointObject point = PointTools.getPointFromString(p);
                        if (point == null)
                            s.append("不存在该据点");
                        else {
                            s.append("据点名 - ").append(point.getName()).append("\n");
                            s.append("占领公会 - ").append(point.getGuildName()).append("\n");
                            s.append("地点 - x: ").append(point.getX()).append(" y: ").append(point.getY()).append(" z: ").append(point.getZ()).append("\n");
                            s.append("世界 - ").append(point.getWorld()).append("\n");
                            s.append("当前血量 - ").append(point.getHealth()).append("/").append(point.getMaxHealth()).append("\n");
                        }
                    } else if (clientInputStr.contains("givePoint")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "points give " + clientInputStr.split(" ")[1] + " " + clientInputStr.split(" ")[2]);
                        s.append("成功给予").append(clientInputStr.split(" ")[1]).append(" ").append(clientInputStr.split(" ")[2]).append("点券");
                    } else if (clientInputStr.contains("givePAll")) {
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "points give " + player.getName() + " " + clientInputStr.split(" ")[1]);
                            s.append("成功给予").append(player.getName()).append(" ").append(clientInputStr.split(" ")[1]).append("点券\n");
                        }
                    } else
                        s.append("服务器未开启！");
                    out.writeUTF(s.toString());


                    out.close();
                    input.close();
                }
            } catch (Exception e) {
                System.out.println("服务器 run 异常: " + e.getMessage());
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("服务端 finally 异常:" + e.getMessage());
                    }
                }
            }
        }
    }
}