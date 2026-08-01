ackage net.betterrent.task;

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




            if (house.isExpired()) {



                plugin.getLogger().info(
                        "La maison "
                        + house.getName()
                        + " est maintenant disponible à la location."
                );



                house.clearRent();



                changed = true;


            }


        }





        if(changed) {


            plugin.getHouseStorage()
                    .save();


        }


    }


}
