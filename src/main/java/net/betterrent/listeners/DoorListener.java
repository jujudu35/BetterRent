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



        RentHouse house =
                regionUtil.getHouseAt(
                        block.getLocation()
                );



        if (house == null) {
            return;
        }



        // OP bypass
        if (player.isOp()) {
            return;
        }



        // Propriétaire bypass
        if (house.getOwner() != null &&
                house.getOwner()
                        .equals(player.getUniqueId())) {

            return;

        }



        // Joueur trust bypass
        if (house.isTrusted(
                player.getUniqueId()
        )) {

            return;

        }



        // Vérification permissions

        if (type.contains("DOOR")
                && !house.canOpenDoors()) {

            cancel(event, player);
            return;

        }



        if (type.contains("TRAPDOOR")
                && !house.canOpenTrapdoors()) {

            cancel(event, player);
            return;

        }



        if (type.contains("FENCE_GATE")
                && !house.canOpenFenceGates()) {

            cancel(event, player);
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
