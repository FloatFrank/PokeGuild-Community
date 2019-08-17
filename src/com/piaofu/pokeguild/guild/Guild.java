package com.piaofu.pokeguild.guild;

import com.piaofu.pokeguild.Message;
import com.piaofu.pokeguild.data.DataLoader;
import com.piaofu.pokeguild.exception.CreatePartyException;
import com.piaofu.pokeguild.gui.guildchat.ChatGui;
import com.piaofu.pokeguild.guild.guilddata.GuildConfig;
import com.piaofu.pokeguild.guild.guildinventory.Backpack;
import com.piaofu.pokeguild.guild.guildinventory.BackpackManager;
import com.piaofu.pokeguild.guild.guildinventory.InventoryConfig;
import com.piaofu.pokeguild.guild.guildlevel.GuildLevel;
import com.piaofu.pokeguild.guild.guildlevel.LevelHolder;
import com.piaofu.pokeguild.guild.guildrelation.GuildRelation;
import com.piaofu.pokeguild.guild.locationplus.GuildLocation;
import com.piaofu.pokeguild.guild.playerdata.GuildPlayer;
import com.piaofu.pokeguild.guild.playerdata.GuildPost;
import com.piaofu.pokeguild.guildpoint.PointTools;
import com.piaofu.pokeguild.main.PokeGuild;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Guild {
    //需要构造↓
    private String guildName;
    private UUID guildOwner;
    private GuildLevel level;
    private List<GuildPlayer> players = new ArrayList<>();
    private GuildLocation location01;
    private GuildLocation location02;
    private GuildLocation location03;
    private String broadInfo;
    private int money;
    private int battlePower;
    private GuildRelation relation;
    private String guildInfo;
    private List<UUID> applyingPlayer = new ArrayList<>();
    //无需构造↓
    private Backpack backpack;
    private ChatGui chatGui;


    //额外构造操作
    private void buildUpWith() {
        this.Backpack();
        new InventoryConfig(this);
        this.chatGui = new ChatGui(this);
        this.addToList();
        this.register();
        this.firstSave();

    }
    public List<UUID> getApplyingPlayer() {
        return applyingPlayer;
    }
    public void register() {
        DataLoader.addGuildRegister(this.guildName);
    }
    //首次创建公会的构建
    public Guild(String name, Player player) {
        this.guildName = name;
        this.guildOwner = player.getUniqueId();
        this.level = LevelHolder.get(1);
        GuildPlayer owner = new GuildPlayer(0, player.getUniqueId(), GuildPost.OWNER, this);
        this.players.add(owner);
        this.money = 0;
        this.battlePower = 0;
        this.relation = new GuildRelation(null, null);
        this.broadInfo = Message.getMsg(Message.EMPTY);
        this.guildInfo = Message.getMsg(Message.EMPTY);
        buildUpWith();

    }
    public boolean hasPlayer(UUID p) {
        return GuildTools.hasPlayer(this, p);
    }

    /**
     * 让全部在线的公会玩家看到这则消息
     */
    public void broadcastMessage(String message) {
        players.forEach(players -> {
            Player player;
            if((player = players.isOnlineAndGetPlayer()) != null) {
                player.sendMessage(message);
            }
        });
    }
    public boolean canLevelUp() {
        return GuildTools.canLevelUp(this);
    }
    public void removeGuild() {
        broadcastMessage(Message.getMsg(Message.KILL_GUILD_INFO));
        PointTools.deleteGuildAllPoint(this);
        GuildConfig config = GuildConfig.getConfig(this);
        config.deleteFile();
        GuildHolder.removeGuild(this);
        DataLoader.delGuildRegister(this.getGuildName());
    }
    public Guild(String guildName, UUID guildOwner, GuildLevel level, List<GuildPlayer> players, GuildLocation location01, GuildLocation location02, GuildLocation location03, String broadInfo, int money, int battlePower, GuildRelation relation, String guildInfo, List<UUID> applyingPlayer) {
        this.applyingPlayer = applyingPlayer;
        this.guildName = guildName;
        this.guildOwner = guildOwner;
        this.level = level;
        this.players = players;
        this.location01 = location01;
        this.location02 = location02;
        this.location03 = location03;
        this.broadInfo = broadInfo;
        this.money = money;
        this.battlePower = battlePower;
        this.relation = relation;
        this.guildInfo = guildInfo;
        buildUpWith();
    }
    public void sendMessage(String message) {
        for (GuildPlayer player : players) {
            if (player.getPlayer().isOnline())
                player.getPlayer().getPlayer().sendMessage(message);
        }
    }
    public void addPlayerToList(UUID uuid) {
        if(!applyingPlayer.contains(uuid))
            applyingPlayer.add(uuid);
        onlySaveApplyList();
    }
    public void removePlayerFromList(UUID uuid) {
        if(applyingPlayer.contains(uuid))
            applyingPlayer.remove(uuid);
        onlySaveApplyList();
    }
    public void removePlayer(GuildPlayer kicked) {
        if (this.getPlayers().contains(kicked)) {
            this.getPlayers().remove(kicked);
            onlySavePlayers();
        }
    }
    public Guild (String name) {
        this.guildName = name;
        load();
    }
    public void onlySaveApplyList() {
        GuildConfig config = GuildConfig.getConfig(this);
        List<String> strings = new ArrayList<>();
        applyingPlayer.forEach(item -> {
            strings.add(item.toString());
        });
        config.set("guild.apply", strings);
        config.forceSave();
    }
    public void onlySaveName() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.name", this.guildName);
        config.forceSave();
    }
    public void onlySavePlayers() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("player", null);
        for (GuildPlayer player : players) {
            config.set("player."+player.getUuid()+".contribution", player.getContribution());
            switch (player.getPost()) {
                case OWNER: {
                    config.set("player."+player.getUuid()+".job", "OWNER");
                    break;
                }
                case INTERVIEWER: {
                    config.set("player."+player.getUuid()+".job", "INTERVIEWER");
                    break;
                }
                case SUBOWNER: {
                    config.set("player."+player.getUuid()+".job", "SUBOWNER");
                    break;
                }
                case SENTINAL: {
                    config.set("player."+player.getUuid()+".job", "SENTINAL");
                    break;
                }
                case AMBASSADOR: {
                    config.set("player."+player.getUuid()+".job", "AMBASSADOR");
                    break;
                }
                case MANAGER: {
                    config.set("player."+player.getUuid()+".job", "MANAGER");
                    break;
                }
                case MEMBER: {
                    config.set("player."+player.getUuid()+".job", "MEMBER");
                    break;
                }
            }
        }
        config.forceSave();
    }
//    public void firstSave() {
//        GuildConfig config = GuildConfig.getConfig(this);
//        config.set("guild.name", this.guildName);
//        config.set("guild.money", this.money);
//        config.set("guild.broadcast", this.broadInfo);
//        config.set("guild.info", this.guildInfo);
//        config.set("guild.owner", this.guildOwner.toString());
//        config.set("guild.level", this.level.getLevel());
//        config.set("guild.battlepower", this.battlePower);
//        config.forceSave();
//    }
    public void firstSave() {
        onlySaveName();
        onlySaveMoney();
        onlySaveBroadcast();
        onlySaveInfo();
        onlySavePlayers();
        onlySaveBattlePower();
        onlySaveLevel();
        onlySaveOwner();
        onlySaveLocation();
        onlySaveApplyList();
//        new BukkitRunnable() {
//            int i = 0;
//            @Override
//            public void run() {
//                switch (i) {
//                    case 0 : {
//                        onlySaveName();
//                        break;
//                    }
//                    case 1 : {
//                        onlySaveMoney();
//                        break;
//                    }
//                    case 2 : {
//                        onlySaveBroadcast();
//                        break;
//                    }
//                    case 3 : {
//                        onlySaveInfo();
//                        break;
//                    }
//                    case 4 : {
//                        onlySavePlayers();
//                        break;
//                    }
//                    case 5 : {
//                        onlySaveBattlePower();
//                        break;
//                    }
//                    case 6 : {
//                        onlySaveLevel();
//                        break;
//                    }
//                    case 7 : {
//                        onlySaveOwner();
//                        break;
//                    }
//                    case 8 : {
//                        onlySaveLocation();
//                        break;
//                    }
//                    default: {
//                        this.cancel();
//                        return;
//                    }
//                }
//                i++;
//            }
//        }.runTaskTimerAsynchronously(PokeGuild.plugin, 2, 1);
    }

    public void onlySaveLocation() {
        GuildConfig config = GuildConfig.getConfig(this);
        if (location01 != null) {
            config.set("guild.location01.world", location01.getWorld().getName());
            config.set("guild.location01.x", location01.getBlockX());
            config.set("guild.location01.y", location01.getBlockY());
            config.set("guild.location01.z", location01.getBlockZ());
            config.set("guild.location01.name", location01.getName());
        }
        if (location02 != null) {
            config.set("guild.location02.x", location02.getBlockX());
            config.set("guild.location02.y", location02.getBlockY());
            config.set("guild.location02.z", location02.getBlockZ());
            config.set("guild.location02.world", location02.getWorld().getName());
            config.set("guild.location02.name", location02.getName());
        }
        if (location03 != null) {
            config.set("guild.location03.x", location03.getBlockX());
            config.set("guild.location03.y", location03.getBlockY());
            config.set("guild.location03.z", location03.getBlockZ());
            config.set("guild.location03.world", location03.getWorld().getName());
            config.set("guild.location03.name", location03.getName());
        }
        config.forceSave();
    }

    public void onlySaveOwner() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.owner", this.guildOwner.toString());
        config.forceSave();
    }

    public void onlySaveInfo() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.info", this.guildInfo);
        config.forceSave();
    }

    public void onlySaveBattlePower() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.battlepower", this.battlePower);
        config.forceSave();
    }

    public void onlySaveBroadcast() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.broadcast", this.broadInfo);
        config.forceSave();
    }

    public void onlySaveLevel() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.level", this.level.getLevel());
        config.forceSave();
    }

    public void onlySaveMoney() {
        GuildConfig config = GuildConfig.getConfig(this);
        config.set("guild.money", this.money);
        config.forceSave();
    }
    private void loadLocation(int i, GuildConfig config) {
        try {
            if (config.getConfigurationSection("guild").getKeys(false).contains("location0"+i)) {
                World world = Bukkit.getWorld(config.getString("guild.location0"+i+".world"));
                if(world==null)
                    return;
                int x = config.getInt("guild.location0"+i+".x");
                int y = config.getInt("guild.location0"+i+".y");
                int z = config.getInt("guild.location0"+i+".z");
                String name = config.getString("guild.location0"+i+".name");
                switch (i) {
                    case 1 : {
                        location01 = new GuildLocation(world, x, y, z, name);
                        break;
                    }
                    case 2 : {
                        location02 = new GuildLocation(world, x, y, z, name);
                        break;
                    }
                    case 3 : {
                        location03 = new GuildLocation(world, x, y, z, name);
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        GuildConfig config = GuildConfig.getConfig(this);
        List<String> strings = config.getStringList("guild.apply");
        if(strings!=null) {
            strings.forEach(item -> {
                this.applyingPlayer.add(UUID.fromString(item));
            });
        }
        this.guildName = config.getString("guild.name");
        this.money = config.getInt("guild.money");
        this.level = LevelHolder.get(config.getInt("guild.level"));
        this.broadInfo = config.getString("guild.broadcast");
        this.battlePower = config.getInt("guild.battlepower");
        this.guildInfo = config.getString("guild.info");
        this.guildOwner = UUID.fromString(config.getString("guild.owner"));
        for(String tt : config.getConfigurationSection("player").getKeys(false)) {
            int contribution = config.getInt("player."+tt+".contribution");
            String job = config.getString("player."+tt+".job");
            GuildPost post;
            if (job == null) throw new CreatePartyException("创建公会时发生错误,错误原因,构建错误，请检查是否删除了数据文件，发生错误的是"+this.guildName+"公会的"+"UUID为"+tt+"的玩家");
            switch (job) {
                case "OWNER": {
                    post = GuildPost.OWNER;
                    break;
                }
                case "INTERVIEWER": {
                    post = GuildPost.INTERVIEWER;
                    break;
                }
                case "SUBOWNER" : {
                    post = GuildPost.SUBOWNER;
                    break;
                }
                case "SENTINAL" : {
                    post = GuildPost.SENTINAL;
                    break;
                }
                case "AMBASSADOR" : {
                    post = GuildPost.AMBASSADOR;
                    break;
                }
                case "MANAGER" : {
                    post = GuildPost.MANAGER;
                    break;
                }
                case "MEMBER" : {
                    post = GuildPost.MEMBER;
                    break;
                }
                default: {
                    throw new CreatePartyException("创建公会时发生错误,错误原因,构建错误，请检查是否删除了数据文件，发生错误的是"+this.guildName+"公会的"+"UUID为"+tt+"的玩家");
                }
            }
            GuildPlayer player = new GuildPlayer(contribution, UUID.fromString(tt),post, this);
            this.players.add(player);
            buildUpWith();
            try {
                loadLocation(1, config);
            } catch (Exception e) {
                location01 = null;
            }
            try {
                loadLocation(2, config);
            } catch (Exception e) {
                location02 = null;
            }
            try {
                loadLocation(3, config);
            } catch (Exception e) {
                location03 = null;
            }

        }
    }
    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public UUID getGuildOwner() {
        return guildOwner;
    }

    public void setGuildOwner(UUID guildOwner) {
        this.guildOwner = guildOwner;
    }


    public List<GuildPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<GuildPlayer> players) {
        this.players = players;
    }

    public GuildLocation getLocation01() {
        return location01;
    }

    public void setLocation01(GuildLocation location01) {
        this.location01 = location01;
        onlySaveLocation();
    }

    public GuildLocation getLocation02() {
        return location02;
    }

    public void setLocation02(GuildLocation location02) {
        this.location02 = location02;onlySaveLocation();
    }

    public GuildLocation getLocation03() {
        return location03;
    }

    public void setLocation03(GuildLocation location03) {
        this.location03 = location03;onlySaveLocation();
    }

    public String getBroadInfo() {
        return broadInfo;
    }

    public void setBroadInfo(String broadInfo) {
        this.broadInfo = broadInfo;
    }

    public Guild addToList() {
        GuildHolder.addGuild(this);
        return this;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    public void addMoney(int money) {
        this.money+=money;
        this.onlySaveMoney();}

    public int getBattlePower() {
        return battlePower;
    }

    public void setBattlePower(int battlePower) {
        this.battlePower = battlePower;
    }

    public GuildRelation getRelation() {
        return relation;
    }

    public void setRelation(GuildRelation relation) {
        this.relation = relation;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void Backpack() {
        BackpackManager.createBackpack(this);
    }
    public void openInventory(Player player) {
        player.openInventory(backpack.getInventory());
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public GuildLevel getLevel() {
        return level;
    }

    public void setLevel(GuildLevel level) {
        this.level = level;
    }

    public ChatGui getChatGui() {
        return chatGui;
    }

    public String getGuildInfo() {
        return guildInfo;
    }
    public void setGuildInfo(String newInfo) {
        this.guildInfo = newInfo;
    }
}
