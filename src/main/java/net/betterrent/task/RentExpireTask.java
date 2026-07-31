package net.betterrent.task;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class RentExpireTask extends BukkitRunnable {


    private final BetterRent plugin;


    public RentExpireTask(BetterRent plugin) {

        this.plugin = plugin;

    }



    @Override
    public void run() {


        boolean changed = false;



        for (Map.Entry<String, RentHouse> entry :
                plugin.getRentManager()
                        .getHouses()
                        .entrySet()) {


            RentHouse house = entry.getValue();



            if (house.getOwner() != null
                    && house.getExpireTime() <= System.currentTimeMillis()) {



                house.setOwner(null);

                house.setExpireTime(0);


                house.getTrustedPlayers()
                        .clear();


                changed = true;



                plugin.getLogger().info(
                        "La location " +
                        house.getName() +
                        " a expiré."
                );

            }

        }



        if (changed) {

            plugin.getHouseStorage()
                    .save();

        }

    }
}
