    public static int[] calculateMetals(Solar planet, Random rng) {
        int[] deposits = new int[PlanetResources.Metals.values().length];
        switch(planet.ptype) {
            case BARE_ROCK:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(300) / 100;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(400) / 100;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(300) / 100;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(400) / 100;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(200) / 100;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(300) / 100;
                deposits[PlanetResources.Metals.Zinc.ordinal()] = rng.nextInt(300) / 100;
                deposits[PlanetResources.Metals.Iron.ordinal()] = rng.nextInt(600) / 100;
                deposits[PlanetResources.Metals.Nickel.ordinal()] = rng.nextInt(500) / 100;
                deposits[PlanetResources.Metals.Mercury.ordinal()] = rng.nextInt(120) / 100;
                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(101) / 100;
                break;
            case ATM_ROCK:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(250) / 100;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(130) / 100;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(201) / 100;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(330) / 100;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(180) / 100;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(210) / 100;
                deposits[PlanetResources.Metals.Zinc.ordinal()] = rng.nextInt(210) / 100;
                deposits[PlanetResources.Metals.Iron.ordinal()] = rng.nextInt(250) / 100;
                deposits[PlanetResources.Metals.Nickel.ordinal()] = rng.nextInt(250) / 100;
                deposits[PlanetResources.Metals.Mercury.ordinal()] = rng.nextInt(110) / 100;
                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(101) / 100;
                break;
            case CLOUD:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(110) / 100;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(200) / 100;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(200) / 100;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(170) / 100;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(150) / 100;
                deposits[PlanetResources.Metals.Zinc.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Iron.ordinal()] = rng.nextInt(110) / 100;
                deposits[PlanetResources.Metals.Nickel.ordinal()] = rng.nextInt(110) / 100;
                deposits[PlanetResources.Metals.Mercury.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(101) / 100;
                break;
            case EARTH:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = 0;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(12) / 5;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(6) / 5;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(12) / 5;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(6) / 5;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(6) / 5;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(8) / 5;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(12) / 5;
                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(101) / 100;
                break;
            case ICE:
                deposits[PlanetResources.Metals.Chromium.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Copper.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Gold.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Lead.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Manganese.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Platinum.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Silver.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Tin.ordinal()] = rng.nextInt(21) / 20;
                deposits[PlanetResources.Metals.Iridium.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Osmium.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Palladium.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Rhodium.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Ruthenium.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Bismuth.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Indium.ordinal()] = rng.nextInt(201) / 200;
                deposits[PlanetResources.Metals.Tellurium.ordinal()] = rng.nextInt(201) / 200;
                break;
            default:
        }
        final int promotionFactor = calculateRichness(planet);
        for (int i = 0; i < deposits.length; i++) {
            if (deposits[i] != 0) {
                int rich = rng.nextInt(promotionFactor + 1);
                rich += deposits[i] - 1;
                deposits[i] = rich;
            }
        }
        return deposits;
    }
