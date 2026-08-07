package net.betterrent.listeners;

import net.betterrent.BetterRent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.UUID;

public class RentSignListener implements Listener {


    private final BetterRent plugin;

    // Stocke le panneau que l'admin veut créer
    private final HashMap<UUID, String> waitingSigns = new HashMap<>();


    public RentSignListener(BetterRent plugin) {
        this.plugin = plugin;
    }


    // Appelé par la commande /rent sign <id>
    public void setWaitingSign(Player player, String houseId) {

        waitingSigns.put(
                player.getUniqueId(),
                houseId
        );

    }



    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {


        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        Block block = event.getClickedBlock();


        if(block == null) {
            return;
        }


        Material type = block.getType();


        if(type != Material.OAK_SIGN
                && type != Material.OAK_WALL_SIGN
                && !type.name().endsWith("_SIGN")) {

            return;
        }



        Player player = event.getPlayer();


        if(!waitingSigns.containsKey(player.getUniqueId())) {
            return;
        }



        String houseId =
                waitingSigns.remove(
                        player.getUniqueId()
                );



        if(!player.hasPermission("betterrent.sign")) {

            return;

        }



        Sign sign =
                (Sign) block.getState();



        sign.setLine(
                0,
                ChatColor.GREEN + "[BetterRent]"
        );

        sign.setLine(
                1,
                houseId
        );

        sign.setLine(
                2,
                ChatColor.YELLOW + "Cliquez pour louer"
        );

        sign.setLine(
                3,
                ""
        );


        sign.update();



        player.sendMessage(
                ChatColor.GREEN +
                "Panneau lié à "
                + houseId
        );

    }

}
