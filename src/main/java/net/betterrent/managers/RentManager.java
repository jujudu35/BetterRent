package net.betterrent.managers;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RentManager {

    private final BetterRent plugin;

    private final Map<String, RentHouse> houses;


    public RentManager(BetterRent plugin) {

        this.plugin = plugin;
        this.houses = new HashMap<>();

    }


    /**
     * Crée une nouvelle maison
     */
    public boolean createHouse(String name, double pricePerDay) {

        if (houses.containsKey(name.toLowerCase())) {
            return false;
        }

        RentHouse house = new RentHouse(name, pricePerDay);

        houses.put(name.toLowerCase(), house);

        return true;
    }


    /**
     * Supprime une maison
     */
    public boolean deleteHouse(String name) {

        return houses.remove(name.toLowerCase()) != null;

    }


    /**
     * Récupère une maison
     */
    public RentHouse getHouse(String name) {

        return houses.get(name.toLowerCase());

    }


    /**
     * Vérifie si une maison existe
     */
    public boolean exists(String name) {

        return houses.containsKey(name.toLowerCase());

    }


    /**
     * Loue une maison
     */
    public boolean rentHouse(String name, UUID player, int days) {

        RentHouse house = getHouse(name);

        if (house == null) {
            return false;
        }


        if (house.isRented()) {
            return false;
        }


        house.setOwner(player);

        long time = System.currentTimeMillis();

        long duration = days * 24L * 60L * 60L * 1000L;


        house.setExpireTime(time + duration);


        return true;
    }


    /**
     * Ajoute un joueur de confiance
     */
    public boolean trustPlayer(String houseName, UUID uuid) {

        RentHouse house = getHouse(houseName);

        if (house == null) {
            return false;
        }


        house.addTrusted(uuid);

        return true;
    }


    /**
     * Retire un joueur de confiance
     */
    public boolean untrustPlayer(String houseName, UUID uuid) {

        RentHouse house = getHouse(houseName);

        if (house == null) {
            return false;
        }


        house.removeTrusted(uuid);

        return true;
    }


    /**
     * Vérifie l'accès d'un joueur
     */
    public boolean hasAccess(String houseName, UUID uuid) {

        RentHouse house = getHouse(houseName);

        if (house == null) {
            return false;
        }


        if (house.getOwner() != null &&
                house.getOwner().equals(uuid)) {

            return true;
        }


        return house.isTrusted(uuid);

    }


    public Map<String, RentHouse> getHouses() {

        return houses;

    }
}
