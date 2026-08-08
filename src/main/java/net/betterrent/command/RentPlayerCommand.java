package net.betterrent.command;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RentPlayerCommand implements CommandExecutor {

    private final BetterRent plugin;

    public RentPlayerCommand(BetterRent plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    "Commande réservée aux joueurs."
            );

            return true;
        }

        if (args.length == 0) {
            help(player);
            return true;
        }

        switch (args[0].toLowerCase()) {

            // =================================
            // LOUER
            // =================================

            case "rent" -> {

                if (args.length < 3) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "/rent rent <maison> <jours>"
                    );

                    return true;
                }

                String id = args[1];

                int days;

                try {

                    days = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Nombre de jours invalide."
                    );

                    return true;
                }

                if (days <= 0) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Le nombre de jours doit être supérieur à 0."
                    );

                    return true;
                }

                RentHouse house =
                        plugin.getRentManager()
                                .getHouse(id);

                if (house == null) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Maison introuvable."
                    );

                    return true;
                }

                if (plugin.getRentManager()
                        .rentHouse(
                                id,
                                player.getUniqueId(),
                                days
                        )) {

                    player.sendMessage(
                            ChatColor.GREEN
                                    + "Maison louée pendant "
                                    + days
                                    + " jour"
                                    + (days > 1 ? "s." : ".")
                    );

                } else {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Impossible de louer cette maison."
                    );
                }
            }

            // =================================
            // PROLONGER
            // =================================

            case "extend" -> {

                if (args.length < 3) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "/rent extend <maison> <jours>"
                    );

                    return true;
                }

                String id = args[1];

                int days;

                try {

                    days = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Nombre de jours invalide."
                    );

                    return true;
                }

                if (days <= 0) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Le nombre de jours doit être supérieur à 0."
                    );

                    return true;
                }

                /*
                 * IMPORTANT :
                 * extendRent demande maintenant :
                 *
                 * String id
                 * UUID player
                 * int days
                 */

                if (plugin.getRentManager()
                        .extendRent(
                                id,
                                player.getUniqueId(),
                                days
                        )) {

                    player.sendMessage(
                            ChatColor.GREEN
                                    + "Location prolongée de "
                                    + days
                                    + " jour"
                                    + (days > 1 ? "s." : ".")
                    );

                } else {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Impossible de prolonger la location."
                    );
                }
            }

            // =================================
            // TRUST
            // =================================

            case "trust" -> {

                if (args.length < 3) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "/rent trust <maison> <joueur>"
                    );

                    return true;
                }

                RentHouse house =
                        plugin.getRentManager()
                                .getHouse(args[1]);

                if (house == null) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Maison introuvable."
                    );

                    return true;
                }

                if (!player.getUniqueId()
                        .equals(house.getOwner())) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Vous n'êtes pas propriétaire."
                    );

                    return true;
                }

                OfflinePlayer target =
                        Bukkit.getOfflinePlayer(args[2]);

                if (plugin.getRentManager()
                        .addTenant(
                                args[1],
                                target.getUniqueId()
                        )) {

                    player.sendMessage(
                            ChatColor.GREEN
                                    + "Joueur ajouté."
                    );

                } else {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Impossible d'ajouter ce joueur."
                    );
                }
            }

            // =================================
            // UNTRUST
            // =================================

            case "untrust" -> {

                if (args.length < 3) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "/rent untrust <maison> <joueur>"
                    );

                    return true;
                }

                RentHouse house =
                        plugin.getRentManager()
                                .getHouse(args[1]);

                if (house == null) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Maison introuvable."
                    );

                    return true;
                }

                if (!player.getUniqueId()
                        .equals(house.getOwner())) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Vous n'êtes pas propriétaire."
                    );

                    return true;
                }

                OfflinePlayer target =
                        Bukkit.getOfflinePlayer(args[2]);

                if (plugin.getRentManager()
                        .removeTenant(
                                args[1],
                                target.getUniqueId()
                        )) {

                    player.sendMessage(
                            ChatColor.GREEN
                                    + "Joueur retiré."
                    );

                } else {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Impossible de retirer ce joueur."
                    );
                }
            }

            // =================================
            // QUITTER
            // =================================

            case "leave" -> {

                if (args.length < 2) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "/rent leave <maison>"
                    );

                    return true;
                }

                RentHouse house =
                        plugin.getRentManager()
                                .getHouse(args[1]);

                if (house == null) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Maison introuvable."
                    );

                    return true;
                }

                house.removeTenant(
                        player.getUniqueId()
                );

                player.sendMessage(
                        ChatColor.GREEN
                                + "Vous avez quitté la maison."
                );
            }

            // =================================
            // INFO
            // =================================

            case "info" -> {

                if (args.length < 2) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "/rent info <maison>"
                    );

                    return true;
                }

                RentHouse house =
                        plugin.getRentManager()
                                .getHouse(args[1]);

                if (house == null) {

                    player.sendMessage(
                            ChatColor.RED
                                    + "Maison inconnue."
                    );

                    return true;
                }

                player.sendMessage(
                        ChatColor.GOLD
                                + "===== Maison ====="
                );

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Nom : "
                                + house.getName()
                );

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Prix : "
                                + house.getPricePerDay()
                );

                player.sendMessage(
                        ChatColor.YELLOW
                                + "Louée : "
                                + house.isRented()
                );
            }

            // =================================
            // LIST
            // =================================

            case "list" -> {

                player.sendMessage(
                        ChatColor.GOLD
                                + "Maisons disponibles :"
                );

                for (String id :
                        plugin.getRentManager()
                                .getHouses()
                                .keySet()) {

                    player.sendMessage(
                            ChatColor.YELLOW
                                    + "- "
                                    + id
                    );
                }
            }

            // =================================
            // AIDE
            // =================================

            default -> help(player);
        }

        return true;
    }

    // =================================
    // AIDE
    // =================================

    private void help(Player player) {

        player.sendMessage(
                ChatColor.GOLD
                        + "===== BetterRent ====="
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent list"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent info <maison>"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent rent <maison> <jours>"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent extend <maison> <jours>"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent trust <maison> <joueur>"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent untrust <maison> <joueur>"
        );

        player.sendMessage(
                ChatColor.YELLOW
                        + "/rent leave <maison>"
        );
    }
}
