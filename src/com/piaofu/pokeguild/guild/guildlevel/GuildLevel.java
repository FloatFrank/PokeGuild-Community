package com.piaofu.pokeguild.guild.guildlevel;

public class GuildLevel {
    private int needMoney;
    private int needBattlePower;
    private int addExp;
    private int addMoney;
    private boolean hasNextLevel;
    private int maxPerson;
    private int level;

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    private int pointNum;

    public int getNeedMoney() {
        return needMoney;
    }

    public int getNeedBattlePower() {
        return needBattlePower;
    }

    public int getAddExp() {
        return addExp;
    }

    public int getAddMoney() {
        return addMoney;
    }

    public boolean isHasNextLevel() {
        return hasNextLevel;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public GuildLevel(int pointNum, int needMoney, int needBattlePower, int addExp, int addMoney, boolean hasNextLevel, int maxPerson, int level) {
        this.needMoney = needMoney;
        this.pointNum = pointNum;
        this.needBattlePower = needBattlePower;
        this.addExp = addExp;
        this.addMoney = addMoney;
        this.hasNextLevel = hasNextLevel;
        this.maxPerson = maxPerson;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
