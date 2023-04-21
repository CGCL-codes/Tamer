    public static void novo_dia(double p, double dias[]) {
        for (int i = 0; i < LONG_PERIOD - 1; i++) dias[i] = dias[i + 1];
        dias[LONG_PERIOD - 1] = p;
    }
