package com.piaofu.pokeguild.guildpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointHolder {
    /**
     * 所有据点对象的存储器
     */
    private static List<PointObject> points = new ArrayList<>();
    private static List<PointTime> times = new ArrayList<>();
    public static PointTime nowTime = null;
    /**
     * 某一个数字所代表的据点等级对象, 是所有据点等级数据的存储器
     */
    private static HashMap<Integer, PointLevel> levelMap = new HashMap<>();

    public static void addLevel(int level, PointLevel levelP) {
        levelMap.put(level, levelP);
    }
    public static void clearLevel() {
        levelMap.clear();
    }
    public static HashMap<Integer, PointLevel> getLevel() {
        return levelMap;
    }
    public static List<PointObject> getPoints() {
        return points;
    }
    public static void addPoint(PointObject object) {
        if (points.contains(object))
            return;
        points.add(object);
    }
    public static void addTime(PointTime time) {
        if(!times.contains(time))
            times.add(time);
    }
    public static List<PointTime> getTimes() {
        return times;
    }
}
