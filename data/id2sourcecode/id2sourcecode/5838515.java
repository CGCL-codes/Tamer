    public static void updateCacheMisses() {
        readsPlusWritesPerDdt = new long[Profile.ddts.length];
        for (int i = 0; i < Profile.ddts.length; ++i) readsPlusWritesPerDdt[i] = 0;
        for (int i = 0; i < Profile.ddts.length; ++i) for (int j = 0; j < varCount; ++j) readsPlusWritesPerDdt[i] += (readsPerDdt[j][i] + writesPerDdt[j][i]);
        cacheMissesL1 = new double[varCount][Profile.ddts.length];
        for (int i = 0; i < varCount; ++i) for (int j = 0; j < Profile.ddts.length; ++j) cacheMissesL1[i][j] = (1.0 * NeSd[i] * (readsPlusWritesPerDdt[j])) / (readsPlusWritesPerDdt[j] + readsPerDdt[i][j] + writesPerDdt[i][j]);
    }
