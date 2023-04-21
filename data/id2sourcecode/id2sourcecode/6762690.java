    public static void novo_dia(double p, double dias[]) {
        for (int i = 0; i < PERIODO - 1; i++) dias[i] = dias[i + 1];
        dias[PERIODO - 1] = p;
    }
