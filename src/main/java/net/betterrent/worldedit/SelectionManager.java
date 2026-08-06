package net.betterrent.worldedit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class SelectionManager {


    private final BetterRent plugin;



    public SelectionManager(BetterRent plugin) {

        this.plugin = plugin;

    }







    private Region getSelection(Player player) {


        WorldEditPlugin worldEdit =
                plugin.getHookManager()
                        .getWorldEditHook()
                        .getWorldEdit();



        if(worldEdit == null) {

            return null;

        }



        try {


            LocalSession session =
                    worldEdit.getSession(player);



            return session.getSelection(
                    BukkitAdapter.adapt(
                            player.getWorld()
                    )
            );



        } catch(Exception e) {

            return null;

        }


    }









    public Location getPos1(Player player) {


        Region region = getSelection(player);



        if(region == null) {

            return null;

        }



        BlockVector3 min =
                region.getMinimumPoint();



        return new Location(
                player.getWorld(),
                min.x(),
                min.y(),
                min.z()
        );


    }









    public Location getPos2(Player player) {


        Region region = getSelection(player);



        if(region == null) {

            return null;

        }



        BlockVector3 max =
                region.getMaximumPoint();



        return new Location(
                player.getWorld(),
                max.x(),
                max.y(),
                max.z()
        );


    }









    // =================================
    // ENREGISTRER LA REGION DANS UNE MAISON
    // =================================


    public boolean saveSelection(
            Player player,
            RentHouse house
    ) {


        Location pos1 =
                getPos1(player);



        Location pos2 =
                getPos2(player);



        if(pos1 == null || pos2 == null) {

            return false;

        }



        house.setPos1(pos1);

        house.setPos2(pos2);



        return true;

    }



}
