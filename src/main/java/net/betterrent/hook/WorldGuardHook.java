package net.betterrent.hook;

import net.betterrent.BetterRent;
import org.bukkit.Bukkit;

public class WorldGuardHook {

    private final BetterRent plugin;

    public WorldGuardHook(BetterRent plugin) {
        this.plugin = plugin;
    }

    public boolean setup() {

        return Bukkit.getPluginManager()
                .getPlugin("WorldGuard") != null;
    }
}
