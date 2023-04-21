    private void generateHauptlinie(Random rnd) {
        final int O = 3;
        final int B = 10;
        final int A = 17;
        final int F = 21;
        final int G = 21;
        final int K = 17;
        final int M = 10;
        final int L = 1;
        int wert = rnd.nextInt(100) + 1;
        wert = wert - L;
        if (wert <= 0) {
            starclass = STAR_TYP_L;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_L + 1);
            return;
        }
        wert = wert - O;
        if (wert <= 0) {
            starclass = STAR_TYP_O;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_O + 1);
            return;
        }
        wert = wert - B;
        if (wert <= 0) {
            starclass = STAR_TYP_B;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_B + 1);
            return;
        }
        wert = wert - A;
        if (wert <= 0) {
            starclass = STAR_TYP_A;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_A + 1);
            return;
        }
        wert = wert - F;
        if (wert <= 0) {
            starclass = STAR_TYP_F;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_F + 1);
            return;
        }
        wert = wert - G;
        if (wert <= 0) {
            starclass = STAR_TYP_G;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_G + 1);
            return;
        }
        wert = wert - K;
        if (wert <= 0) {
            starclass = STAR_TYP_K;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_K + 1);
            return;
        }
        wert = wert - M;
        if (wert <= 0) {
            starclass = STAR_TYP_M;
            planetcount = rnd.nextInt(MAX_PLANETS_STAR_TYP_M + 1);
            return;
        }
    }
