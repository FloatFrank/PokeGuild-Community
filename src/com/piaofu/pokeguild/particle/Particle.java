package com.piaofu.pokeguild.particle;

import com.piaofu.pokeguild.data.DataLoader;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;

public class Particle {
    public static void showPointBoom(Location location, String particle) {
        for (double i = 0; i < 180; i += 180 / 6) {
            // 依然要做角度与弧度的转换
            double radians = Math.toRadians(i);
            // 计算出来的半径
            double radius = Math.sin(radians);
            double y = Math.cos(radians);
            for (double j = 0; j < 360; j += 180 / 6) {
                // 依然需要做角度转弧度的操作
                double radiansCircle = Math.toRadians(j);
                double x = Math.cos(radiansCircle) * radius;
                double z = Math.sin(radiansCircle) * radius;
                location.add(x, y, z);
                try{
                    location.getWorld().playEffect(location, Effect.valueOf(particle), 1);
                }catch (Exception e){
                    Bukkit.getLogger().severe("特效名" + particle + "出错，请检查");}
                location.subtract(x, y, z);
            }
        }
    }
}
