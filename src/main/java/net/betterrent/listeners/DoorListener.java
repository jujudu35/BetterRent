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



        if (!block.getType()
                .toString()
                .contains("DOOR")) {

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



        if (house.getOwner() == null) {
            return;
        }




        if (house.getOwner()
                .equals(player.getUniqueId())) {

            return;

        }



        if (house.isTrusted(
                player.getUniqueId()
        )) {

            return;

        }



        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Cette maison est louée."
        );

    }

}
