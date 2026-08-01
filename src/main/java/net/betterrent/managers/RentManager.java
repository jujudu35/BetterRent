package net.betterrent.managers;


import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import org.bukkit.Location;

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
     * Crée une maison
     */
    public boolean createHouse(String name, double pricePerDay) {


        if (houses.containsKey(name.toLowerCase())) {

            return false;

        }



        RentHouse house =
                new RentHouse(
                        name,
                        pricePerDay
                );



        houses.put(
                name.toLowerCase(),
                house
        );



        return true;

    }






    /**
     * Supprime une maison
     */
    public boolean deleteHouse(String name) {


        return houses.remove(
                name.toLowerCase()
        ) != null;


    }







    /**
     * Récupérer une maison
     */
    public RentHouse getHouse(String name) {


        return houses.get(
                name.toLowerCase()
        );


    }







    /**
     * Liste des maisons
     */
    public Map<String, RentHouse> getHouses() {


        return houses;


    }







    /**
     * Vérifie existence
     */
    public boolean exists(String name) {


        return houses.containsKey(
                name.toLowerCase()
        );


    }







    /**
     * Louer une maison
     */
    public boolean rentHouse(
            String name,
            UUID player,
            int days
    ) {



        RentHouse house =
                getHouse(name);



        if (house == null) {

            return false;

        }





        if (house.isRented()) {

            return false;

        }






        house.setOwner(player);



        long expire =
                System.currentTimeMillis()
                + (days * 86400000L);




        house.setExpireTime(expire);




        return true;

    }








    /**
     * Libérer une maison
     */
    public boolean unrentHouse(String name) {


        RentHouse house =
                getHouse(name);



        if (house == null) {

            return false;

        }



        house.setOwner(null);

        house.setExpireTime(0);



        house.clearTrusted();



        return true;

    }








    /**
     * Trouve une maison avec une position
     */
    public RentHouse getHouseAt(Location location) {


        for (RentHouse house : houses.values()) {


            if (house.isInside(location)) {

                return house;

            }

        }


        return null;

    }








    /**
     * Ajouter confiance
     */
    public boolean trustPlayer(
            String houseName,
            UUID uuid
    ) {


        RentHouse house =
                getHouse(houseName);



        if (house == null) {

            return false;

        }



        house.addTrusted(uuid);



        return true;

    }








    /**
     * Retirer confiance
     */
    public boolean untrustPlayer(
            String houseName,
            UUID uuid
    ) {


        RentHouse house =
                getHouse(houseName);



        if (house == null) {

            return false;

        }



        house.removeTrusted(uuid);



        return true;

    }







    /**
     * Vérifie accès
     */
    public boolean hasAccess(
            String houseName,
            UUID uuid
    ) {


        RentHouse house =
                getHouse(houseName);



        if (house == null) {

            return false;

        }




        if (house.getOwner() != null
                && house.getOwner().equals(uuid)) {


            return true;

        }



        return house.isTrusted(uuid);


    }



}
