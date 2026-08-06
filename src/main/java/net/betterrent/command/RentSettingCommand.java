package net.betterrent.command;


import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class RentSettingCommand implements CommandExecutor {



    private final BetterRent plugin;




    public RentSettingCommand(BetterRent plugin) {

        this.plugin = plugin;

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








        if(args.length < 3) {


            sendHelp(player);


            return true;

        }








        String houseId =
                args[0];





        RentHouse house =
                plugin.getRentManager()
                        .getHouse(houseId);





        if(house == null) {


            player.sendMessage(
                    ChatColor.RED +
                    "Maison introuvable."
            );


            return true;

        }








        String permissionName =
                args[1].toLowerCase();





        boolean value =
                Boolean.parseBoolean(args[2]);








        switch(permissionName) {


            case "doors" -> {


                for(Player target :
                        player.getServer()
                                .getOnlinePlayers()) {


                    if(house.isTenant(
                            target.getUniqueId()
                    )) {


                        house.getPermission(
                                target.getUniqueId()
                        ).setDoors(value);


                    }

                }


            }



            case "storage" -> {



                for(Player target :
                        player.getServer()
                                .getOnlinePlayers()) {


                    if(house.isTenant(
                            target.getUniqueId()
                    )) {


                        house.getPermission(
                                target.getUniqueId()
                        ).setStorage(value);


                    }

                }


            }



            case "use" -> {



                for(Player target :
                        player.getServer()
                                .getOnlinePlayers()) {


                    if(house.isTenant(
                            target.getUniqueId()
                    )) {


                        house.getPermission(
                                target.getUniqueId()
                        ).setUse(value);


                    }

                }


            }
                case "place" -> {



                for(Player target :
                        player.getServer()
                                .getOnlinePlayers()) {


                    if(house.isTenant(
                            target.getUniqueId()
                    )) {


                        house.getPermission(
                                target.getUniqueId()
                        ).setPlace(value);


                    }

                }


            }






            case "break" -> {



                for(Player target :
                        player.getServer()
                                .getOnlinePlayers()) {


                    if(house.isTenant(
                            target.getUniqueId()
                    )) {


                        house.getPermission(
                                target.getUniqueId()
                        ).setBreak(value);


                    }

                }


            }






            default -> {


                player.sendMessage(
                        ChatColor.RED +
                        "Permission inconnue."
                );


                sendHelp(player);


                return true;

            }


        }







        plugin.getHouseStorage()
                .save();






        player.sendMessage(
                ChatColor.GREEN +
                "Permission "
                + permissionName
                + " changée en "
                + value
        );



        return true;


    }









    private void sendHelp(Player player) {



        player.sendMessage(
                ChatColor.GOLD +
                "===== BetterRent Setting ====="
        );



        player.sendMessage(
                ChatColor.YELLOW +
                "/rentsetting <maison> <permission> <true/false>"
        );



        player.sendMessage(
                ChatColor.GRAY +
                "Permissions disponibles :"
        );



        player.sendMessage(
                ChatColor.GRAY +
                "- doors"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "- storage"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "- use"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "- place"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "- break"
        );


    }



}
