    public synchronized MightyStaticBin1D[] splitApproximately(DoubleArrayList percentages, int k) {
        int percentSize = percentages.size();
        if (k < 1 || percentSize < 2) throw new IllegalArgumentException();
        double[] percent = percentages.elements();
        int noOfBins = percentSize - 1;
        double[] subBins = new double[1 + k * (percentSize - 1)];
        subBins[0] = percent[0];
        int c = 1;
        for (int i = 0; i < noOfBins; i++) {
            double step = (percent[i + 1] - percent[i]) / k;
            for (int j = 1; j <= k; j++) {
                subBins[c++] = percent[i] + j * step;
            }
        }
        double[] quantiles = quantiles(new DoubleArrayList(subBins)).elements();
        MightyStaticBin1D[] splitBins = new MightyStaticBin1D[noOfBins];
        int maxOrderForSumOfPowers = getMaxOrderForSumOfPowers();
        maxOrderForSumOfPowers = Math.min(10, maxOrderForSumOfPowers);
        int dataSize = this.size();
        c = 0;
        for (int i = 0; i < noOfBins; i++) {
            double step = (percent[i + 1] - percent[i]) / k;
            double binSum = 0;
            double binSumOfSquares = 0;
            double binSumOfLogarithms = 0;
            double binSumOfInversions = 0;
            double[] binSumOfPowers = null;
            if (maxOrderForSumOfPowers > 2) {
                binSumOfPowers = new double[maxOrderForSumOfPowers - 2];
            }
            double binMin = quantiles[c++];
            double safe_min = binMin;
            double subIntervalSize = dataSize * step;
            for (int j = 1; j <= k; j++) {
                double binMax = quantiles[c++];
                double binMean = (binMin + binMax) / 2;
                binSum += binMean * subIntervalSize;
                binSumOfSquares += binMean * binMean * subIntervalSize;
                if (this.hasSumOfLogarithms) {
                    binSumOfLogarithms += (Math.log(binMean)) * subIntervalSize;
                }
                if (this.hasSumOfInversions) {
                    binSumOfInversions += (1 / binMean) * subIntervalSize;
                }
                if (maxOrderForSumOfPowers >= 3) binSumOfPowers[0] += binMean * binMean * binMean * subIntervalSize;
                if (maxOrderForSumOfPowers >= 4) binSumOfPowers[1] += binMean * binMean * binMean * binMean * subIntervalSize;
                for (int p = 5; p <= maxOrderForSumOfPowers; p++) {
                    binSumOfPowers[p - 3] += Math.pow(binMean, p) * subIntervalSize;
                }
                binMin = binMax;
            }
            c--;
            int binSize = (int) Math.round((percent[i + 1] - percent[i]) * dataSize);
            double binMax = binMin;
            binMin = safe_min;
            splitBins[i] = new MightyStaticBin1D(this.hasSumOfLogarithms, this.hasSumOfInversions, maxOrderForSumOfPowers);
            if (binSize > 0) {
                splitBins[i].size = binSize;
                splitBins[i].min = binMin;
                splitBins[i].max = binMax;
                splitBins[i].sum = binSum;
                splitBins[i].sum_xx = binSumOfSquares;
                splitBins[i].sumOfLogarithms = binSumOfLogarithms;
                splitBins[i].sumOfInversions = binSumOfInversions;
                splitBins[i].sumOfPowers = binSumOfPowers;
            }
        }
        return splitBins;
    }
