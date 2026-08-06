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



    private final Set<Material> blockedPlaceBlocks;




    public RentHouse(String name, double pricePerDay) {


        this.name = name;

        this.pricePerDay = pricePerDay;


        this.tenants = new ArrayList<>();

        this.permissions = new HashMap<>();

        this.blockedPlaceBlocks = new HashSet<>();


        loadDefaultBlockedBlocks();

    }



    private void loadDefaultBlockedBlocks() {


        blockedPlaceBlocks.add(Material.CHEST);

        blockedPlaceBlocks.add(Material.TRAPPED_CHEST);

        blockedPlaceBlocks.add(Material.BARREL);

        blockedPlaceBlocks.add(Material.HOPPER);

        blockedPlaceBlocks.add(Material.DISPENSER);

        blockedPlaceBlocks.add(Material.DROPPER);



        for(Material material : Material.values()) {


            if(material.name().endsWith("SHULKER_BOX")) {


                blockedPlaceBlocks.add(material);


            }

        }

    }



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
    // TEMPS LOCATION
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


        long time =
                expireTime
                - System.currentTimeMillis();


        return Math.max(time, 0);

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



    public void setWorldGuardRegion(String worldGuardRegion) {

        this.worldGuardRegion = worldGuardRegion;

    }





    public boolean isInside(Location location) {


        if(pos1 == null || pos2 == null || location == null) {

            return false;

        }



        if(!pos1.getWorld().equals(location.getWorld())) {

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
    // PERMISSIONS
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
    // BLOCS INTERDITS
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
    // CLASSE PERMISSION
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
