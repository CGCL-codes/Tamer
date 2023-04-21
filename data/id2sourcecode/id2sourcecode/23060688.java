    public static void novo_dia(double p, double dias[]) {
        int tam = dias.length;
        for (int i = 0; i < tam - 1; i++) dias[i] = dias[i + 1];
        dias[tam - 1] = p;
    }
