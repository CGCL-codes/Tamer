    public static float[] vertFlipQuadTexCoords(float[] tc) {
        float swap = 0;
        for (int i = 1; i < tc.length - 2; i += 4) {
            swap = tc[i];
            tc[i] = tc[i + 2];
            tc[i + 2] = swap;
        }
        return tc;
    }
