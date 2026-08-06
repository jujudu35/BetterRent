package net.betterrent.listener;


import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;



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



        // propriétaire

        if (player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }



        // colocataire

        if (house.isTenant(player.getUniqueId())) {


            if (house.getPermission(
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



        if (house == null) {
            return;
        }




        Material material =
                event.getBlock()
                        .getType();




        // Blocs interdits

        if (house.isBlockedPlace(material)
                && !player.getUniqueId()
                .equals(house.getOwner())) {


            event.setCancelled(true);


            player.sendMessage(
                    ChatColor.RED +
                    "Ce bloc est interdit dans cette maison."
            );


            return;

        }






        if (player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }




        if (house.isTenant(
                player.getUniqueId()
        )) {


            if (house.getPermission(
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
    // INTERACTION (portes/coffres)
    // =================================


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {


        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        Player player =
                event.getPlayer();



        Block block =
                event.getClickedBlock();



        if(block == null) {
            return;
        }




        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(
                                block.getLocation()
                        );



        if(house == null) {
            return;
        }





        // propriétaire

        if(player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }






        if(house.isTenant(
                player.getUniqueId()
        )) {


            RentHouse.RentPermission permission =
                    house.getPermission(
                            player.getUniqueId()
                    );



            // portes

            if(isDoor(block.getType())
                    && permission.canDoors()) {

                return;

            }



            // stockage

            if(isStorage(block.getType())
                    && permission.canStorage()) {

                return;

            }



            if(permission.canUse()) {

                return;

            }

        }




        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous n'avez pas accès à cette maison."
        );


    }







    private boolean isDoor(Material material) {


        return material.name()
                .endsWith("_DOOR")
                ||
                material.name()
                .endsWith("_TRAPDOOR")
                ||
                material.name()
                .endsWith("_FENCE_GATE");


    }







    private boolean isStorage(Material material) {


        return material == Material.CHEST
                ||
                material == Material.TRAPPED_CHEST
                ||
                material == Material.BARREL
                ||
                material.name()
                .endsWith("SHULKER_BOX");


    }



}
