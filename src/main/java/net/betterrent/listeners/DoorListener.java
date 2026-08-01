package net.betterrent.listeners;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import net.betterrent.utils.RegionUtil;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;


public class DoorListener implements Listener {


    private final BetterRent plugin;
    private final RegionUtil regionUtil;



    public DoorListener(BetterRent plugin) {

        this.plugin = plugin;
        this.regionUtil = new RegionUtil(plugin);

    }





    @EventHandler
    public void onDoorOpen(PlayerInteractEvent event) {


        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }



        Block block = event.getClickedBlock();


        if(block == null) {
            return;
        }



        String type = block.getType().name();



        if(!type.contains("DOOR")
                && !type.contains("TRAPDOOR")
                && !type.contains("FENCE_GATE")) {

            return;

        }




        Player player = event.getPlayer();



        RentHouse house = regionUtil.getHouseAt(
                block.getLocation()
        );



        if(house == null) {
            return;
        }




        // Admin

        if(player.isOp()) {
            return;
        }





        // Propriétaire

        if(house.getOwner() != null
                && house.getOwner().equals(player.getUniqueId())) {

            return;

        }





        // Pas locataire

        if(!house.isTrusted(player.getUniqueId())) {


            cancel(event, player);
            return;

        }






        // Permission du joueur

        RentHouse.RentPermission permission =
                house.getPermission(player.getUniqueId());






        // Porte

        if(type.contains("DOOR")
                && !type.contains("TRAPDOOR")) {


            if(!permission.canDoors()) {

                cancel(event, player);

            }


            return;

        }







        // Trappe

        if(type.contains("TRAPDOOR")) {


            if(!permission.canDoors()) {

                cancel(event, player);

            }


            return;

        }







        // Portail

        if(type.contains("FENCE_GATE")) {


            if(!permission.canDoors()) {

                cancel(event, player);

            }


        }


    }







    private void cancel(PlayerInteractEvent event, Player player) {


        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous n'avez pas la permission d'ouvrir ceci."
        );


    }


}
