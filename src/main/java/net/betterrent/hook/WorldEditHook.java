package net.betterrent.hook;

import net.betterrent.BetterRent;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;

public class WorldEditHook {

    private final BetterRent plugin;
    private WorldEditPlugin worldEdit;

    public WorldEditHook(BetterRent plugin) {
        this.plugin = plugin;
    }

    public boolean setup() {

        if (Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
            return false;
        }

        worldEdit = (WorldEditPlugin)
                Bukkit.getPluginManager().getPlugin("WorldEdit");

        return worldEdit != null;
    }

    public WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }
}
