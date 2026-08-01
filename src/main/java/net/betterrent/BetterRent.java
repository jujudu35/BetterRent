package net.betterrent;

import net.betterrent.command.RentCommand;
import net.betterrent.command.RentSettingCommand;
import net.betterrent.listeners.BlockListener;
import net.betterrent.listeners.DoorListener;
import net.betterrent.listeners.InventoryListener;
import net.betterrent.listeners.SignListener;
import net.betterrent.managers.ConfigManager;
import net.betterrent.managers.HookManager;
import net.betterrent.managers.RentManager;
import net.betterrent.storage.HouseStorage;
import net.betterrent.task.RentExpireTask;
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



    @Override
    public void onEnable() {


        instance = this;


        saveDefaultConfig();



        configManager = new ConfigManager(this);


        hookManager = new HookManager(this);



        if (!hookManager.setup()) {


            getLogger().severe("-------------------------------------");
            getLogger().severe("BetterRent failed to start.");
            getLogger().severe("Missing dependency.");
            getLogger().severe("-------------------------------------");


            Bukkit.getPluginManager()
                    .disablePlugin(this);

            return;

        }



        rentManager = new RentManager(this);


        houseStorage = new HouseStorage(this);

        houseStorage.loadHouses();



        selectionManager = new SelectionManager(this);



        registerCommands();


        registerListeners();



        new RentExpireTask(this)
                .runTaskTimer(
                        this,
                        20L * 60L,
                        20L * 60L
                );



        getLogger().info("-------------------------------------");
        getLogger().info("BetterRent enabled successfully!");
        getLogger().info("-------------------------------------");

    }






    private void registerCommands() {


        if(getCommand("rent") != null) {

            getCommand("rent")
                    .setExecutor(
                            new RentCommand(this)
                    );

        }



        if(getCommand("rentsetting") != null) {

            getCommand("rentsetting")
                    .setExecutor(
                            new RentSettingCommand(this)
                    );

        }

    }







    private void registerListeners() {


        Bukkit.getPluginManager()
                .registerEvents(
                        new BlockListener(this),
                        this
                );



        Bukkit.getPluginManager()
                .registerEvents(
                        new DoorListener(this),
                        this
                );



        Bukkit.getPluginManager()
                .registerEvents(
                        new InventoryListener(this),
                        this
                );



        Bukkit.getPluginManager()
                .registerEvents(
                        new SignListener(this),
                        this
                );

    }







    @Override
    public void onDisable() {


        if(houseStorage != null) {

            houseStorage.save();

        }



        getLogger().info("-------------------------------------");
        getLogger().info("BetterRent disabled.");
        getLogger().info("-------------------------------------");

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

}
