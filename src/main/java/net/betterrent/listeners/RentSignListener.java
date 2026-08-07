package net.betterrent.listeners;

import net.betterrent.BetterRent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.UUID;


public class RentSignListener implements Listener {


    private final BetterRent plugin;


    // Admins qui sont en attente de création d'un panneau
    private final HashMap<UUID, String> waitingSigns =
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



        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {

            return;

        }




        Block block =
                event.getClickedBlock();



        if(block == null) {

            return;

        }




        Material type =
                block.getType();



        if(!type.name().endsWith("SIGN")) {

            return;

        }




        Player player =
                event.getPlayer();




        // Seulement les admins BetterRent

        if(!player.hasPermission(
                "betterrent.sign"
        )) {

            return;

        }




        // Vérifie si l'admin a lancé /rent sign

        if(!waitingSigns.containsKey(
                player.getUniqueId()
        )) {

            return;

        }





        String houseId =
                waitingSigns.remove(
                        player.getUniqueId()
                );






        Sign sign =
                (Sign) block.getState();





        sign.setLine(
                0,
                ChatColor.GREEN +
                "[BetterRent]"
        );


        sign.setLine(
                1,
                ChatColor.YELLOW +
                "Maison"
        );


        sign.setLine(
                2,
                houseId
        );


        sign.setLine(
                3,
                ChatColor.AQUA +
                "Cliquez louer"
        );



        sign.update();





        player.sendMessage(
                ChatColor.GREEN +
                "Panneau lié à la maison : "
                + houseId
        );



    }


}
