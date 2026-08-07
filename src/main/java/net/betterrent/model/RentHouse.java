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



    private final List<UUID> tenants;

    private static final int MAX_TENANTS = 5;


    private final Map<UUID, RentPermission> permissions;



    // BLOCS INTERDITS A LA POSE
    private final Set<Material> blockedPlaceBlocks;



    // ==========================
    // SETTINGS MAISON
    // ==========================

    private boolean openDoors = true;

    private boolean openTrapdoors = true;

    private boolean openFenceGates = true;


    private boolean placeBlocks = true;

    private boolean breakBlocks = true;


    private boolean openChests = true;

    private boolean openBarrels = true;

    private boolean openShulkers = true;


    private boolean useFurnaces = true;

    private boolean useAnvils = true;

    private boolean useCrafting = true;

    private boolean useEnchanting = true;



    public RentHouse(
            String name,
            double pricePerDay
    ) {


        this.name = name;

        this.pricePerDay = pricePerDay;


        this.tenants = new ArrayList<>();

        this.permissions = new HashMap<>();

        this.blockedPlaceBlocks = new HashSet<>();


        loadBlockedBlocks();

    }





    // ==========================
    // BLOCS INTERDITS
    // ==========================


    private void loadBlockedBlocks() {


        for(Material material : Material.values()) {


            String id = material.name();



            // Tous les coffres
            if(id.contains("CHEST")) {

                blockedPlaceBlocks.add(material);

            }



            // Toutes les shulkers

            if(id.contains("SHULKER")) {

                blockedPlaceBlocks.add(material);

            }


        }



        blockedPlaceBlocks.add(Material.BARREL);

        blockedPlaceBlocks.add(Material.HOPPER);

        blockedPlaceBlocks.add(Material.DISPENSER);

        blockedPlaceBlocks.add(Material.DROPPER);


    }





    public boolean isBlockedPlace(Material material) {


        return blockedPlaceBlocks.contains(material);

    }



    public Set<Material> getBlockedPlaceBlocks() {


        return blockedPlaceBlocks;

    }



    public void addBlockedPlace(Material material) {


        blockedPlaceBlocks.add(material);

    }



    public void removeBlockedPlace(Material material) {


        blockedPlaceBlocks.remove(material);

    }





    // ==========================
    // INFOS
    // ==========================


    public String getName() {


        return name;

    }



    public double getPricePerDay() {


        return pricePerDay;

     }
       // ==========================
    // PROPRIETAIRE
    // ==========================


    public UUID getOwner() {


        return owner;

    }



    public void setOwner(UUID owner) {


        this.owner = owner;

    }





    // ==========================
    // LOCATION
    // ==========================


    public long getExpireTime() {


        return expireTime;

    }



    public void setExpireTime(long expireTime) {


        this.expireTime = expireTime;

    }



    public boolean isRented() {


        return owner != null;

    }



    public boolean isExpired() {


        if(owner == null) {

            return false;

        }


        return expireTime <= System.currentTimeMillis();

    }



    public boolean isAvailable() {


        return owner == null || isExpired();

    }



    public long getRemainingTime() {


        return Math.max(
                expireTime - System.currentTimeMillis(),
                0
        );

    }



    public void clearRent() {


        owner = null;

        expireTime = 0;


        tenants.clear();

        permissions.clear();


    }





    // ==========================
    // REGION
    // ==========================


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





    public boolean isInside(Location location) {


        if(location == null
                || pos1 == null
                || pos2 == null) {

            return false;

        }



        if(!location.getWorld()
                .equals(pos1.getWorld())) {

            return false;

        }




        int minX = Math.min(
                pos1.getBlockX(),
                pos2.getBlockX()
        );


        int maxX = Math.max(
                pos1.getBlockX(),
                pos2.getBlockX()
        );



        int minY = Math.min(
                pos1.getBlockY(),
                pos2.getBlockY()
        );


        int maxY = Math.max(
                pos1.getBlockY(),
                pos2.getBlockY()
        );



        int minZ = Math.min(
                pos1.getBlockZ(),
                pos2.getBlockZ()
        );


        int maxZ = Math.max(
                pos1.getBlockZ(),
                pos2.getBlockZ()
        );



        return location.getBlockX() >= minX
                && location.getBlockX() <= maxX

                && location.getBlockY() >= minY
                && location.getBlockY() <= maxY

                && location.getBlockZ() >= minZ
                && location.getBlockZ() <= maxZ;


    }





    // ==========================
    // COLOCATAIRES
    // ==========================


    public boolean addTenant(UUID uuid) {


        if(tenants.size() >= MAX_TENANTS) {

            return false;

        }



        if(!tenants.contains(uuid)) {


            tenants.add(uuid);


            permissions.put(
                    uuid,
                    new RentPermission()
            );


            return true;

        }


        return false;

    }





    public void removeTenant(UUID uuid) {


        tenants.remove(uuid);

        permissions.remove(uuid);


    }




    public boolean isTenant(UUID uuid) {


        return tenants.contains(uuid);

    }




    public List<UUID> getTenants() {


        return tenants;

    }




    public int getTenantCount() {


        return tenants.size();

    }




    public int getMaxTenants() {


        return MAX_TENANTS;

    }
     // ==========================
    // PERMISSIONS COLOCATAIRES
    // ==========================


    public RentPermission getPermission(UUID uuid) {


        if(!permissions.containsKey(uuid)) {


            permissions.put(
                    uuid,
                    new RentPermission()
            );


        }


        return permissions.get(uuid);

    }




    public Map<UUID, RentPermission> getPermissions() {


        return permissions;

    }







    // ==========================
    // SETTINGS MAISON
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








    // ==========================
    // CLASSE PERMISSIONS
    // ==========================


    public static class RentPermission {


        private boolean doors = true;

        private boolean storage = true;

        private boolean usage = true;

        private boolean place = true;

        private boolean breakBlocks = true;




        public boolean canDoors() {

            return doors;

        }



        public void setDoors(boolean value) {

            doors = value;

        }





        public boolean canStorage() {

            return storage;

        }



        public void setStorage(boolean value) {

            storage = value;

        }





        public boolean canUse() {

            return usage;

        }



        public void setUse(boolean value) {

            usage = value;

        }





        public boolean canPlace() {

            return place;

        }



        public void setPlace(boolean value) {

            place = value;

        }





        public boolean canBreak() {

            return breakBlocks;

        }



        public void setBreak(boolean value) {

            breakBlocks = value;

        }


    }


}
