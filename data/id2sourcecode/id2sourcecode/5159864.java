    public static void updateCacheMisses() {
        readsPlusWritesPerDdt = new long[Profile.ddts.length];
        for (int i = 0; i < Profile.ddts.length; ++i) readsPlusWritesPerDdt[i] = 0;
        for (int i = 0; i < Profile.ddts.length; ++i) for (int j = 0; j < M; ++j) readsPlusWritesPerDdt[i] += (readsPerDdt[j][i] + writesPerDdt[j][i]);
        cacheMissesL1 = new double[M][Profile.ddts.length];
        for (int i = 0; i < M; ++i) for (int j = 0; j < Profile.ddts.length; ++j) cacheMissesL1[i][j] = (1.0 * NeSd[i] * (readsPlusWritesPerDdt[j])) / (readsPlusWritesPerDdt[j] + readsPerDdt[i][j] + writesPerDdt[i][j]);
    }
