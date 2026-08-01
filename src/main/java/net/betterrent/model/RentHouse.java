package net.betterrent.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;


public class RentHouse {


    private final String name;

    private final double pricePerDay;



    // ==========================
    // Location
    // ==========================

    private UUID owner;

    private long expireTime;



    // ==========================
    // Région
    // ==========================

    private Location pos1;

    private Location pos2;

    private String worldGuardRegion;





    // ==========================
    // Colocataires
    // ==========================

    private final List<UUID> tenants;


    private static final int MAX_TENANTS = 5;





    // Permissions des joueurs

    private final Map<UUID, RentPermission> permissions;





    // ==========================
    // Blocs interdits
    // ==========================

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


        // Coffres

        blockedPlaceBlocks.add(Material.CHEST);

        blockedPlaceBlocks.add(Material.TRAPPED_CHEST);



        // Stockage

        blockedPlaceBlocks.add(Material.BARREL);

        blockedPlaceBlocks.add(Material.HOPPER);

        blockedPlaceBlocks.add(Material.DISPENSER);

        blockedPlaceBlocks.add(Material.DROPPER);



        // Shulkers toutes couleurs

        for(Material material : Material.values()) {


            if(material.name().contains("SHULKER_BOX")) {

                blockedPlaceBlocks.add(material);

            }

        }


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
// Temps
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





/**
 * Vérifie si la location est terminée
 */
public boolean isExpired() {


    if(owner == null) {

        return false;

    }


    return expireTime <= System.currentTimeMillis();

}






/**
 * Vérifie si la maison peut être louée
 */
public boolean isAvailable() {


    return owner == null || isExpired();


}







/**
 * Temps restant en millisecondes
 */
public long getRemainingTime() {


    long remaining =
            expireTime - System.currentTimeMillis();



    if(remaining < 0) {

        return 0;

    }


    return remaining;

}








/**
 * Libère complètement la maison
 */
public void clearRent() {


    owner = null;


    expireTime = 0;


    tenants.clear();


    permissions.clear();


}
    // ==========================
    // Colocataires
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
    // Permissions joueur
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
    // Région WorldEdit
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








    // ==========================
    // Blocs interdits
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
// COMPATIBILITÉ LISTENERS
// ==========================

public boolean canOpenDoors() {

    return true;

}


public boolean canOpenTrapdoors() {

    return true;

}


public boolean canOpenFenceGates() {

    return true;

}



public boolean canPlaceBlocks() {

    return true;

}



public boolean canBreakBlocks() {

    return true;

}



public boolean canOpenChests() {

    return true;

}



public boolean canOpenBarrels() {

    return true;

}



public boolean canOpenShulkers() {

    return false;

}



public boolean canUseFurnaces() {

    return true;

}



public boolean canUseAnvils() {

    return true;

}



public boolean canUseCrafting() {

    return true;

}



public boolean canUseEnchanting() {

    return true;

}
    // ==========================
    // Classe permissions
    // ==========================


    public static class RentPermission {


        // 🚪 Porte / trappe / portail

        private boolean doors = true;



        // 📦 Stockage

        private boolean storage = true;



        // ⚒️ Utilisation blocs

        private boolean usage = true;



        // 🪨 Poser

        private boolean place = true;



        // ⛏️ Casser

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
