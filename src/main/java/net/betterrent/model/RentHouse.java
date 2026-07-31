package net.betterrent.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentHouse {

    private final String name;
    private final double pricePerDay;

    private UUID owner;

    private long expireTime;

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


    public boolean isRented() {

        return owner != null && expireTime > System.currentTimeMillis();
    }
}
