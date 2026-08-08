package net.betterrent;

import net.betterrent.command.RentCommand;
import net.betterrent.command.RentSettingCommand;

import net.betterrent.listeners.RentProtectionListener;
import net.betterrent.listeners.RentSignListener;

import net.betterrent.managers.ConfigManager;
import net.betterrent.managers.HookManager;
import net.betterrent.managers.RentManager;

import net.betterrent.storage.HouseStorage;

import net.betterrent.task.RentExpireTask;

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

    // ==========================
    // PANNEAUX
    // ==========================

    private RentSignListener rentSignListener;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        // ==========================
        // MANAGERS
        // ==========================

        configManager = new ConfigManager(this);

        hookManager = new HookManager(this);

        selectionManager = new SelectionManager(this);

        // ==========================
        // VAULT
        // ==========================

        vaultHook = new VaultHook(this);

        if (!vaultHook.setup()) {

            getLogger().severe(
                    "Impossible de connecter BetterRent à Vault."
            );

        } else {

            getLogger().info(
                    "Vault connecté avec succès."
            );
        }

        // ==========================
        // STORAGE
        // ==========================

        houseStorage = new HouseStorage(this);

        // ==========================
        // RENT MANAGER
        // ==========================

        rentManager = new RentManager(this);

        houseStorage.loadHouses();

        // ==========================
        // LISTENER PANNEAUX
        // ==========================

        rentSignListener = new RentSignListener(this);

        Bukkit.getPluginManager()
                .registerEvents(
                        rentSignListener,
                        this
                );

        // ==========================
        // COMMANDES
        // ==========================

        if (getCommand("rent") != null) {

            getCommand("rent")
                    .setExecutor(
                            new RentCommand(this)
                    );

        } else {

            getLogger().warning(
                    "Commande /rent introuvable dans plugin.yml"
            );
        }

        if (getCommand("rentsetting") != null) {

            getCommand("rentsetting")
                    .setExecutor(
                            new RentSettingCommand(this)
                    );

        } else {

            getLogger().warning(
                    "Commande /rentsetting introuvable dans plugin.yml"
            );
        }

        // ==========================
        // LISTENER PROTECTION
        // ==========================

        Bukkit.getPluginManager()
                .registerEvents(
                        new RentProtectionListener(this),
                        this
                );

        // ==========================
        // EXPIRATION LOCATIONS
        // ==========================

        new RentExpireTask(this)
                .runTaskTimer(
                        this,
                        20L,
                        20L * 60
                );

        // ==========================
        // FIN
        // ==========================

        getLogger().info(
                "BetterRent activé avec succès."
        );
    }

    @Override
    public void onDisable() {

        if (houseStorage != null) {

            houseStorage.save();
        }

        getLogger().info(
                "BetterRent désactivé."
        );
    }

    // ==========================
    // INSTANCE
    // ==========================

    public static BetterRent getInstance() {

        return instance;
    }

    // ==========================
    // GETTERS
    // ==========================

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

    // ==========================
    // RENT SIGN LISTENER
    // ==========================

    public RentSignListener getRentSignListener() {

        return rentSignListener;
    }
}
