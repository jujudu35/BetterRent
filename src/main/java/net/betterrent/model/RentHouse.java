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

        // Toutes les shulkers
        for (Material material : Material.values()) {

            if (material.name().endsWith("SHULKER_BOX")) {
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

    public boolean isExpired() {

        if (owner == null) {
            return false;
        }

        return expireTime <= System.currentTimeMillis();
    }

    public boolean isAvailable() {
        return owner == null || isExpired();
    }

    public long getRemainingTime() {

        long remaining = expireTime - System.currentTimeMillis();

        return Math.max(remaining, 0);
    }

    public void clearRent() {

        owner = null;
        expireTime = 0;

        tenants.clear();
        permissions.clear();
    }

    // ==========================
    // Région
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

    // ==========================
    // Vérifie si une position est
    // dans la maison
    // ==========================

    public boolean isInside(Location location) {

        if (pos1 == null || pos2 == null || location == null) {
            return false;
        }

        if (!pos1.getWorld().equals(location.getWorld())) {
            return false;
        }

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        return location.getBlockX() >= minX
                && location.getBlockX() <= maxX
                && location.getBlockY() >= minY
                && location.getBlockY() <= maxY
                && location.getBlockZ() >= minZ
                && location.getBlockZ() <= maxZ;
    }




// ==========================
    // Colocataires
    // ==========================

    public boolean addTenant(UUID uuid) {

        if (tenants.size() >= MAX_TENANTS) {
            return false;
        }

        if (!tenants.contains(uuid)) {

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
    // Permissions joueurs
    // ==========================

    public RentPermission getPermission(UUID uuid) {

        if (!permissions.containsKey(uuid)) {

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
    // Classe permissions
    // ==========================

    public static class RentPermission {

        // 🚪 Porte / trappe / portail
        private boolean doors = true;

        // 📦 Stockage
        private boolean storage = true;

        // ⚒️ Utilisation des blocs
        private boolean usage = true;

        // 🪨 Poser des blocs
        private boolean place = true;

        // ⛏️ Casser des blocs
        private boolean breakBlocks = true;

        public boolean canDoors() {
            return doors;
        }

        public void setDoors(boolean value) {
            this.doors = value;
        }

        public boolean canStorage() {
            return storage;
        }

        public void setStorage(boolean value) {
            this.storage = value;
        }

        public boolean canUse() {
            return usage;
        }

        public void setUse(boolean value) {
            this.usage = value;
        }

        public boolean canPlace() {
            return place;
        }

        public void setPlace(boolean value) {
            this.place = value;
        }

        public boolean canBreak() {
            return breakBlocks;
        }

        public void setBreak(boolean value) {
            this.breakBlocks = value;
        }
    }
}
