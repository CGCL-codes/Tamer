    public void crossover(int c1, int c2, float[] kid) {
        int i, cut1;
        cut1 = 1 + r.nextInt(CHROMOSOME_SIZE - 1);
        for (i = 0; i < cut1; i++) kid[i] = pop[c1][i];
        for (i = cut1; i < CHROMOSOME_SIZE; i++) kid[i] = pop[c2][i];
    }
