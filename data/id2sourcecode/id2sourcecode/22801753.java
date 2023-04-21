    private void generateRoteRiese(Random rnd) {
        final int BIG = 30;
        final int NORMAL = 30;
        final int LITTLE = 30;
        int wert = rnd.nextInt(100) + 1;
        wert = wert - BIG;
        if (wert <= 0) {
            starclass = STAR_TYP_RIESE_0;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_R0 + 1);
            return;
        }
        wert = wert - NORMAL;
        if (wert <= 0) {
            starclass = STAR_TYP_RIESE_1;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_R1 + 1);
            return;
        }
        wert = wert - LITTLE;
        if (wert <= 0) {
            starclass = STAR_TYP_RIESE_2;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_R2 + 1);
            return;
        }
    }
