
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



    // ==========================
    // BREAK
    // ==========================


    @EventHandler
    public void onBreak(BlockBreakEvent event) {


        Player player = event.getPlayer();


        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());


        if(house == null) return;



        if(player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }



        if(house.isTenant(player.getUniqueId())
                &&
                house.getPermission(player.getUniqueId())
                        .canBreak()) {

            return;

        }



        event.setCancelled(true);

        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas casser ici."
        );

    }






    // ==========================
    // PLACE
    // ==========================


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {


        Player player = event.getPlayer();


        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());


        if(house == null) return;




        if(player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }




        if(house.isTenant(player.getUniqueId())
                &&
                house.getPermission(player.getUniqueId())
                        .canPlace()) {

            return;

        }



        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas poser de blocs ici."
        );


    }









    // ==========================
    // UTILISATION BLOCS
    // ==========================


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {


        Player player =
                event.getPlayer();



        RentHouse house =
                plugin.getRentManager()
                        .getHouseAt(player.getLocation());



        if(house == null) return;



        if(event.getClickedBlock() == null) return;



        Material material =
                event.getClickedBlock()
                        .getType();





        if(player.getUniqueId()
                .equals(house.getOwner())) {

            return;

        }




        if(!house.isTenant(player.getUniqueId())) {


            event.setCancelled(true);

            return;

        }





        RentHouse.RentPermission permission =
                house.getPermission(
                        player.getUniqueId()
                );







        // Portes

        if(material.name().endsWith("_DOOR")) {


            if(house.canOpenDoors()
                    && permission.canDoors()) {

                return;

            }

        }





        // Trappes

        if(material.name().endsWith("_TRAPDOOR")) {


            if(house.canOpenTrapdoors()
                    && permission.canDoors()) {

                return;

            }

        }






        // Barrières

        if(material.name().endsWith("_FENCE_GATE")) {


            if(house.canOpenFenceGates()
                    && permission.canDoors()) {

                return;

            }

        }







        // Coffres

        if(material == Material.CHEST
                ||
                material == Material.TRAPPED_CHEST) {


            if(house.canOpenChests()
                    && permission.canStorage()) {

                return;

            }

        }







        // Barils

        if(material == Material.BARREL) {


            if(house.canOpenBarrels()
                    && permission.canStorage()) {

                return;

            }

        }







        // Shulkers

        if(material.name().endsWith("SHULKER_BOX")) {


            if(house.canOpenShulkers()
                    && permission.canStorage()) {

                return;

            }

        }







        // Four

        if(material.name().endsWith("FURNACE")) {


            if(house.canUseFurnaces()
                    && permission.canUse()) {

                return;

            }

        }







        // Enclume

        if(material.name().endsWith("ANVIL")) {


            if(house.canUseAnvils()
                    && permission.canUse()) {

                return;

            }

        }







        // Table de craft

        if(material == Material.CRAFTING_TABLE) {


            if(house.canUseCrafting()
                    && permission.canUse()) {

                return;

            }

        }








        // Table enchant

        if(material == Material.ENCHANTING_TABLE) {


            if(house.canUseEnchanting()
                    && permission.canUse()) {

                return;

            }

        }







        // Autres utilisations

        if(permission.canUse()) {

            return;

        }




        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas utiliser ce bloc."
        );


    }



}
