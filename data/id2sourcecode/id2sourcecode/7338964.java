    public static final float[][] getElevation_16(boolean withSnow) {
        int len = 16;
        float[][] table = new float[3][len];
        float[][] elev_8 = Colors.getElevation_8(withSnow);
        table[0][0] = elev_8[0][0];
        table[1][0] = elev_8[1][0];
        table[2][0] = elev_8[2][0];
        table[0][len - 1] = elev_8[0][7];
        table[1][len - 1] = elev_8[1][7];
        table[2][len - 1] = elev_8[2][7];
        for (int i = 1; i < (len - 1); i += 2) {
            table[0][i] = (table[0][i - 1] + table[0][i + 1]) / 2.0f;
            table[1][i] = (table[1][i - 1] + table[1][i + 1]) / 2.0f;
            table[2][i] = (table[2][i - 1] + table[2][i + 1]) / 2.0f;
        }
        return table;
    }
