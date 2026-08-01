package net.betterrent.listeners;


import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import net.betterrent.utils.RegionUtil;

import org.bukkit.ChatColor;
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



        // OP bypass
        if(player.isOp()) {
            return;
        }



        // Propriétaire
        if(house.getOwner() != null
                && house.getOwner()
                .equals(player.getUniqueId())) {

            return;

        }



        // Colocataire
        if(house.isTrusted(player.getUniqueId())) {


            RentHouse.RentPermission permission =
                    house.getPermission(player.getUniqueId());



            if(permission.canBreak()) {

                return;

            }


        }



        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous n'avez pas la permission de casser des blocs ici."
        );


    }







    // ==========================
    // POSER DES BLOCS
    // ==========================


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {


        Player player = event.getPlayer();



        RentHouse house =
                regionUtil.getHouseAt(
                        event.getBlockPlaced()
                        .getLocation()
                );



        if(house == null) {
            return;
        }



        // OP bypass

        if(player.isOp()) {
            return;
        }



        // Propriétaire

        if(house.getOwner() != null
                && house.getOwner()
                .equals(player.getUniqueId())) {

            return;

        }



        // Colocataire

        if(house.isTrusted(player.getUniqueId())) {


            RentHouse.RentPermission permission =
                    house.getPermission(player.getUniqueId());



            if(!permission.canPlace()) {


                event.setCancelled(true);


                player.sendMessage(
                        ChatColor.RED +
                        "Vous n'avez pas la permission de poser des blocs ici."
                );


                return;

            }


        } else {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Vous n'avez pas accès à cette maison."
            );


            return;

        }






        // ==========================
        // BLOCS INTERDITS
        // ==========================


        if(house.isBlockedPlace(
                event.getBlockPlaced()
                .getType())) {



            event.setCancelled(true);



            player.sendMessage(
                    ChatColor.RED +
                    "Ce bloc est interdit dans une location."
            );


        }



    }



}
