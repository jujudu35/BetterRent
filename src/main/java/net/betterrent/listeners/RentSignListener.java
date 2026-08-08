package net.betterrent.listeners;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class RentSignListener implements Listener {

    private final BetterRent plugin;

    // Admins qui sont en attente de création d'un panneau
    private final HashMap<UUID, String> waitingSigns =
            new HashMap<>();

    // Joueurs ayant ouvert le menu de location
    private final HashMap<UUID, String> rentingHouse =
            new HashMap<>();

    public RentSignListener(BetterRent plugin) {
        this.plugin = plugin;
    }

    // ==========================
    // METTRE UN ADMIN EN ATTENTE
    // ==========================

    public void setWaitingSign(
            Player player,
            String houseId
    ) {

        waitingSigns.put(
                player.getUniqueId(),
                houseId
        );
    }

    // ==========================
    // CLIQUE SUR UN PANNEAU
    // ==========================

    @EventHandler
    public void onSignClick(
            PlayerInteractEvent event
    ) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        Material type = block.getType();

        if (!type.name().endsWith("SIGN")) {
            return;
        }

        Player player = event.getPlayer();

        Sign sign = (Sign) block.getState();

        String houseId = getHouseId(sign);

        // ==========================
        // CREATION DU PANNEAU
        // ==========================

        if (player.hasPermission("betterrent.sign")
                && waitingSigns.containsKey(player.getUniqueId())) {

            houseId =
                    waitingSigns.remove(
                            player.getUniqueId()
                    );

            sign.setLine(
                    0,
                    ChatColor.GREEN + "[BetterRent]"
            );

            sign.setLine(
                    1,
                    ChatColor.YELLOW + "Maison"
            );

            sign.setLine(
                    2,
                    houseId
            );

            sign.setLine(
                    3,
                    ChatColor.AQUA + "Cliquez louer");

            sign.update();

            player.sendMessage(
                    ChatColor.GREEN
                            + "Panneau lié à la maison : "
                            + houseId
            );

            return;
        }

        // ==========================
        // PANNEAU BETTERRENT
        // ==========================

        if (houseId == null) {
            return;
        }

        RentHouse house =
                plugin.getRentManager()
                        .getHouse(houseId);

        if (house == null) {

            player.sendMessage(
                    ChatColor.RED
                            + "Cette maison n'existe plus."
            );

            return;
        }

        event.setCancelled(true);

        // ==========================
        // MAISON DEJA LOUEE
        // ==========================

        if (!house.isAvailable()) {

            player.sendMessage(
                    ChatColor.RED
                            + "Cette maison est déjà louée."
            );

            return;
        }

        // ==========================
        // UNE SEULE MAISON PAR JOUEUR
        // ==========================

        String playerHouse =
                plugin.getRentManager()
                        .getPlayerHouseId(
                                player.getUniqueId()
                        );

        if (playerHouse != null) {

            RentHouse ownedHouse =
                    plugin.getRentManager()
                            .getHouse(playerHouse);

            String displayName =
                    ownedHouse != null
                            ? ownedHouse.getName()
                            : playerHouse;

            player.sendMessage(
                    ChatColor.RED
                            + "Vous possédez déjà une maison : "
                            + ChatColor.YELLOW
                            + displayName
            );

            return;
        }

        // ==========================
        // OUVRIR MENU
        // ==========================

        openRentMenu(
                player,
                houseId,
                house
        );
    }

    // ==========================
    // MENU DE LOCATION
    // ==========================

    private void openRentMenu(
            Player player,
            String houseId,
            RentHouse house
    ) {

        rentingHouse.put(
                player.getUniqueId(),
                houseId
        );

        Inventory inventory =
                Bukkit.createInventory(
                        null,
                        27,
                        ChatColor.DARK_GREEN
                                + "Louer : "
                                + house.getName()
                );

        for (int days = 1; days <= 9; days++) {

            double price =
                    house.getPricePerDay()
                            * days;

            ItemStack item =
                    new ItemStack(
                            Material.PAPER
                    );

            ItemMeta meta =
                    item.getItemMeta();

            if (meta != null) {

                meta.setDisplayName(
                        ChatColor.GREEN
                                + "" + days
                                + " jour"
                                + (days > 1 ? "s" : "")
                );

                meta.setLore(
                        java.util.List.of(
                                ChatColor.GRAY
                                        + "Prix : "
                                        + ChatColor.GOLD
                                        + formatMoney(price),
                                "",
                                ChatColor.YELLOW
                                        + "Cliquez pour louer"
                        )
                );

                item.setItemMeta(meta);
            }

            // 1 à 9 placés au milieu du menu
            inventory.setItem(
                    9 + days,
                    item
            );
        }

        player.openInventory(inventory);
    }

    // ==========================
    // CLIQUE DANS LE MENU
    // ==========================

    @EventHandler
    public void onInventoryClick(
            InventoryClickEvent event
    ) {

        if (!(event.getWhoClicked()
                instanceof Player player)) {

            return;
        }

        UUID uuid =
                player.getUniqueId();

        if (!rentingHouse.containsKey(uuid)) {
            return;
        }

        String title =
                event.getView()
                        .getTitle();

        if (!title.startsWith(
                ChatColor.DARK_GREEN + "Louer : "
        )) {

            return;
        }

        event.setCancelled(true);

        ItemStack item =
                event.getCurrentItem();

        if (item == null
                || item.getType() != Material.PAPER) {

            return;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null
                || meta.getDisplayName() == null) {

            return;
        }

        String display =
                ChatColor.stripColor(
                        meta.getDisplayName()
                );

        String[] parts =
                display.split(" ");

        int days;

        try {

            days =
                    Integer.parseInt(parts[0]);

        } catch (NumberFormatException e) {

            return;
        }

        if (days < 1 || days > 9) {
            return;
        }

        String houseId =
                rentingHouse.remove(uuid);

        player.closeInventory();

        if (houseId == null) {
            return;
        }

        RentHouse house =
                plugin.getRentManager()
                        .getHouse(houseId);

        if (house == null) {

            player.sendMessage(
                    ChatColor.RED
                            + "Maison introuvable."
            );

            return;
        }

        // ==========================
        // VERIFICATION MAISON
        // ==========================

        if (!house.isAvailable()) {

            player.sendMessage(
                    ChatColor.RED
                            + "Cette maison est déjà louée."
            );

            return;
        }

        // ==========================
        // UNE SEULE MAISON
        // ==========================

        if (plugin.getRentManager()
                .hasHouse(uuid)) {

            RentHouse ownedHouse =
                    plugin.getRentManager()
                            .getPlayerHouse(uuid);

            String name =
                    ownedHouse != null
                            ? ownedHouse.getName()
                            : "inconnue";

            player.sendMessage(
                    ChatColor.RED
                            + "Vous possédez déjà une maison : "
                            + ChatColor.YELLOW
                            + name
            );

            return;
        }

        // ==========================
        // PRIX
        // ==========================

        double price =
                house.getPricePerDay()
                        * days;

        // ==========================
        // VAULT
        // ==========================

        Economy economy =
                plugin.getVaultHook()
                        .getEconomy();

        if (economy == null) {

            player.sendMessage(
                    ChatColor.RED
                            + "Le système économique Vault "
                            + "n'est pas disponible."
            );

            return;
        }

        // ==========================
        // ARGENT
        // ==========================

        if (!economy.has(
                player,
                price
        )) {

            player.sendMessage(
                    ChatColor.RED
                            + "Vous n'avez pas assez d'argent."
            );

            player.sendMessage(
                    ChatColor.GRAY
                            + "Prix : "
                            + ChatColor.GOLD
                            + formatMoney(price)
            );

            return;
        }

        // ==========================
        // PAIEMENT
        // ==========================

        var result =
                economy.withdrawPlayer(
                        player,
                        price
                );

        if (!result.transactionSuccess()) {

            player.sendMessage(
                    ChatColor.RED
                            + "Le paiement a échoué."
            );

            return;
        }

        // ==========================
        // LOCATION
        // ==========================

        boolean rented =
                plugin.getRentManager()
                        .rentHouse(
                                houseId,
                                uuid,
                                days
                        );

        if (!rented) {

            // REMBOURSEMENT SI ERREUR
            economy.depositPlayer(
                    player,
                    price
            );

            player.sendMessage(
                    ChatColor.RED
                            + "Impossible de louer cette maison."
            );

            return;
        }

        // ==========================
        // SUCCES
        // ==========================

        player.sendMessage(
                ChatColor.GREEN
                        + "✓ Maison louée !"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "Maison : "
                        + ChatColor.WHITE
                        + house.getName()
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "Durée : "
                        + ChatColor.WHITE
                        + days
                        + " jour"
                        + (days > 1 ? "s" : "")
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "Prix payé : "
                        + ChatColor.GOLD
                        + formatMoney(price)
        );
    }

    // ==========================
    // RECUPERER ID MAISON
    // ==========================

    private String getHouseId(Sign sign) {

        String line2 =
                ChatColor.stripColor(
                        sign.getLine(2)
                );

        if (line2 == null
                || line2.isBlank()) {

            return null;
        }

        if (!sign.getLine(0)
                .contains("BetterRent")) {

            return null;
        }

        return line2.trim();
    }

    // ==========================
    // ARGENT
    // ==========================

    private String formatMoney(double amount) {

        if (amount == Math.floor(amount)) {

            return String.format(
                    "%,.0f$",
                    amount
            );
        }

        return String.format(
                "%,.2f$",
                amount
        );
    }
}
