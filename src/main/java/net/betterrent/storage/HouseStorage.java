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

        loadFile();

    }





    private void loadFile() {


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








    // ==========================
    // SAUVEGARDE
    // ==========================


    public void save() {


        config.set(
                "houses",
                null
        );



        for(Map.Entry<String, RentHouse> entry :
                plugin.getRentManager()
                        .getHouses()
                        .entrySet()) {



            String path =
                    "houses."
                    + entry.getKey();



            RentHouse house =
                    entry.getValue();




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






            // COLOCATAIRES

            for(UUID uuid :
                    house.getTenants()) {


                config.set(
                        path + ".tenants." + uuid,
                        uuid.toString()
                );


            }






            // PERMISSIONS

            for(UUID uuid :
                    house.getPermissions()
                            .keySet()) {



                RentHouse.RentPermission perm =
                        house.getPermission(uuid);



                String permPath =
                        path
                        + ".permissions."
                        + uuid;



                config.set(
                        permPath + ".doors",
                        perm.canDoors()
                );


                config.set(
                        permPath + ".storage",
                        perm.canStorage()
                );


                config.set(
                        permPath + ".use",
                        perm.canUse()
                );


                config.set(
                        permPath + ".place",
                        perm.canPlace()
                );


                config.set(
                        permPath + ".break",
                        perm.canBreak()
                );


            }


        }






        try {


            config.save(file);



        } catch(IOException e) {


            e.printStackTrace();


        }


    }









    // ==========================
    // CHARGEMENT
    // ==========================


    public void loadHouses() {


        if(!config.contains("houses")) {

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






            if(config.contains(path+".owner")) {


                house.setOwner(
                        UUID.fromString(
                                config.getString(
                                        path+".owner"
                                )
                        )
                );


            }





            house.setExpireTime(
                    config.getLong(
                            path+".expire"
                    )
            );





            house.setPos1(
                    loadLocation(
                            path+".pos1"
                    )
            );



            house.setPos2(
                    loadLocation(
                            path+".pos2"
                    )
            );





            house.setWorldGuardRegion(
                    config.getString(
                            path+".region"
                    )
            );






            // CHARGER COLOCATAIRES


            if(config.contains(path+".tenants")) {


                for(String uuid :
                        config.getConfigurationSection(
                                path+".tenants"
                        ).getKeys(false)) {



                    house.addTenant(
                            UUID.fromString(uuid)
                    );


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










    private void saveLocation(
            Location loc,
            String path
    ) {


        if(loc == null) {

            return;

        }




        config.set(
                path+".world",
                loc.getWorld().getName()
        );


        config.set(
                path+".x",
                loc.getX()
        );


        config.set(
                path+".y",
                loc.getY()
        );


        config.set(
                path+".z",
                loc.getZ()
        );


    }







    private Location loadLocation(String path) {


        if(!config.contains(path+".world")) {


            return null;

        }




        return new Location(

                Bukkit.getWorld(
                        config.getString(
                                path+".world"
                        )
                ),

                config.getDouble(
                        path+".x"
                ),

                config.getDouble(
                        path+".y"
                ),

                config.getDouble(
                        path+".z"
                )

        );


    }


}
