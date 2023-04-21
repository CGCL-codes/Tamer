    public double[] melFilter(double bin[], int cbin[]) {
        double temp[] = new double[numMelFilters + 2];
        for (int k = 1; k <= numMelFilters; k++) {
            double num1 = 0, num2 = 0;
            for (int i = cbin[k - 1]; i <= cbin[k]; i++) {
                num1 += ((i - cbin[k - 1] + 1) / (cbin[k] - cbin[k - 1] + 1)) * bin[i];
            }
            for (int i = cbin[k] + 1; i <= cbin[k + 1]; i++) {
                num2 += (1 - ((i - cbin[k]) / (cbin[k + 1] - cbin[k] + 1))) * bin[i];
            }
            temp[k] = num1 + num2;
        }
        double fbank[] = new double[numMelFilters];
        for (int i = 0; i < numMelFilters; i++) {
            fbank[i] = temp[i + 1];
        }
        return fbank;
    }
