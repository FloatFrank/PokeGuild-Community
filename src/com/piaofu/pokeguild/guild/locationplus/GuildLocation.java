package com.piaofu.pokeguild.guild.locationplus;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class GuildLocation extends Location {
    private String name;
    public GuildLocation(World world, double x, double y, double z, String name) {
        super(world, x, y, z);
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
