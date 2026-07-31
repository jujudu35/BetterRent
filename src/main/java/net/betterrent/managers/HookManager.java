package net.betterrent.managers;

import net.betterrent.BetterRent;
import net.betterrent.hook.*;

public class HookManager {

    private final BetterRent plugin;

    private VaultHook vaultHook;
    private WorldEditHook worldEditHook;
    private WorldGuardHook worldGuardHook;


    public HookManager(BetterRent plugin) {

        this.plugin = plugin;

        vaultHook = new VaultHook(plugin);
        worldEditHook = new WorldEditHook(plugin);
        worldGuardHook = new WorldGuardHook(plugin);
    }


    public boolean setup() {

        return vaultHook.setup()
                && worldEditHook.setup()
                && worldGuardHook.setup();
    }


    public VaultHook getVaultHook() {
        return vaultHook;
    }


    public WorldEditHook getWorldEditHook() {
        return worldEditHook;
    }


    public WorldGuardHook getWorldGuardHook() {
        return worldGuardHook;
    }
}
