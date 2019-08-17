package com.piaofu.pokeguild.guildpoint;

/**
 * 据点等级对象
 */
public class PointLevel {
    private int level;
    private int health;
    private int money;
    private String name;

    public PointLevel(int level, int health, int money, String name) {
        this.level = level;
        this.health = health;
        this.money = money;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
