
public void save() {


    for (Map.Entry<String, RentHouse> entry :
            plugin.getRentManager()
                    .getHouses()
                    .entrySet()) {


        String path = "houses." + entry.getKey();

        RentHouse house = entry.getValue();


        config.set(path + ".name", house.getName());

        config.set(path + ".price", house.getPricePerDay());


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

} // <-- FIN DE save()



// ICI seulement
private void saveLocation(Location loc, String path) {

    if (loc == null) {
        return;
    }

    config.set(path + ".world", loc.getWorld().getName());
    config.set(path + ".x", loc.getX());
    config.set(path + ".y", loc.getY());
    config.set(path + ".z", loc.getZ());

}
