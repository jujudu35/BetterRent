package net.betterrent.hook;

import net.betterrent.BetterRent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private final BetterRent plugin;
    private Economy economy;

    public VaultHook(BetterRent plugin) {
        this.plugin = plugin;
    }

    public boolean setup() {

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =
                Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();

        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }
}
