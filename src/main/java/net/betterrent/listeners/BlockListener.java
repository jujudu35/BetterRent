package net.betterrent.listeners;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {


    private final BetterRent plugin;


    public BlockListener(BetterRent plugin) {

        this.plugin = plugin;

    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {


        Player player = event.getPlayer();


        RentHouse house = getHouseAt(event.getBlock().getLocation());


        if (house == null) {
            return;
        }



        // OP ou propriétaire
        if (player.isOp()) {
            return;
        }


        if (house.getOwner() != null &&
                house.getOwner().equals(player.getUniqueId())) {

            return;

        }



        if (!house.canBreakBlocks()) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Vous ne pouvez pas casser de blocs dans cette maison."
            );

        }

    }





    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {


        Player player = event.getPlayer();


        RentHouse house = getHouseAt(event.getBlock().getLocation());


        if (house == null) {
            return;
        }



        // OP ou propriétaire
        if (player.isOp()) {
            return;
        }


        if (house.getOwner() != null &&
                house.getOwner().equals(player.getUniqueId())) {

            return;

        }




        if (!house.canPlaceBlocks()) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Vous ne pouvez pas poser de blocs dans cette maison."
            );

            return;

        }



        // Blocs interdits
        if (house.isBlockedPlace(
                event.getBlockPlaced().getType()
        )) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Ce bloc est interdit dans une location."
            );

        }

    }





    private RentHouse getHouseAt(Location location) {


        for (RentHouse house :
                plugin.getRentManager()
                        .getHouses()
                        .values()) {


            if (!house.hasLocation()) {
                continue;
            }


            if (!location.getWorld()
                    .equals(house.getPos1().getWorld())) {

                continue;

            }



            double minX = Math.min(
                    house.getPos1().getX(),
                    house.getPos2().getX()
            );

            double maxX = Math.max(
                    house.getPos1().getX(),
                    house.getPos2().getX()
            );


            double minY = Math.min(
                    house.getPos1().getY(),
                    house.getPos2().getY()
            );

            double maxY = Math.max(
                    house.getPos1().getY(),
                    house.getPos2().getY()
            );


            double minZ = Math.min(
                    house.getPos1().getZ(),
                    house.getPos2().getZ()
            );

            double maxZ = Math.max(
                    house.getPos1().getZ(),
                    house.getPos2().getZ()
            );



            if (location.getX() >= minX &&
                    location.getX() <= maxX &&
                    location.getY() >= minY &&
                    location.getY() <= maxY &&
                    location.getZ() >= minZ &&
                    location.getZ() <= maxZ) {


                return house;

            }

        }


        return null;

    }

}
