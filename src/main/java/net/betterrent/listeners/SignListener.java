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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    private final BetterRent plugin;
    private final RegionUtil regionUtil;

    public SignListener(BetterRent plugin) {
        this.plugin = plugin;
        this.regionUtil = new RegionUtil(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Player player = event.getPlayer();

        RentHouse house = regionUtil.getHouseAt(
                block.getLocation()
        );

        if (house == null) {
            return;
        }

        // ==========================
        // OP
        // ==========================

        if (player.isOp()) {
            return;
        }

        // ==========================
        // PROPRIETAIRE / TRUST
        // ==========================

        if (isAllowed(player, house)) {
            return;
        }

        Material type = block.getType();

        // ==========================
        // COFFRES
        // ==========================

        if (type == Material.CHEST
                || type == Material.TRAPPED_CHEST) {

            if (!house.canOpenChests()) {
                cancel(event, player);
            }

            return;
        }

        // ==========================
        // BARILS
        // ==========================

        if (type == Material.BARREL) {

            if (!house.canOpenBarrels()) {
                cancel(event, player);
            }

            return;
        }

        // ==========================
        // SHULKERS
        // ==========================

        if (type.name().endsWith("SHULKER_BOX")) {

            if (!house.canOpenShulkers()) {
                cancel(event, player);
            }

            return;
        }

        // ==========================
        // FOURS
        // ==========================

        if (type == Material.FURNACE
                || type == Material.BLAST_FURNACE
                || type == Material.SMOKER) {

            if (!house.canUseFurnaces()) {
                cancel(event, player);
            }

            return;
        }

        // ==========================
        // ENCLUMES
        // ==========================

        if (type == Material.ANVIL
                || type == Material.CHIPPED_ANVIL
                || type == Material.DAMAGED_ANVIL) {

            if (!house.canUseAnvils()) {
                cancel(event, player);
            }

            return;
        }

        // ==========================
        // TABLE DE CRAFT
        // ==========================

        if (type == Material.CRAFTING_TABLE) {

            if (!house.canUseCrafting()) {
                cancel(event, player);
            }

            return;
        }

        // ==========================
        // TABLE D'ENCHANTEMENT
        // ==========================

        if (type == Material.ENCHANTING_TABLE) {

            if (!house.canUseEnchanting()) {
                cancel(event, player);
            }

            return;
        }
    }

    // ==========================
    // JOUEUR AUTORISE
    // ==========================

    private boolean isAllowed(
            Player player,
            RentHouse house
    ) {

        if (house.getOwner() != null
                && house.getOwner().equals(
                        player.getUniqueId()
                )) {

            return true;
        }

        return house.isTrusted(
                player.getUniqueId()
        );
    }

    // ==========================
    // BLOQUER
    // ==========================

    private void cancel(
            PlayerInteractEvent event,
            Player player
    ) {

        event.setCancelled(true);

        player.sendMessage(
                ChatColor.RED
                        + "Vous ne pouvez pas utiliser ceci dans cette location."
        );
    }
}
