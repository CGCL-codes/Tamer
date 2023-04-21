    public void evaluate(Solution<Variable<Integer>> solution) {
        for (int i = 0; i < 3; i++) {
            solution.getObjectives().set(i, 0.0);
        }
        double performance = 0;
        double memory = 0;
        double energy = 0;
        double randomCount = 0;
        double secuentialCount = 0;
        double averageSize = 0;
        double numCreations = 0;
        double reads = 0;
        double writes = 0;
        double execTime = 0;
        for (int i = 0; i < numberOfVariables; i++) {
            Variable<Integer> variable = solution.getVariables().get(i);
            int ddt = variable.getValue();
            randomCount = 0;
            secuentialCount = 0;
            averageSize = 0;
            numCreations = 0;
            reads = readsPerDdt[i][ddt];
            writes = writesPerDdt[i][ddt];
            execTime = 0;
            if (ddt == DynamicDataTypes.AR) {
                secuentialCount = 9 * Ne[i];
                randomCount = 2;
                averageSize = 19 * Tref + Ne[i] * Te[i];
                numCreations = 1;
            } else if (ddt == DynamicDataTypes.ARP) {
                secuentialCount = 10 * Ne[i];
                randomCount = 3;
                averageSize = 19 * Tref + Ne[i] * (Tref + Te[i]);
                numCreations = 1 + Nve[i];
            } else if (ddt == DynamicDataTypes.SLL) {
                secuentialCount = 7 * Ne[i];
                randomCount = Ne[i] / 2 + 1;
                averageSize = 19 * Tref + Ne[i] * (2 * Tref + Te[i]);
                numCreations = 1 + Nve[i];
            } else if (ddt == DynamicDataTypes.DLL) {
                secuentialCount = 7 * Ne[i];
                randomCount = Ne[i] / 4 + 1;
                averageSize = 19 * Tref + Ne[i] * (3 * Tref + Te[i]);
                numCreations = 1 + Nve[i];
            } else if (ddt == DynamicDataTypes.SLLO) {
                secuentialCount = 10 * Ne[i];
                randomCount = Ne[i] / 3 + 1;
                averageSize = 20 * Tref + Ne[i] * (2 * Tref + Te[i]);
                numCreations = 1 + Nve[i];
            } else if (ddt == DynamicDataTypes.DLLO) {
                secuentialCount = 10 * Ne[i];
                randomCount = Ne[i] / 6 + 1;
                averageSize = 20 * Tref + Ne[i] * (3 * Tref + Te[i]);
                numCreations = 1 + Nve[i];
            } else if (ddt == DynamicDataTypes.SLLAR) {
                secuentialCount = 18 * Ne[i] + 8 * Na;
                randomCount = Nn[i] / 2 + 1;
                averageSize = 21 * Tref + Nn[i] * (21 * Tref + Ne[i] * (Te[i] + Tref));
                numCreations = 2 * Ne[i] + Nve[i];
            } else if (ddt == DynamicDataTypes.DLLAR) {
                secuentialCount = 18 * Ne[i] + 8 * Na;
                randomCount = Nn[i] / 4 + 1;
                averageSize = 21 * Tref + Nn[i] * (22 * Tref + Ne[i] * (Te[i] + Tref));
                numCreations = 2 * Ne[i] + Nve[i];
            } else if (ddt == DynamicDataTypes.SLLARO) {
                secuentialCount = 18 * Ne[i] + 10 * Na;
                randomCount = Nn[i] / 3 + 1;
                averageSize = 22 * Tref + Nn[i] * (21 * Tref + Ne[i] * (Te[i] + Tref));
                numCreations = 2 * Ne[i] + Nve[i];
            } else if (ddt == DynamicDataTypes.DLLARO) {
                secuentialCount = 18 * Ne[i] + 10 * Na;
                randomCount = Nn[i] / 6 + 1;
                averageSize = 22 * Tref + Nn[i] * (22 * Tref + Ne[i] * (Te[i] + Tref));
                numCreations = 2 * Ne[i] + Nve[i];
            }
            performance += 0.00001 * ((randomCount * (3 * (reads + writes - 2) / 4)) + (secuentialCount * ((reads + writes - 2) / 4)) + (2 * numCreations));
            memory += averageSize;
            execTime = (reads + writes) * (1 - cacheMissesL1[i][ddt]) * cacheAccessTime + (reads + writes) * cacheMissesL1[i][ddt] * dramAccessTime + (reads + writes) * cacheMissesL1[i][ddt] * cacheLineSize * (1 / dramBandwith);
            energy += (execTime * cpuPower + (reads + writes) * (1 - cacheMissesL1[i][ddt]) * cacheAccessEnergy + (reads + writes) * cacheMissesL1[i][ddt] * cacheAccessEnergy * cacheLineSize + (reads + writes) * cacheMissesL1[i][ddt] * dramAccessPower * (dramAccessTime + cacheLineSize / dramBandwith));
        }
        solution.getObjectives().set(0, performance);
        solution.getObjectives().set(1, memory);
        solution.getObjectives().set(2, energy);
    }
