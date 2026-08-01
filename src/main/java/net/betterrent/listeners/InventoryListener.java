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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;



public class InventoryListener implements Listener {


    private final BetterRent plugin;

    private final RegionUtil regionUtil;



    public InventoryListener(BetterRent plugin) {

        this.plugin = plugin;
        this.regionUtil = new RegionUtil(plugin);

    }






    @EventHandler
    public void onInteract(PlayerInteractEvent event) {


        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }



        Block block = event.getClickedBlock();


        if(block == null) {
            return;
        }



        Player player = event.getPlayer();



        RentHouse house =
                regionUtil.getHouseAt(
                        block.getLocation()
                );



        if(house == null) {
            return;
        }



        // OP

        if(player.isOp()) {
            return;
        }






        // Propriétaire

        if(house.getOwner() != null
                && house.getOwner()
                .equals(player.getUniqueId())) {

            return;

        }






        // Pas colocataire

        if(!house.isTrusted(player.getUniqueId())) {


            cancel(event, player);

            return;

        }







        RentHouse.RentPermission permission =
                house.getPermission(
                        player.getUniqueId()
                );





        Material type = block.getType();





        // ==========================
        // STOCKAGE
        // ==========================


        if(type == Material.CHEST
                || type == Material.TRAPPED_CHEST
                || type == Material.BARREL
                || type.name().endsWith("SHULKER_BOX")) {



            if(!permission.canStorage()) {


                cancel(event, player);


            }


            return;

        }







        // ==========================
        // UTILISATION BLOCS
        // ==========================


        if(type == Material.FURNACE
                || type == Material.BLAST_FURNACE
                || type == Material.SMOKER

                || type == Material.ANVIL
                || type == Material.CHIPPED_ANVIL
                || type == Material.DAMAGED_ANVIL

                || type == Material.CRAFTING_TABLE

                || type == Material.ENCHANTING_TABLE) {



            if(!permission.canUse()) {


                cancel(event, player);


            }


        }


    }








    private void cancel(
            PlayerInteractEvent event,
            Player player
    ) {



        event.setCancelled(true);



        player.sendMessage(
                ChatColor.RED +
                "Vous n'avez pas la permission d'utiliser ceci dans cette location."
        );


    }


}
