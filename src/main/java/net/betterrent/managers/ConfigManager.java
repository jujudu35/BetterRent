package net.betterrent.managers;

import net.betterrent.BetterRent;

public class ConfigManager {

    private final BetterRent plugin;

    public ConfigManager(BetterRent plugin) {
        this.plugin = plugin;
    }

    public BetterRent getPlugin() {
        return plugin;
    }
}
