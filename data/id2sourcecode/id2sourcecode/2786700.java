    public double getMedian(double a, double b) {
        int sumFreq = 0, numValues = 0, lRank, uRank;
        double lValue = a - 1, uValue = b + 1, w = domain.getWidth();
        for (double x = a; x <= b + 0.5 * w; x = x + w) numValues = numValues + getFreq(x);
        if (2 * (numValues / 2) == numValues) {
            lRank = numValues / 2;
            uRank = lRank + 1;
        } else {
            lRank = (numValues + 1) / 2;
            uRank = lRank;
        }
        for (double x = a; x <= b + 0.5 * w; x = x + w) {
            sumFreq = sumFreq + getFreq(x);
            if ((lValue == a - 1) & (sumFreq >= lRank)) lValue = x;
            if ((uValue == b + 1) & (sumFreq >= uRank)) uValue = x;
        }
        return (uValue + lValue) / 2;
    }
