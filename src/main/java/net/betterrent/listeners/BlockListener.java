package net.betterrent.listeners;


import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import net.betterrent.utils.RegionUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;



public class BlockListener implements Listener {


    private final BetterRent plugin;

    private final RegionUtil regionUtil;



    public BlockListener(BetterRent plugin) {

        this.plugin = plugin;
        this.regionUtil = new RegionUtil(plugin);

    }



    // ==========================
    // CASSER DES BLOCS
    // ==========================


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {


        Player player = event.getPlayer();


        RentHouse house =
                regionUtil.getHouseAt(
                        event.getBlock().getLocation()
                );


        if (house == null) {
            return;
        }



        // OP
        if (player.isOp()) {
            return;
        }



        // Propriétaire
        if (isAllowed(player, house)) {
            return;
        }



        if (!house.canBreakBlocks()) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Vous ne pouvez pas casser de blocs dans cette location."
            );

        }

    }




    // ==========================
    // POSER DES BLOCS
    // ==========================


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {


        Player player = event.getPlayer();


        Block block = event.getBlockPlaced();



        RentHouse house =
                regionUtil.getHouseAt(
                        block.getLocation()
                );



        if (house == null) {
            return;
        }



        // OP
        if (player.isOp()) {
            return;
        }



        // Propriétaire / Trust
        if (isAllowed(player, house)) {
            return;
        }



        // Permission poser
        if (!house.canPlaceBlocks()) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Vous ne pouvez pas poser de blocs ici."
            );


            return;

        }



        // Bloc interdit
        if (house.isBlockedPlace(block.getType())) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Ce bloc est interdit dans une location."
            );


        }

    }





    private boolean isAllowed(Player player, RentHouse house) {


        if (house.getOwner() != null
                && house.getOwner()
                .equals(player.getUniqueId())) {

            return true;

        }



        return house.isTrusted(
                player.getUniqueId()
        );

    }


}
