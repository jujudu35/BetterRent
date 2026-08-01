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


        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        Block block = event.getClickedBlock();


        if (block == null) {
            return;
        }


        String type = block.getType().name();


        if (!type.contains("DOOR")
                && !type.contains("TRAPDOOR")
                && !type.contains("FENCE_GATE")) {

            return;
        }


        Player player = event.getPlayer();


        RentHouse house = regionUtil.getHouseAt(
                block.getLocation()
        );


        if (house == null) {
            return;
        }


        // Admin bypass
        if (player.isOp()) {
            return;
        }


        // Propriétaire
        if (house.getOwner() != null
                && house.getOwner().equals(player.getUniqueId())) {

            return;
        }


        // Joueur ajouté avec /rent trust
        if (house.isTrusted(player.getUniqueId())) {

            return;
        }



        // =====================
        // Portes
        // =====================

        if (type.contains("DOOR")
                && !type.contains("TRAPDOOR")) {


            if (!house.canOpenDoors()) {

                cancel(event, player);

            }

            return;
        }



        // =====================
        // Trappes
        // =====================

        if (type.contains("TRAPDOOR")) {


            if (!house.canOpenTrapdoors()) {

                cancel(event, player);

            }

            return;
        }



        // =====================
        // Portails
        // =====================

        if (type.contains("FENCE_GATE")) {


            if (!house.canOpenFenceGates()) {

                cancel(event, player);

            }

            return;
        }

    }



    private void cancel(PlayerInteractEvent event, Player player) {


        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas ouvrir ceci dans cette location."
        );

    }

}
