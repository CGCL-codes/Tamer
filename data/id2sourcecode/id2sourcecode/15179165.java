    @Override
    public double cumulativeProbability(double bucketValueStart, double bucketValueEnd, double sampleAverage, double stdDeviation) {
        double x = (bucketValueStart + bucketValueEnd) / 2;
        return Gaussian.phi(x, sampleAverage, stdDeviation);
    }
