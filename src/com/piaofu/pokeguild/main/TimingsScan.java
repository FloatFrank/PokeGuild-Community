package com.piaofu.pokeguild.main;

public class TimingsScan {
    private static long time;
    public static void setTime(){
        time = System.currentTimeMillis();
    }
    public static float getTime(){
        return System.currentTimeMillis()-time;
    }
}
