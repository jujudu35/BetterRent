package net.betterrent.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class RentHouse {


    private final String name;

    private final double pricePerDay;


    private UUID owner;

    private long expireTime;


    private Location pos1;
    private Location pos2;


    private String worldGuardRegion;


    private final List<UUID> trustedPlayers;



    // ==========================
    // Permissions location
    // ==========================

    private boolean openDoors = true;
    private boolean openTrapdoors = true;
    private boolean openFenceGates = true;


    private boolean placeBlocks = true;
    private boolean breakBlocks = true;


    private boolean openChests = true;
    private boolean openBarrels = true;
    private boolean openShulkers = false;


    private boolean useFurnaces = true;
    private boolean useAnvils = true;
    private boolean useCrafting = true;
    private boolean useEnchanting = true;



    // Blocs interdits
    private final Set<Material> blockedPlaceBlocks;



    public RentHouse(String name, double pricePerDay) {


        this.name = name;
        this.pricePerDay = pricePerDay;


        this.trustedPlayers = new ArrayList<>();

        this.blockedPlaceBlocks = new HashSet<>();


        // Blocs interdits par défaut

        blockedPlaceBlocks.add(Material.HOPPER);
        blockedPlaceBlocks.add(Material.BARREL);

        blockedPlaceBlocks.add(Material.CHEST);
        blockedPlaceBlocks.add(Material.TRAPPED_CHEST);

        blockedPlaceBlocks.add(Material.DISPENSER);
        blockedPlaceBlocks.add(Material.DROPPER);

    }



    // ==========================
    // Informations
    // ==========================


    public String getName() {
        return name;
    }


    public double getPricePerDay() {
        return pricePerDay;
    }



    // ==========================
    // Propriétaire
    // ==========================


    public UUID getOwner() {

        return owner;

    }


    public void setOwner(UUID owner) {

        this.owner = owner;

    }



    // ==========================
    // Expiration
    // ==========================


    public long getExpireTime() {

        return expireTime;

    }


    public void setExpireTime(long time) {

        this.expireTime = time;

    }


    public boolean isExpired() {

        return System.currentTimeMillis() > expireTime;

    }



    // ==========================
    // Trust
    // ==========================


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



    // ==========================
    // Région
    // ==========================


    public String getWorldGuardRegion() {

        return worldGuardRegion;

    }


    public void setWorldGuardRegion(String region) {

        this.worldGuardRegion = region;

    }



    public Location getPos1() {

        return pos1;

    }


    public void setPos1(Location loc) {

        this.pos1 = loc;

    }


    public Location getPos2() {

        return pos2;

    }


    public void setPos2(Location loc) {

        this.pos2 = loc;

    }



    // ==========================
    // Portes
    // ==========================


    public boolean canOpenDoors() {

        return openDoors;

    }


    public void setOpenDoors(boolean value) {

        openDoors = value;

    }



    public boolean canOpenTrapdoors() {

        return openTrapdoors;

    }


    public void setOpenTrapdoors(boolean value) {

        openTrapdoors = value;

    }



    public boolean canOpenFenceGates() {

        return openFenceGates;

    }


    public void setOpenFenceGates(boolean value) {

        openFenceGates = value;

    }



    // ==========================
    // Coffres
    // ==========================


    public boolean canOpenChests() {

        return openChests;

    }


    public void setOpenChests(boolean value) {

        openChests = value;

    }



    public boolean canOpenBarrels() {

        return openBarrels;

    }


    public void setOpenBarrels(boolean value) {

        openBarrels = value;

    }



    public boolean canOpenShulkers() {

        return openShulkers;

    }


    public void setOpenShulkers(boolean value) {

        openShulkers = value;

    }



    // ==========================
    // Utilitaires
    // ==========================


    public Set<Material> getBlockedPlaceBlocks() {

        return blockedPlaceBlocks;

    }


    public boolean isBlockedPlace(Material material) {

        return blockedPlaceBlocks.contains(material);

    }


    public void addBlockedPlace(Material material) {

        blockedPlaceBlocks.add(material);

    }


    public void removeBlockedPlace(Material material) {

        blockedPlaceBlocks.remove(material);

    }



    // ==========================
    // Autres blocs
    // ==========================


    public boolean canPlaceBlocks() {

        return placeBlocks;

    }


    public void setPlaceBlocks(boolean value) {

        placeBlocks = value;

    }



    public boolean canBreakBlocks() {

        return breakBlocks;

    }


    public void setBreakBlocks(boolean value) {

        breakBlocks = value;

    }



    public boolean canUseFurnaces() {

        return useFurnaces;

    }


    public void setUseFurnaces(boolean value) {

        useFurnaces = value;

    }



    public boolean canUseAnvils() {

        return useAnvils;

    }


    public void setUseAnvils(boolean value) {

        useAnvils = value;

    }



    public boolean canUseCrafting() {

        return useCrafting;

    }


    public void setUseCrafting(boolean value) {

        useCrafting = value;

    }



    public boolean canUseEnchanting() {

        return useEnchanting;

    }


    public void setUseEnchanting(boolean value) {

        useEnchanting = value;

    }

}
