package net.betterrent.storage;

import net.betterrent.BetterRent;
import net.betterrent.model.RentHouse;
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
                plugin.getRentManager().getHouses().entrySet()) {


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


            config.set(
                    path + ".trusted",
                    house.getTrustedPlayers()
                            .stream()
                            .map(UUID::toString)
                            .toList()
            );

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



            String name =
                    config.getString(
                            "houses." + key + ".name"
                    );


            double price =
                    config.getDouble(
                            "houses." + key + ".price"
                    );



            RentHouse house =
                    new RentHouse(
                            name,
                            price
                    );



            if (config.contains(
                    "houses." + key + ".owner"
            )) {


                house.setOwner(
                        UUID.fromString(
                                config.getString(
                                "houses." + key + ".owner"
                                )
                        )
                );

            }



            house.setExpireTime(
                    config.getLong(
                            "houses." + key + ".expire"
                    )
            );



            plugin.getRentManager()
                    .getHouses()
                    .put(
                            key,
                            house
                    );

        }

    }
}
