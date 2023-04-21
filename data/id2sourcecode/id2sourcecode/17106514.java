    void setMinimum(DasDevicePosition row, double nposition) {
        double minima[] = getMinima();
        double maxima[] = getMaxima();
        int i = rows.indexOf(row);
        minima[i] = nposition;
        double alpha1 = this.row.getMinimum();
        double alpha2 = nposition;
        if (i == 0) {
            this.row.setMinimum(nposition);
        } else {
            int nAbove = i;
            double weight3 = integrateWeight(i);
            double fractionalIntegratedWeight = 0.;
            for (int j = 0; j < i; j++) {
                minima[j] = alpha2 * fractionalIntegratedWeight + alpha1 * (1 - fractionalIntegratedWeight);
                fractionalIntegratedWeight += ((Double) weights.get(j)).doubleValue() / weight3;
            }
        }
        for (int j = 0; j < rows.size() - 1; j++) {
            maxima[j] = minima[j + 1] - interMargin;
        }
        setWeights(minima, maxima);
    }
