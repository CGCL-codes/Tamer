    public void evaluate() {
        for (int i = 0; i < 3; i++) objectiveVector.set(i, 0.0);
        double memoryAccesses = 0;
        double memoryUsage = 0;
        double energyConsumption = 0;
        double sequentialAccesses = 0;
        double randomAccesses = 0;
        double averageSize = 0;
        double readsCounter = 0;
        double writesCounter = 0;
        int Te0 = 0;
        int Te1 = 0;
        int Te2 = 0;
        for (int i = 0; i < M / 5; i++) {
            int ddt0 = x[5 * i + 0].intValue();
            int ddt1 = x[5 * i + 1].intValue();
            int ddt2 = x[5 * i + 3].intValue();
            long na1 = x[5 * i + 2].longValue();
            long na2 = x[5 * i + 4].longValue();
            Te0 = Tref;
            Te1 = Tref;
            Te2 = Te[i];
            if (ddt1 == NOTHING || na1 == 0) {
                Te0 = Te[i];
                x[5 * i + 1].setValue(0);
                ddt1 = NOTHING;
                x[5 * i + 2].setValue(0);
                na1 = 1;
                x[5 * i + 3].setValue(NOTHING);
                ddt2 = NOTHING;
            }
            if (ddt2 == NOTHING || na2 == 0) {
                Te1 = Te[i];
                x[5 * i + 3].setValue(NOTHING);
                ddt2 = NOTHING;
                x[5 * i + 4].setValue(0);
                na2 = 1;
            }
            double complexity = calculateComplexity(ddt0, ddt1, ddt2, i);
            double na0 = (1.0 * Ne[i] / (1.0 * na1 * na2));
            if (na0 == 0) na0 = Math.round(Long.MAX_VALUE / (na1 * na2));
            if (ddt1 == NOTHING) na1 = 0;
            if (ddt2 == NOTHING) na2 = 0;
            sequentialAccesses = 0;
            randomAccesses = 0;
            averageSize = 0;
            readsCounter = 0;
            writesCounter = 0;
            sequentialAccesses += calculateSequentialAccesses(ddt0, na0);
            randomAccesses += calculateRandomAccesses(ddt0, na0);
            averageSize += calculateAverageSize(ddt0, na0, Te0);
            sequentialAccesses += calculateSequentialAccesses(ddt1, na0 * na1);
            randomAccesses += calculateRandomAccesses(ddt1, na0 * na1);
            averageSize += calculateAverageSize(ddt1, na0 * na1, Te1);
            sequentialAccesses += calculateSequentialAccesses(ddt2, na0 * na1 * na2);
            randomAccesses += calculateRandomAccesses(ddt2, na0 * na1 * na2);
            averageSize += calculateAverageSize(ddt2, na0 * na1 * na2, Te2);
            writesCounter = 1.0 * writes[i];
            readsCounter = 1.0 * reads[i];
            memoryAccesses += (sequentialAccesses + randomAccesses);
            memoryUsage += averageSize;
            energyConsumption += (0.96 - complexity) * (writesCounter + readsCounter) * 1e-4 + (0.04 + complexity) * (writesCounter + readsCounter) * 1e-2;
        }
        objectiveVector.set(0, memoryAccesses);
        objectiveVector.set(1, memoryUsage);
        objectiveVector.set(2, energyConsumption);
    }
