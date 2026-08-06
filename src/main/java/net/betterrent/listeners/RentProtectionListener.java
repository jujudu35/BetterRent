package net.betterrent.listeners;


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


        Player player =
                event.getPlayer();



        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());



        if(house == null) {

            return;

        }




        // propriétaire

        if(player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }





        // colocataire

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


        Player player =
                event.getPlayer();



        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());



        if(house == null) {

            return;

        }





        Material material =
                event.getBlock()
                        .getType();





        // BLOCS INTERDITS POUR TOUS

        if(house.isBlockedPlace(material)) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Ce bloc est interdit dans cette maison."
            );


            return;

        }







        // propriétaire

        if(player.getUniqueId()
                .equals(house.getOwner())) {


            return;

        }







        // colocataire

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
    // UTILISATION
    // =================================


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {


        Player player =
                event.getPlayer();



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








        // propriétaire

        if(player.getUniqueId()
                .equals(house.getOwner())) {


            return;

        }






        // colocataire

        if(house.isTenant(player.getUniqueId())) {


            RentHouse.RentPermission permission =
                    house.getPermission(
                            player.getUniqueId()
                    );





            // Coffres / stockage

            if(isStorage(material)) {


                if(permission.canStorage()) {

                    return;

                }

            }




            // Portes

            if(isDoor(material)) {


                if(permission.canDoors()) {

                    return;

                }

            }





            // Autres blocs

            if(permission.canUse()) {

                return;

            }


        }






        event.setCancelled(true);



        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas utiliser ce bloc."
        );


    }









    private boolean isDoor(Material material) {


        return material.name().endsWith("_DOOR")
                ||
                material.name().endsWith("_TRAPDOOR")
                ||
                material.name().endsWith("_FENCE_GATE");


    }







    private boolean isStorage(Material material) {


        return material == Material.CHEST
                ||
                material == Material.TRAPPED_CHEST
                ||
                material == Material.BARREL
                ||
                material.name().endsWith("SHULKER_BOX");


    }



}
