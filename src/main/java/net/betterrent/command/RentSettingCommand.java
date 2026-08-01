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



        if (!(sender instanceof Player player)) {

            sender.sendMessage(
                    "Commande réservée aux joueurs."
            );

            return true;

        }



        if (!player.isOp()
                && !player.hasPermission("betterrent.admin")) {


            player.sendMessage(
                    ChatColor.RED +
                    "Vous n'avez pas la permission."
            );

            return true;

        }




        if (args.length < 3) {


            sendHelp(player);

            return true;

        }



        String houseName = args[0];


        RentHouse house =
                plugin.getRentManager()
                        .getHouse(houseName);



        if (house == null) {


            player.sendMessage(
                    ChatColor.RED +
                    "Maison introuvable."
            );


            return true;

        }




        String setting = args[1];

        boolean value;



        try {

            value = Boolean.parseBoolean(args[2]);

        } catch (Exception e) {


            player.sendMessage(
                    ChatColor.RED +
                    "Utilise true ou false."
            );


            return true;

        }





        switch (setting.toLowerCase()) {


            case "open-doors" -> {

                house.setOpenDoors(value);

            }


            case "open-trapdoors" -> {

                house.setOpenTrapdoors(value);

            }


            case "open-fence-gates" -> {

                house.setOpenFenceGates(value);

            }


            case "place-blocks" -> {

                house.setPlaceBlocks(value);

            }


            case "break-blocks" -> {

                house.setBreakBlocks(value);

            }


            case "open-chests" -> {

                house.setOpenChests(value);

            }


            case "open-barrels" -> {

                house.setOpenBarrels(value);

            }


            case "open-shulkers" -> {

                house.setOpenShulkers(value);

            }


            case "use-furnaces" -> {

                house.setUseFurnaces(value);

            }


            case "use-anvils" -> {

                house.setUseAnvils(value);

            }


            case "use-crafting" -> {

                house.setUseCrafting(value);

            }


            case "use-enchanting" -> {

                house.setUseEnchanting(value);

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
                "Permission " +
                setting +
                " de " +
                houseName +
                " mise à " +
                value
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
                "Permissions :"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "open-doors, open-trapdoors, open-fence-gates"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "place-blocks, break-blocks"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "open-chests, open-barrels, open-shulkers"
        );


        player.sendMessage(
                ChatColor.GRAY +
                "use-furnaces, use-anvils, use-crafting, use-enchanting"
        );


    }

}
