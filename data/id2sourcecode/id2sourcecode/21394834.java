    private void updateCacheMisses() {
        readsPlusWritesPerDdt = new long[DynamicDataTypes.ddts.length];
        for (int i = 0; i < DynamicDataTypes.ddts.length; ++i) {
            readsPlusWritesPerDdt[i] = 0;
        }
        for (int i = 0; i < DynamicDataTypes.ddts.length; ++i) {
            for (int j = 0; j < numberOfVariables; ++j) {
                readsPlusWritesPerDdt[i] += (readsPerDdt[j][i] + writesPerDdt[j][i]);
            }
        }
        cacheMissesL1 = new double[numberOfVariables][DynamicDataTypes.ddts.length];
        for (int i = 0; i < numberOfVariables; ++i) {
            for (int j = 0; j < DynamicDataTypes.ddts.length; ++j) {
                cacheMissesL1[i][j] = (1.0 * NeSd[i] * (readsPlusWritesPerDdt[j])) / (readsPlusWritesPerDdt[j] + readsPerDdt[i][j] + writesPerDdt[i][j]);
            }
        }
    }
