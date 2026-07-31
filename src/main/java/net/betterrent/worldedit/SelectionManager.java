package net.betterrent.worldedit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import net.betterrent.BetterRent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SelectionManager {


    private final BetterRent plugin;


    public SelectionManager(BetterRent plugin) {

        this.plugin = plugin;

    }



    public Selection getSelection(Player player) {


        WorldEditPlugin worldEdit =
                plugin.getHookManager()
                        .getWorldEditHook()
                        .getWorldEdit();


        if (worldEdit == null) {

            return null;

        }


        return worldEdit.getSelection(player);

    }




    public Location getPos1(Player player) {


        Selection selection = getSelection(player);


        if (selection == null) {

            return null;

        }


        return selection.getMinimumPoint();

    }





    public Location getPos2(Player player) {


        Selection selection = getSelection(player);


        if (selection == null) {

            return null;

        }


        return selection.getMaximumPoint();

    }

}
