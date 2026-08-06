package net.betterrent.command;


import net.betterrent.BetterRent;
import net.betterrent.worldedit.SelectionManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class RentCommand implements CommandExecutor {


    private final BetterRent plugin;

    private final SelectionManager selectionManager;



    public RentCommand(BetterRent plugin) {

        this.plugin = plugin;

        this.selectionManager =
                new SelectionManager(plugin);

    }







    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {



        if(!(sender instanceof Player player)) {

            sender.sendMessage(
                    "Commande réservée aux joueurs."
            );

            return true;

        }






        if(!player.hasPermission("betterrent.admin")) {


            player.sendMessage(
                    ChatColor.RED +
                    "Vous n'avez pas la permission."
            );


            return true;

        }







        if(args.length == 0) {


            player.sendMessage(
                    ChatColor.GOLD +
                    "===== BetterRent ====="
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "/rent create <type>"
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "/rent delete <nom>"
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "/rent info <nom>"
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "/rent setregion <nom>"
            );


            return true;

        }









        // ===============================
        // CREATION
        // ===============================


        if(args[0].equalsIgnoreCase("create")) {


            if(args.length < 2) {


                player.sendMessage(
                        ChatColor.RED +
                        "/rent create <type>"
                );


                return true;

            }



            try {


                RentManager.HouseType type =
                        RentManager.HouseType.valueOf(
                                args[1].toUpperCase()
                        );



                String id =
                        plugin.getRentManager()
                                .createHouse(type);



                player.sendMessage(
                        ChatColor.GREEN +
                        "Maison créée : "
                        + id
                );



            } catch(Exception e) {


                player.sendMessage(
                        ChatColor.RED +
                        "Type inconnu."
                );


            }


            return true;

        }









        // ===============================
        // SUPPRESSION
        // ===============================


        if(args[0].equalsIgnoreCase("delete")) {


            if(args.length < 2) {

                player.sendMessage(
                        ChatColor.RED +
                        "/rent delete <nom>"
                );


                return true;

            }




            if(plugin.getRentManager()
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









        // ===============================
        // INFO
        // ===============================


        if(args[0].equalsIgnoreCase("info")) {


            if(args.length < 2) {

                player.sendMessage(
                        ChatColor.RED +
                        "/rent info <nom>"
                );

                return true;

            }



            var house =
                    plugin.getRentManager()
                            .getHouse(args[1]);



            if(house == null) {


                player.sendMessage(
                        ChatColor.RED +
                        "Maison introuvable."
                );


                return true;

            }




            player.sendMessage(
                    ChatColor.GOLD +
                    "===== Maison ====="
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "Nom : "
                    + house.getName()
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "Prix : "
                    + house.getPricePerDay()
                    + "$/jour"
            );


            player.sendMessage(
                    ChatColor.YELLOW +
                    "Louée : "
                    + house.isRented()
            );


            return true;

        }









        // ===============================
        // REGION WORLDEDIT
        // ===============================


        if(args[0].equalsIgnoreCase("setregion")) {



            if(args.length < 2) {


                player.sendMessage(
                        ChatColor.RED +
                        "/rent setregion <maison>"
                );


                return true;

            }




            var house =
                    plugin.getRentManager()
                            .getHouse(args[1]);



            if(house == null) {


                player.sendMessage(
                        ChatColor.RED +
                        "Maison introuvable."
                );


                return true;

            }





            boolean result =
                    selectionManager.saveSelection(
                            player,
                            house
                    );





            if(!result) {


                player.sendMessage(
                        ChatColor.RED +
                        "Sélection WorldEdit introuvable."
                );


                return true;

            }




            player.sendMessage(
                    ChatColor.GREEN +
                    "Région enregistrée."
            );



            return true;

        }









        player.sendMessage(
                ChatColor.RED +
                "Commande inconnue."
        );


        return true;

    }

}
