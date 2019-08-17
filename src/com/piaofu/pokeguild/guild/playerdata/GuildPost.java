package com.piaofu.pokeguild.guild.playerdata;

import com.piaofu.pokeguild.Message;

public enum GuildPost {
    OWNER, MEMBER, SUBOWNER, AMBASSADOR, SENTINAL, INTERVIEWER, MANAGER;

    public String getPostName() {
        switch (this) {
            case OWNER: return Message.getMsg(Message.OWNER);
            case MEMBER: return Message.getMsg(Message.MEMBER);
            case SENTINAL: return Message.getMsg(Message.SENTINAL);
            case SUBOWNER: return Message.getMsg(Message.SUBOWNER);
            case AMBASSADOR: return Message.getMsg(Message.AMBASSADOR);
            case INTERVIEWER: return Message.getMsg(Message.INTERVIEWER);
            case MANAGER: return Message.getMsg(Message.MANAGER);
            default: return Message.getMsg(Message.EMPTY);
        }
    }

    public boolean canDoEveryThing() {
        switch (this) {
            case OWNER: return true;
            case MEMBER:
            case SENTINAL:
            case SUBOWNER:
            case AMBASSADOR:
            case INTERVIEWER:
            case MANAGER: return false;
        }
        return false;
    }
    public boolean canKickMember() {
        switch (this) {
            case SUBOWNER:
            case MANAGER:
            case OWNER: return true;
        }
        return false;
    }
    public static boolean kickResult(GuildPost kicker, GuildPost kicked) {
        if (kicker.equals(GuildPost.OWNER)) {
            return !kicked.equals(GuildPost.OWNER);
        }
        if (kicker.equals(GuildPost.MANAGER)) {
            if (kicked.equals(GuildPost.OWNER) || kicked.equals(GuildPost.SUBOWNER) ||kicker.equals(GuildPost.MANAGER))
                return false;
        }
        if (kicker.equals(GuildPost.SUBOWNER)) {
            if (kicked.equals(GuildPost.OWNER) || kicked.equals(GuildPost.SUBOWNER))
                return false;
        }
        return false;
    }

    public boolean canSetLocation() {
        switch (this) {
            case SUBOWNER:
            case MANAGER:
            case SENTINAL:
            case OWNER: return true;
        }
        return false;
    }
    public boolean canLevelUpGuild() {
        switch (this) {
            case OWNER:
            case SUBOWNER: return true;
        }
        return false;
    }
    public boolean canChangeGuildRelation() {
        switch (this) {
            case OWNER:
            case MANAGER:
            case SUBOWNER:
            case AMBASSADOR: return true;
        }
        return false;
    }
    public boolean canAgreeGuildJoin() {
        switch (this) {
            case OWNER:
            case MANAGER:
            case SUBOWNER:
            case INTERVIEWER: return true;
        }
        return false;
    }
    public boolean canChangeBroadcastAndInfo() {
        switch (this) {
            case OWNER:
            case SUBOWNER: return true;
        }
        return false;
    }
}
