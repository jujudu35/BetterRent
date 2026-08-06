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



        if(!file.exists()) {


            try {

                file.createNewFile();

            } catch(IOException e) {

                e.printStackTrace();

            }

        }



        config =
                YamlConfiguration
                        .loadConfiguration(file);


    }






    // =================================
    // SAUVEGARDE
    // =================================


    public void save() {


        config.set(
                "houses",
                null
        );



        for(Map.Entry<String, RentHouse> entry :
                plugin.getRentManager()
                        .getHouses()
                        .entrySet()) {



            String id =
                    entry.getKey();



            RentHouse house =
                    entry.getValue();



            String path =
                    "houses."
                    + id;





            config.set(
                    path + ".name",
                    house.getName()
            );



            config.set(
                    path + ".price",
                    house.getPricePerDay()
            );





            if(house.getOwner() != null) {


                config.set(
                        path + ".owner",
                        house.getOwner()
                                .toString()
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





            for(int i = 0;
                i < house.getTenants().size();
                i++) {


                UUID uuid =
                        house.getTenants()
                                .get(i);



                config.set(
                        path + ".tenants." + i,
                        uuid.toString()
                );



                RentHouse.RentPermission permission =
                        house.getPermission(uuid);



                String permissionPath =
                        path
                        + ".permissions."
                        + uuid;





                config.set(
                        permissionPath + ".doors",
                        permission.canDoors()
                );


                config.set(
                        permissionPath + ".storage",
                        permission.canStorage()
                );


                config.set(
                        permissionPath + ".usage",
                        permission.canUse()
                );


                config.set(
                        permissionPath + ".place",
                        permission.canPlace()
                );


                config.set(
                        permissionPath + ".break",
                        permission.canBreak()
                );


            }



        }




        try {


            config.save(file);



        } catch(IOException e) {


            e.printStackTrace();


        }


    }
    // =================================
    // CHARGEMENT
    // =================================


    public void loadHouses() {


        if(!config.contains("houses")) {

            return;

        }



        if(config.getConfigurationSection("houses") == null) {

            return;

        }





        for(String id :
                config.getConfigurationSection("houses")
                        .getKeys(false)) {



            String path =
                    "houses."
                    + id;





            RentHouse house =
                    new RentHouse(

                            config.getString(
                                    path + ".name"
                            ),

                            config.getDouble(
                                    path + ".price"
                            )

                    );







            // ==========================
            // PROPRIETAIRE
            // ==========================


            if(config.contains(path + ".owner")) {


                try {


                    house.setOwner(
                            UUID.fromString(
                                    config.getString(
                                            path + ".owner"
                                    )
                            )
                    );


                } catch(Exception ignored) {


                }


            }







            house.setExpireTime(
                    config.getLong(
                            path + ".expire"
                    )
            );







            // ==========================
            // REGION
            // ==========================


            house.setPos1(
                    loadLocation(
                            path + ".pos1"
                    )
            );


            house.setPos2(
                    loadLocation(
                            path + ".pos2"
                    )
            );



            house.setWorldGuardRegion(
                    config.getString(
                            path + ".region"
                    )
            );









            // ==========================
            // COLOCATAIRES
            // ==========================


            if(config.getConfigurationSection(
                    path + ".tenants"
            ) != null) {



                for(String key :
                        config.getConfigurationSection(
                                path + ".tenants"
                        ).getKeys(false)) {



                    UUID uuid;



                    try {


                        uuid =
                                UUID.fromString(
                                        config.getString(
                                                path
                                                + ".tenants."
                                                + key
                                        )
                                );


                    } catch(Exception e) {


                        continue;


                    }





                    house.addTenant(uuid);






                    String permissionPath =
                            path
                            + ".permissions."
                            + uuid;







                    if(config.contains(permissionPath)) {


                        RentHouse.RentPermission permission =
                                house.getPermission(uuid);




                        permission.setDoors(
                                config.getBoolean(
                                        permissionPath + ".doors",
                                        true
                                )
                        );



                        permission.setStorage(
                                config.getBoolean(
                                        permissionPath + ".storage",
                                        true
                                )
                        );



                        permission.setUse(
                                config.getBoolean(
                                        permissionPath + ".usage",
                                        true
                                )
                        );



                        permission.setPlace(
                                config.getBoolean(
                                        permissionPath + ".place",
                                        true
                                )
                        );



                        permission.setBreak(
                                config.getBoolean(
                                        permissionPath + ".break",
                                        true
                                )
                        );


                    }


                }


            }







            plugin.getRentManager()
                    .getHouses()
                    .put(
                            id,
                            house
                    );


        }


    }







    // =================================
    // SAUVEGARDE POSITION
    // =================================


    private void saveLocation(
            Location location,
            String path
    ) {


        if(location == null) {

            return;

        }



        config.set(
                path + ".world",
                location.getWorld()
                        .getName()
        );



        config.set(
                path + ".x",
                location.getX()
        );


        config.set(
                path + ".y",
                location.getY()
        );


        config.set(
                path + ".z",
                location.getZ()
        );


    }







    // =================================
    // CHARGEMENT POSITION
    // =================================


    private Location loadLocation(
            String path
    ) {


        if(!config.contains(path + ".world")) {

            return null;

        }



        String worldName =
                config.getString(
                        path + ".world"
                );



        if(worldName == null) {

            return null;

        }



        if(Bukkit.getWorld(worldName) == null) {

            return null;

        }






        return new Location(

                Bukkit.getWorld(worldName),

                config.getDouble(
                        path + ".x"
                ),

                config.getDouble(
                        path + ".y"
                ),

                config.getDouble(
                        path + ".z"
                )

        );


    }


}
