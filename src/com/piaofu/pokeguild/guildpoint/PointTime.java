package com.piaofu.pokeguild.guildpoint;

import org.bukkit.Bukkit;

public class PointTime{
    private int day;
    private int startTime;
    private int overTime;

    public PointTime(int day, int startTime, int overTime) {
        this.day = day;
        this.startTime = startTime;
        this.overTime = overTime;
    }

    public int getDay() {
        return day;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getOverTime() {
        return overTime;
    }

    public void addToList() {
        PointHolder.addTime(this);
    }
    public boolean isIn(int[] times) {
        if(times[0] != day)
            return false;
        for(int i = startTime;i<overTime; i++) {
            if(i == times[1]) {
                return true;
            }
        }
        return false;
    }

}
