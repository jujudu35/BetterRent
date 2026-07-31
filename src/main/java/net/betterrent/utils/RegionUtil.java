package net.betterrent.utils;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import org.bukkit.Location;

import java.util.Map;

public class RegionUtil {


    private final BetterRent plugin;


    public RegionUtil(BetterRent plugin) {

        this.plugin = plugin;

    }



    /**
     * Trouve la maison correspondant à une position
     * (sera connecté à WorldGuard ensuite)
     */
    public RentHouse getHouseAt(Location location) {


        for (Map.Entry<String, RentHouse> entry :
                plugin.getRentManager()
                        .getHouses()
                        .entrySet()) {


            RentHouse house = entry.getValue();


            // Sera remplacé par la vraie vérification WorldGuard
            // quand on ajoutera les régions


        }


        return null;

    }

}
