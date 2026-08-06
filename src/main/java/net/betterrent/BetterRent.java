package net.betterrent;


import net.betterrent.listeners.RentProtectionListener;

import net.betterrent.managers.ConfigManager;
import net.betterrent.managers.HookManager;
import net.betterrent.managers.RentManager;

import net.betterrent.storage.HouseStorage;

import net.betterrent.vault.VaultHook;

import net.betterrent.worldedit.SelectionManager;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;



public final class BetterRent extends JavaPlugin {



    private static BetterRent instance;



    private ConfigManager configManager;

    private HookManager hookManager;

    private RentManager rentManager;

    private HouseStorage houseStorage;

    private SelectionManager selectionManager;

    private VaultHook vaultHook;






    @Override
    public void onEnable() {



        instance = this;



        saveDefaultConfig();





        configManager =
                new ConfigManager(this);




        hookManager =
                new HookManager(this);




        rentManager =
                new RentManager(this);




        selectionManager =
                new SelectionManager(this);




        vaultHook =
                new VaultHook(this);




        houseStorage =
                new HouseStorage(this);





        Bukkit.getPluginManager().registerEvents(
                new RentProtectionListener(this),
                this
        );





        getLogger().info(
                "BetterRent activé."
        );


    }








    @Override
    public void onDisable() {



        if(houseStorage != null) {


            houseStorage.save();


        }



        getLogger().info(
                "BetterRent désactivé."
        );


    }









    public static BetterRent getInstance() {

        return instance;

    }









    public ConfigManager getConfigManager() {

        return configManager;

    }









    public HookManager getHookManager() {

        return hookManager;

    }









    public RentManager getRentManager() {

        return rentManager;

    }









    public HouseStorage getHouseStorage() {

        return houseStorage;

    }









    public SelectionManager getSelectionManager() {

        return selectionManager;

    }









    public VaultHook getVaultHook() {

        return vaultHook;

    }



}
