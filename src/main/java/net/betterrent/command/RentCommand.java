package net.betterrent.command;


import net.betterrent.BetterRent;
import net.betterrent.managers.RentManager;
import net.betterrent.model.RentHouse;

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

            sendHelp(player);

            return true;

        }






        // ==========================
        // CREATE
        // ==========================

        if (args[0].equalsIgnoreCase("create")) {


            if (!player.hasPermission("betterrent.create")) {

                player.sendMessage(
                        ChatColor.RED +
                        "Vous n'avez pas la permission."
                );

                return true;

            }



            if (args.length < 2) {

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



            } catch (Exception e) {


                player.sendMessage(
                        ChatColor.RED +
                        "Type inconnu."
                );


                player.sendMessage(
                        ChatColor.YELLOW +
                        "Types disponibles :"
                );


                for(RentManager.HouseType type :
                        RentManager.HouseType.values()) {


                    player.sendMessage(
                            ChatColor.GRAY +
                            "- "
                            + type.name()
                    );

                }

            }


            return true;

        }








        // ==========================
        // DELETE
        // ==========================

        if(args[0].equalsIgnoreCase("delete")) {


            if (!player.hasPermission("betterrent.delete")) {

                player.sendMessage(
                        ChatColor.RED +
                        "Vous n'avez pas la permission."
                );

                return true;

            }



            if(args.length < 2) {


                player.sendMessage(
                        ChatColor.RED +
                        "/rent delete <id>"
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









        // ==========================
        // INFO
        // ==========================

        if(args[0].equalsIgnoreCase("info")) {



            if(args.length < 2) {

                player.sendMessage(
                        ChatColor.RED +
                        "/rent info <id>"
                );

                return true;

            }




            RentHouse house =
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
                    "====== BetterRent ======"
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


            player.sendMessage(
                    ChatColor.GOLD +
                    "======================"
            );


            return true;

        }









        // ==========================
        // SET REGION
        // ==========================

        if(args[0].equalsIgnoreCase("setregion")) {



            if(args.length < 2) {


                player.sendMessage(
                        ChatColor.RED +
                        "/rent setregion <id>"
                );


                return true;

            }





            RentHouse house =
                    plugin.getRentManager()
                            .getHouse(args[1]);



            if(house == null) {


                player.sendMessage(
                        ChatColor.RED +
                        "Maison introuvable."
                );


                return true;

            }




            var pos1 =
                    plugin.getSelectionManager()
                            .getPos1(player);



            var pos2 =
                    plugin.getSelectionManager()
                            .getPos2(player);




            if(pos1 == null || pos2 == null) {


                player.sendMessage(
                        ChatColor.RED +
                        "Faites une sélection WorldEdit."
                );


                return true;

            }




            house.setPos1(pos1);

            house.setPos2(pos2);




            player.sendMessage(
                    ChatColor.GREEN +
                    "Région enregistrée."
            );


            return true;
  
        }

        
        
        
        
        
    // ==========================
// SIGN
// ==========================

if(args[0].equalsIgnoreCase("sign")) {


    if(!player.hasPermission("betterrent.sign")) {

        player.sendMessage(
                ChatColor.RED +
                "Vous n'avez pas la permission."
        );

        return true;

    }


    if(args.length < 2) {

        player.sendMessage(
                ChatColor.RED +
                "/rent sign <id>"
        );

        return true;

    }



    RentHouse house =
            plugin.getRentManager()
                    .getHouse(args[1]);



    if(house == null) {

        player.sendMessage(
                ChatColor.RED +
                "Maison introuvable."
        );

        return true;

    }



    plugin.getRentSignListener()
            .setWaitingSign(
                    player,
                    args[1]
            );



    player.sendMessage(
            ChatColor.GREEN +
            "Placez ou cliquez sur un panneau."
    );


    return true;

}






        // ==========================
        // LIST
        // ==========================

        if(args[0].equalsIgnoreCase("list")) {



            player.sendMessage(
                    ChatColor.GOLD +
                    "===== Maisons ====="
            );



            if(plugin.getRentManager()
                    .getHouses()
                    .isEmpty()) {


                player.sendMessage(
                        ChatColor.GRAY +
                        "Aucune maison."
                );


                return true;

            }




            for(String id :
                    plugin.getRentManager()
                            .getHouses()
                            .keySet()) {


                player.sendMessage(
                        ChatColor.YELLOW +
                        "- "
                        + id
                );

            }


            return true;

        }








        sendHelp(player);


        return true;

    }









    private void sendHelp(Player player) {


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
                "/rent setregion <id>"
        );


        player.sendMessage(
                ChatColor.YELLOW +
                "/rent info <id>"
        );


        player.sendMessage(
        ChatColor.YELLOW +
        "/rent sign <id>"
);

        
        player.sendMessage(
                ChatColor.YELLOW +
                "/rent delete <id>"
        );


        player.sendMessage(
                ChatColor.YELLOW +
                "/rent list"
        );


    }

}
