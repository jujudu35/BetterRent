package net.betterrent.managers;

import net.betterrent.BetterRent;

public class HookManager {

    private final BetterRent plugin;

    public HookManager(BetterRent plugin) {
        this.plugin = plugin;
    }

    public boolean setup() {
        return true;
    }

    public BetterRent getPlugin() {
        return plugin;
    }
}
