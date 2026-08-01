package net.betterrent.model;


import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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



    // =========================
    // PERMISSIONS
    // =========================


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



    // =========================
    // BLOCS INTERDITS
    // =========================


    private final Set<Material> blockedPlaceBlocks;




    public RentHouse(String name, double pricePerDay) {


        this.name = name;

        this.pricePerDay = pricePerDay;


        this.trustedPlayers = new ArrayList<>();


        this.blockedPlaceBlocks = new HashSet<>();


        blockedPlaceBlocks.add(Material.HOPPER);
        blockedPlaceBlocks.add(Material.BARREL);
        blockedPlaceBlocks.add(Material.CHEST);
        blockedPlaceBlocks.add(Material.TRAPPED_CHEST);
        blockedPlaceBlocks.add(Material.DISPENSER);
        blockedPlaceBlocks.add(Material.DROPPER);

    }





    // =========================
    // LOCATION
    // =========================


    public boolean isInside(Location location) {


        if (pos1 == null || pos2 == null) {

            return false;

        }



        if (!location.getWorld()
                .equals(pos1.getWorld())) {

            return false;

        }



        double minX = Math.min(pos1.getX(), pos2.getX());
        double maxX = Math.max(pos1.getX(), pos2.getX());


        double minY = Math.min(pos1.getY(), pos2.getY());
        double maxY = Math.max(pos1.getY(), pos2.getY());


        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());



        return location.getX() >= minX
                && location.getX() <= maxX

                && location.getY() >= minY
                && location.getY() <= maxY

                && location.getZ() >= minZ
                && location.getZ() <= maxZ;

    }







    // =========================
    // LOCATION SET
    // =========================


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








    // =========================
    // LOCATION RENT
    // =========================


    public boolean isRented() {


        return owner != null
                && expireTime > System.currentTimeMillis();


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








    // =========================
    // TRUST
    // =========================


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





    public void clearTrusted() {

        trustedPlayers.clear();

    }





    public List<UUID> getTrustedPlayers() {

        return trustedPlayers;

    }









    // =========================
    // DOORS
    // =========================


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









    // =========================
    // BLOCS
    // =========================


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






    // =========================
    // INVENTAIRES
    // =========================


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








    // =========================
    // BLOCS INTERDITS
    // =========================


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






    // =========================
    // INFO
    // =========================


    public String getName() {

        return name;

    }


    public double getPricePerDay() {

        return pricePerDay;

    }



    public String getWorldGuardRegion() {

        return worldGuardRegion;

    }



    public void setWorldGuardRegion(String region) {

        this.worldGuardRegion = region;

    }


}
