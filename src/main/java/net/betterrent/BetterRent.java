package net.betterrent;

import net.betterrent.managers.ConfigManager;
import net.betterrent.managers.HookManager;
import net.betterrent.managers.RentManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterRent extends JavaPlugin {

    private static BetterRent instance;

    private ConfigManager configManager;
    private HookManager hookManager;
    private RentManager rentManager;


    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();


        configManager = new ConfigManager(this);

        hookManager = new HookManager(this);

        rentManager = new RentManager(this);


        if (!hookManager.setup()) {

            getLogger().severe("-------------------------------------");
            getLogger().severe("BetterRent failed to start.");
            getLogger().severe("Vault, WorldEdit or WorldGuard is missing.");
            getLogger().severe("-------------------------------------");

            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        getLogger().info("-------------------------------------");
        getLogger().info("BetterRent enabled successfully!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("-------------------------------------");
    }


    @Override
    public void onDisable() {

        getLogger().info("BetterRent disabled.");

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
}
