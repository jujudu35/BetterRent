package net.betterrent.model;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentHouse {


    private final String name;

    private final double pricePerDay;


    private UUID owner;

    private long expireTime;


    private Location pos1;

    private Location pos2;


    private String worldGuardRegion;


    private final List<UUID> trustedPlayers;



    public RentHouse(String name, double pricePerDay) {

        this.name = name;
        this.pricePerDay = pricePerDay;

        this.trustedPlayers = new ArrayList<>();

    }




    public String getName() {

        return name;

    }




    public double getPricePerDay() {

        return pricePerDay;

    }





    public UUID getOwner() {

        return owner;

    }




    public void setOwner(UUID owner) {

        this.owner = owner;

    }





    public long getExpireTime() {

        return expireTime;

    }




    public void setExpireTime(long expireTime) {

        this.expireTime = expireTime;

    }





    public boolean isRented() {

        return owner != null &&
                expireTime > System.currentTimeMillis();

    }





    public List<UUID> getTrustedPlayers() {

        return trustedPlayers;

    }





    public void addTrusted(UUID uuid) {

        if (!trustedPlayers.contains(uuid)) {

            trustedPlayers.add(uuid);

        }

    }





    public void removeTrusted(UUID uuid) {

        trustedPlayers.remove(uuid);

    }





    public boolean isTrusted(UUID uuid) {

        return trustedPlayers.contains(uuid);

    }





    public Location getPos1() {

        return pos1;

    }





    public void setPos1(Location pos1) {

        this.pos1 = pos1;

    }





    public Location getPos2() {

        return pos2;

    }





    public void setPos2(Location pos2) {

        this.pos2 = pos2;

    }





    public String getWorldGuardRegion() {

        return worldGuardRegion;

    }





    public void setWorldGuardRegion(String region) {

        this.worldGuardRegion = region;

    }

}
