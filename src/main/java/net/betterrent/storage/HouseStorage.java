package net.betterrent.storage;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class HouseStorage {


    private final BetterRent plugin;

    private File file;

    private FileConfiguration config;



    public HouseStorage(BetterRent plugin) {

        this.plugin = plugin;

        load();

    }



    private void load() {

        file = new File(
                plugin.getDataFolder(),
                "houses.yml"
        );


        if (!file.exists()) {

            try {

                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }


        config = YamlConfiguration.loadConfiguration(file);

    }





    public void save() {


        for (Map.Entry<String, RentHouse> entry :
                plugin.getRentManager()
                        .getHouses()
                        .entrySet()) {


            String path = "houses." + entry.getKey();

            RentHouse house = entry.getValue();



            config.set(
                    path + ".name",
                    house.getName()
            );


            config.set(
                    path + ".price",
                    house.getPricePerDay()
            );



            if (house.getOwner() != null) {

                config.set(
                        path + ".owner",
                        house.getOwner().toString()
                );

            }



            config.set(
                    path + ".expire",
                    house.getExpireTime()
            );



            saveLocation(
                    house.getPos1(),
                    path + ".pos1"
            );


            saveLocation(
                    house.getPos2(),
                    path + ".pos2"
            );



            config.set(
                    path + ".region",
                    house.getWorldGuardRegion()
            );



            config.set(
                    path + ".trusted",
                    house.getTrustedPlayers()
                            .stream()
                            .map(UUID::toString)
                            .toList()
            );



            config.set(path + ".permissions.open-doors",
                    house.canOpenDoors());

            config.set(path + ".permissions.open-trapdoors",
                    house.canOpenTrapdoors());

            config.set(path + ".permissions.open-fence-gates",
                    house.canOpenFenceGates());

            config.set(path + ".permissions.place-blocks",
                    house.canPlaceBlocks());

            config.set(path + ".permissions.break-blocks",
                    house.canBreakBlocks());

            config.set(path + ".permissions.open-chests",
                    house.canOpenChests());

            config.set(path + ".permissions.open-barrels",
                    house.canOpenBarrels());

            config.set(path + ".permissions.open-shulkers",
                    house.canOpenShulkers());

            config.set(path + ".permissions.use-furnaces",
                    house.canUseFurnaces());

            config.set(path + ".permissions.use-anvils",
                    house.canUseAnvils());

            config.set(path + ".permissions.use-crafting",
                    house.canUseCrafting());

            config.set(path + ".permissions.use-enchanting",
                    house.canUseEnchanting());

        }



        try {

            config.save(file);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }





    public void loadHouses() {


        if (!config.contains("houses")) {

            return;

        }


        for (String key :
                config.getConfigurationSection("houses")
                        .getKeys(false)) {


            String path = "houses." + key;


            RentHouse house = new RentHouse(
                    config.getString(path + ".name"),
                    config.getDouble(path + ".price")
            );



            if (config.contains(path + ".owner")) {

                house.setOwner(
                        UUID.fromString(
                                config.getString(path + ".owner")
                        )
                );

            }



            house.setExpireTime(
                    config.getLong(path + ".expire")
            );


            house.setPos1(
                    loadLocation(path + ".pos1")
            );


            house.setPos2(
                    loadLocation(path + ".pos2")
            );


            house.setWorldGuardRegion(
                    config.getString(path + ".region")
            );



            plugin.getRentManager()
                    .getHouses()
                    .put(
                            key,
                            house
                    );

        }

    }





    private void saveLocation(Location loc, String path) {


        if (loc == null) {

            return;

        }


        config.set(path + ".world",
                loc.getWorld().getName());

        config.set(path + ".x",
                loc.getX());

        config.set(path + ".y",
                loc.getY());

        config.set(path + ".z",
                loc.getZ());

    }





    private Location loadLocation(String path) {


        if (!config.contains(path + ".world")) {

            return null;

        }


        return new Location(
                Bukkit.getWorld(
                        config.getString(path + ".world")
                ),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z")
        );

    }

}
