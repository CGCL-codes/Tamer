    public double supportedHypothesis(String testName, double pvalue) {
        double currentBoost = 1.0;
        double currentPvalue = test(testName, currentBoost);
        double lastBoost = 1.0;
        double lastPvalue = currentPvalue;
        int iterations = 0;
        while ((lastPvalue < pvalue) == (currentPvalue < pvalue)) {
            double nextBoost = currentBoost;
            if (currentPvalue < pvalue) {
                nextBoost *= 1.05;
            } else if (currentPvalue > pvalue) {
                nextBoost *= 0.95;
            }
            double nextPvalue = test(testName, nextBoost);
            lastBoost = currentBoost;
            lastPvalue = currentPvalue;
            currentBoost = nextBoost;
            currentPvalue = nextPvalue;
            iterations++;
            if (iterations > 50) {
                return 0;
            }
        }
        double lowBoost = Math.min(lastBoost, currentBoost);
        double highBoost = Math.max(lastBoost, currentBoost);
        while (highBoost - lowBoost > 0.00005) {
            double middleBoost = (highBoost + lowBoost) / 2;
            currentPvalue = test(testName, middleBoost);
            if (currentPvalue > pvalue) {
                highBoost = middleBoost;
            } else {
                lowBoost = middleBoost;
            }
            iterations++;
            if (iterations > 100) {
                return 0;
            }
        }
        return lowBoost;
    }
