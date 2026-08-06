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





        HouseType(
                String displayName,
                double price
        ) {


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
    // CREATION
    // =================================


    public String createHouse(
            HouseType type
    ) {


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
    // SUPPRESSION
    // =================================


    public boolean deleteHouse(
            String id
    ) {


        if(!houses.containsKey(id)) {

            return false;

        }



        houses.remove(id);


        save();


        return true;


    }







    // =================================
    // GETTERS
    // =================================


    public RentHouse getHouse(
            String id
    ) {


        return houses.get(id);


    }





    public Map<String, RentHouse> getHouses() {


        return houses;


    }







    // =================================
    // MAISON A UNE POSITION
    // =================================


    public RentHouse getHouseAt(
            Location location
    ) {



        if(location == null) {

            return null;

        }





        for(RentHouse house :
                houses.values()) {


            if(house.isInside(location)) {


                return house;


            }


        }





        return null;


    }
       // =================================
    // LOUER UNE MAISON
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



        if(days <= 0) {

            return false;

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



        save();



        return true;


    }









    // =================================
    // PROLONGER LOCATION
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




        if(!house.isRented()) {

            return false;

        }




        if(days <= 0) {

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



        save();



        return true;


    }









    // =================================
    // COLOCATAIRES
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




        if(!house.isRented()) {

            return false;

        }




        boolean result =
                house.addTenant(uuid);




        if(result) {

            save();

        }




        return result;


    }







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



        save();



        return true;


    }









    // =================================
    // EXPIRATION
    // =================================


    public void clearExpired() {



        boolean changed = false;





        for(RentHouse house :
                houses.values()) {



            if(house.isExpired()) {



                house.clearRent();



                changed = true;


            }


        }






        if(changed) {


            save();


        }


    }









    // =================================
    // LIBERER UNE MAISON
    // =================================


    public boolean releaseHouse(
            String id
    ) {


        RentHouse house =
                getHouse(id);




        if(house == null) {

            return false;

        }




        house.clearRent();



        save();




        return true;


    }









    // =================================
    // NOMBRE DE MAISONS
    // =================================


    public int getHouseCount() {


        return houses.size();


    }









    // =================================
    // SAUVEGARDE RAPIDE
    // =================================


    private void save() {


        if(plugin.getHouseStorage() != null) {


            plugin.getHouseStorage()
                    .save();


        }


    }







    // =================================
    // RELOAD
    // =================================


    public void reload() {


        houses.clear();



        plugin.getHouseStorage()
                .loadHouses();




        plugin.getLogger().info(
                houses.size()
                + " maisons rechargées."
        );


    }



}
