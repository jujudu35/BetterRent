package net.betterrent.listener;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;



public class RentProtectionListener implements Listener {


    private final BetterRent plugin;



    public RentProtectionListener(BetterRent plugin) {

        this.plugin = plugin;

    }





    // =================================
    // CASSER DES BLOCS
    // =================================

    @EventHandler
    public void onBreak(BlockBreakEvent event) {


        Player player = event.getPlayer();


        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());



        if (house == null) {
            return;
        }



        // Propriétaire

        if(player.getUniqueId().equals(house.getOwner())) {

            return;

        }




        // Colocataire

        if(house.isTenant(player.getUniqueId())) {


            if(house.getPermission(
                    player.getUniqueId()
            ).canBreak()) {

                return;

            }

        }




        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas casser ici."
        );


    }








    // =================================
    // POSER DES BLOCS
    // =================================

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {


        Player player = event.getPlayer();



        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());



        if(house == null) {

            return;

        }



        Material material =
                event.getBlock()
                        .getType();





        // BLOCS INTERDITS POUR TOUT LE MONDE
        // MEME LE PROPRIETAIRE

        if(house.isBlockedPlace(material)) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Ce bloc est interdit dans une maison louée."
            );


            return;

        }





        // Propriétaire

        if(player.getUniqueId()
                .equals(house.getOwner())) {


            return;

        }






        // Colocataire

        if(house.isTenant(player.getUniqueId())) {


            if(house.getPermission(
                    player.getUniqueId()
            ).canPlace()) {


                return;

            }

        }





        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas poser de blocs ici."
        );


    }









    // =================================
    // UTILISATION DES BLOCS
    // =================================

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {


        Player player = event.getPlayer();



        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());



        if(house == null) {

            return;

        }



        if(event.getClickedBlock() == null) {

            return;

        }



        Material material =
                event.getClickedBlock()
                        .getType();





        // Propriétaire

        if(player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }





        if(house.isTenant(player.getUniqueId())) {


            if(house.getPermission(
                    player.getUniqueId()
            ).canUse()) {


                return;

            }

        }




        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas utiliser ce bloc."
        );


    }



}
