package net.betterrent.listeners;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import net.betterrent.utils.RegionUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {


    private final BetterRent plugin;

    private final RegionUtil regionUtil;



    public SignListener(BetterRent plugin) {

        this.plugin = plugin;
        this.regionUtil = new RegionUtil(plugin);

    }





    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {


        if (!(event.getClickedBlock()
                instanceof Sign sign)) {

            return;

        }



        String[] lines = sign.getLines();



        if (!lines[0]
                .equalsIgnoreCase("[BetterRent]")) {

            return;

        }



        Player player = event.getPlayer();



        String houseName = lines[1];



        RentHouse house =
                plugin.getRentManager()
                        .getHouse(houseName);



        if (house == null) {


            player.sendMessage(
                    ChatColor.RED +
                    "Cette maison n'existe pas."
            );


            return;

        }



        if (house.isRented()) {


            player.sendMessage(
                    ChatColor.RED +
                    "Cette maison est déjà louée."
            );


            return;

        }




        Economy economy =
                plugin.getHookManager()
                        .getEconomy();



        if (economy == null) {


            player.sendMessage(
                    ChatColor.RED +
                    "Vault n'est pas disponible."
            );


            return;

        }




        double price =
                house.getPricePerDay();



        if (!economy.has(
                player,
                price
        )) {


            player.sendMessage(
                    ChatColor.RED +
                    "Vous n'avez pas assez d'argent."
            );


            return;

        }




        economy.withdrawPlayer(
                player,
                price
        );



        house.setOwner(
                player.getUniqueId()
        );


        house.setExpireTime(
                System.currentTimeMillis()
                        + (24L * 60L * 60L * 1000L)
        );



        player.sendMessage(
                ChatColor.GREEN +
                "Vous avez loué la maison "
                + house.getName()
                + " pour 24 heures."
        );

    }

}
