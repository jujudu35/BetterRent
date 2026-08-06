package net.betterrent.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class VaultHook {


    private final JavaPlugin plugin;

    private Economy economy;



    public VaultHook(JavaPlugin plugin) {

        this.plugin = plugin;

        setup();

    }





    private boolean setup() {


        if(plugin.getServer()
                .getPluginManager()
                .getPlugin("Vault") == null) {

            return false;

        }



        RegisteredServiceProvider<Economy> rsp =
                plugin.getServer()
                        .getServicesManager()
                        .getRegistration(
                                Economy.class
                        );



        if(rsp == null) {

            return false;

        }



        economy = rsp.getProvider();


        return economy != null;

    }





    public Economy getEconomy() {

        return economy;

    }




    public boolean removeMoney(
            org.bukkit.entity.Player player,
            double amount
    ) {


        if(economy == null) {

            return false;

        }


        if(economy.getBalance(player) < amount) {

            return false;

        }


        economy.withdrawPlayer(
                player,
                amount
        );


        return true;

    }


}
