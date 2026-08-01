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





    // =================================
    // TYPES DE MAISONS
    // =================================


    public enum HouseType {


        MAISON_PAUVRE(
                "Maison pauvre",
                10000
        ),


        GRANDE_MAISON_PAUVRE(
                "Grande maison pauvre",
                50000
        ),


        MAISON_MODERNE(
                "Maison moderne",
                100000
        ),


        GRANDE_MAISON_MODERNE(
                "Grande maison moderne",
                500000
        ),


        MAISON_RICHE(
                "Maison riche",
                1000000
        ),


        GRANDE_MAISON_RICHE(
                "Grande maison riche",
                2500000
        );



        private final String displayName;

        private final double price;





        HouseType(String displayName, double price) {

            this.displayName = displayName;

            this.price = price;

        }





        public String getDisplayName() {

            return displayName;

        }





        public double getPrice() {

            return price;

        }


    }







    // =================================
    // CREATION MAISON PAR TYPE
    // =================================


    public String createHouse(HouseType type) {



        int number = 1;


        String id;



        do {


            id =
                    type.name().toLowerCase()
                    + "_"
                    + number;


            number++;


        } while(houses.containsKey(id));






        RentHouse house =
                new RentHouse(
                        type.getDisplayName(),
                        type.getPrice()
                );




        houses.put(
                id,
                house
        );



        return id;


    }








    // =================================
    // CREATION MAISON MANUELLE
    // /rent create <nom> <prix>
    // =================================


    public boolean createHouse(
            String name,
            double price
    ) {



        if(houses.containsKey(name)) {

            return false;

        }





        RentHouse house =
                new RentHouse(
                        name,
                        price
                );




        houses.put(
                name,
                house
        );



        return true;


    }









    // =================================
    // SUPPRESSION
    // =================================


    public boolean deleteHouse(String id) {


        return houses.remove(id) != null;


    }









    // =================================
    // RECUPERATION
    // =================================


    public RentHouse getHouse(String id) {


        return houses.get(id);


    }







    public Map<String, RentHouse> getHouses() {


        return houses;


    }









    // =================================
    // TROUVER UNE MAISON
    // =================================


    public RentHouse getHouseAt(Location location) {



        for(RentHouse house : houses.values()) {



            if(house.isInside(location)) {

                return house;

            }


        }



        return null;


    }









    // =================================
    // LOCATION
    // =================================


    public boolean rentHouse(
            String id,
            UUID player,
            int days
    ) {



        RentHouse house =
                getHouse(id);



        if(house == null) {

            return false;

        }





        if(!house.isAvailable()) {

            return false;

        }






        // Nettoyage ancienne location

        if(house.isExpired()) {

            house.clearRent();

        }







        house.setOwner(player);





        long duration =
                days
                * 24L
                * 60L
                * 60L
                * 1000L;






        house.setExpireTime(
                System.currentTimeMillis()
                + duration
        );




        return true;


    }









    / =================================
    // RENOUVELER UNE LOCATION
    // =================================


    public boolean extendRent(
            String id,
            int days
    ) {


        RentHouse house =
                getHouse(id);



        if(house == null) {

            return false;

        }





        if(house.isExpired()) {

            return false;

        }







        long duration =
                days
                * 24L
                * 60L
                * 60L
                * 1000L;





        house.setExpireTime(
                house.getExpireTime()
                + duration
        );



        return true;


    }









    // =================================
    // AJOUTER UN COLOCATAIRE
    // =================================


    public boolean addTenant(
            String id,
            UUID uuid
    ) {



        RentHouse house =
                getHouse(id);



        if(house == null) {

            return false;

        }





        return house.addTenant(uuid);


    }









    // =================================
    // RETIRER UN COLOCATAIRE
    // =================================


    public boolean removeTenant(
            String id,
            UUID uuid
    ) {



        RentHouse house =
                getHouse(id);



        if(house == null) {

            return false;

        }





        house.removeTenant(uuid);



        return true;


    }









    // =================================
    // VERIFIER PROPRIETAIRE
    // =================================


    public boolean isOwner(
            String id,
            UUID uuid
    ) {


        RentHouse house =
                getHouse(id);



        if(house == null) {

            return false;

        }




        return house.getOwner() != null
                && house.getOwner()
                .equals(uuid);


    }









    // =================================
    // EXPIRATION DES LOCATIONS
    // =================================


    public void clearExpired() {



        for(RentHouse house : houses.values()) {



            if(house.isExpired()) {


                house.clearRent();


            }


        }


    }









    // =================================
    // FORCER LA LIBERATION
    // =================================


    public boolean releaseHouse(String id) {



        RentHouse house =
                getHouse(id);



        if(house == null) {

            return false;

        }



        house.clearRent();



        return true;


    }









    // =================================
    // COMPTER MAISONS
    // =================================


    public int getHouseCount() {


        return houses.size();


    }









    // =================================
    // DEBUG
    // =================================


    public void reload() {


        plugin.getLogger()
                .info(
                        houses.size()
                        + " maisons chargées."
                );


    }



}
