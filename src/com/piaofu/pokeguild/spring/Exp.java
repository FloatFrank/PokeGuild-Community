package com.piaofu.pokeguild.spring;

public class Exp {
    private String permission;
    private double money;
    private int exp;
    public Exp(String permission, double money, int exp){
        this.permission = permission;
        this.money = money;
        this.exp = exp;
    }

    public String getPermission() {
        return permission;
    }

    public double getMoney() {
        return money;
    }

    public int getExp() {
        return exp;
    }
}
