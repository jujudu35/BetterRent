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
    public void onInventoryOpen(PlayerInteractEvent event) {


        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }



        Block block = event.getClickedBlock();


        if (block == null) {
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



        Material type = block.getType();



        switch (type) {


            // Coffres
            case CHEST:
            case TRAPPED_CHEST:

                if (!house.canOpenChests()) {

                    cancel(event, player);

                }

                break;



            // Barils
            case BARREL:

                if (!house.canOpenBarrels()) {

                    cancel(event, player);

                }

                break;



            // Shulkers
            case SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:

                if (!house.canOpenShulkers()) {

                    cancel(event, player);

                }

                break;



            // Four
            case FURNACE:
            case BLAST_FURNACE:
            case SMOKER:

                if (!house.canUseFurnaces()) {

                    cancel(event, player);

                }

                break;



            // Enclume
            case ANVIL:
            case CHIPPED_ANVIL:
            case DAMAGED_ANVIL:

                if (!house.canUseAnvils()) {

                    cancel(event, player);

                }

                break;



            // Craft
            case CRAFTING_TABLE:

                if (!house.canUseCrafting()) {

                    cancel(event, player);

                }

                break;



            // Enchantement
            case ENCHANTING_TABLE:

                if (!house.canUseEnchanting()) {

                    cancel(event, player);

                }

                break;


        }

    }





    private void cancel(PlayerInteractEvent event, Player player) {


        event.setCancelled(true);


        player.sendMessage(
                ChatColor.RED +
                "Vous ne pouvez pas utiliser ceci dans cette location."
        );

    }

}
