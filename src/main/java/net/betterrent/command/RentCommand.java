package net.betterrent.command;

import net.betterrent.BetterRent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RentCommand implements CommandExecutor {


    private final BetterRent plugin;


    public RentCommand(BetterRent plugin) {

        this.plugin = plugin;

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender instanceof Player player)) {

            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return true;

        }


        if (!player.hasPermission("betterrent.admin")) {

            player.sendMessage(ChatColor.RED + "Vous n'avez pas la permission.");
            return true;

        }


        if (args.length == 0) {

            player.sendMessage(ChatColor.GOLD + "BetterRent");
            player.sendMessage(ChatColor.YELLOW + "/rent create <nom> <prix>");
            player.sendMessage(ChatColor.YELLOW + "/rent delete <nom>");
            player.sendMessage(ChatColor.YELLOW + "/rent info <nom>");

            return true;

        }



        if (args[0].equalsIgnoreCase("create")) {


            if (args.length < 3) {

                player.sendMessage(
                        ChatColor.RED +
                        "Utilisation : /rent create <nom> <prix>"
                );

                return true;

            }


            String name = args[1];


            double price;

            try {

                price = Double.parseDouble(args[2]);

            } catch (NumberFormatException e) {

                player.sendMessage(
                        ChatColor.RED +
                        "Le prix doit être un nombre."
                );

                return true;

            }



            boolean created =
                    plugin.getRentManager()
                            .createHouse(name, price);



            if (!created) {

                player.sendMessage(
                        ChatColor.RED +
                        "Cette maison existe déjà."
                );

                return true;

            }



            player.sendMessage(
                    ChatColor.GREEN +
                    "Maison " + name +
                    " créée pour " +
                    price +
                    "$/jour."
            );


            return true;

        }



        if (args[0].equalsIgnoreCase("delete")) {


            if (args.length < 2) {

                player.sendMessage(
                        ChatColor.RED +
                        "Utilisation : /rent delete <nom>"
                );

                return true;

            }


            if (plugin.getRentManager()
                    .deleteHouse(args[1])) {


                player.sendMessage(
                        ChatColor.GREEN +
                        "Maison supprimée."
                );


            } else {


                player.sendMessage(
                        ChatColor.RED +
                        "Maison introuvable."
                );

            }


            return true;

        }



        if (args[0].equalsIgnoreCase("info")) {


            if (args.length < 2) {

                player.sendMessage(
                        ChatColor.RED +
                        "Utilisation : /rent info <nom>"
                );

                return true;

            }


            var house =
                    plugin.getRentManager()
                            .getHouse(args[1]);



            if (house == null) {

                player.sendMessage(
                        ChatColor.RED +
                        "Maison introuvable."
                );

                return true;

            }



            player.sendMessage(ChatColor.GOLD + "------");
            player.sendMessage(
                    ChatColor.YELLOW +
                    "Nom : " +
                    house.getName()
            );

            player.sendMessage(
                    ChatColor.YELLOW +
                    "Prix : " +
                    house.getPricePerDay() +
                    "$/jour"
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "Louée : " +
                    house.isRented()
            );


            player.sendMessage(ChatColor.GOLD + "------");


            return true;

        }



        player.sendMessage(
                ChatColor.RED +
                "Commande inconnue."
        );


        return true;
    }
}
