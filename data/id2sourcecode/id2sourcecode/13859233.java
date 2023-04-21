    public int adaptiveThreshold(int threshold) {
        int mLeft = computeM(0, threshold);
        int mRight = computeM(threshold, amount_bins);
        int newThr = (mLeft + mRight) / 2;
        if (Math.abs(newThr - threshold) <= 1) return threshold;
        return adaptiveThreshold(newThr);
    }
