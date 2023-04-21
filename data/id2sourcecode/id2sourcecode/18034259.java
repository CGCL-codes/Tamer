    public static void novo_dia(double p, int volume, double dias[]) {
        p = pvt(p, volume);
        for (int i = 0; i < PERIODO - 1; i++) dias[i] = dias[i + 1];
        dias[PERIODO - 1] = p;
    }
