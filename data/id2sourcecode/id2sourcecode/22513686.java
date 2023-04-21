    public static int[] calculateMinerals(Solar planet, Random rng) {
        int[] deposits = new int[PlanetResources.Minerals.values().length];
        switch(planet.ptype) {
            case BARE_ROCK:
                deposits[PlanetResources.Minerals.Kaolinite.ordinal()] = rng.nextInt(401) / 100;
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(205) / 100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(205) / 100;
                break;
            case ATM_ROCK:
                deposits[PlanetResources.Minerals.Kaolinite.ordinal()] = rng.nextInt(401) / 100;
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(105) / 100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(105) / 100;
                break;
            case CLOUD:
                deposits[PlanetResources.Minerals.Kaolinite.ordinal()] = rng.nextInt(201) / 100;
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(103) / 100;
                break;
            case EARTH:
                deposits[PlanetResources.Minerals.Kaolinite.ordinal()] = rng.nextInt(401) / 100;
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(103) / 100;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(103) / 100;
                break;
            case ICE:
                deposits[PlanetResources.Minerals.Kaolinite.ordinal()] = rng.nextInt(101) / 100;
                deposits[PlanetResources.Minerals.Cerium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Dysprosium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Erbium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Europium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Gadolinium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Holmium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Lanthanum.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Lutetium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Neodymium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Praseodymium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Promethium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Samarium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Scandium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Terbium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Thulium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Ytterbium.ordinal()] = rng.nextInt(301) / 300;
                deposits[PlanetResources.Minerals.Yttrium.ordinal()] = rng.nextInt(301) / 300;
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
