    public int inverseCumulativeProbability(final double p) throws MathException {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be between 0 and 1.0 (inclusive)");
        }
        int x0 = getDomainLowerBound(p);
        int x1 = getDomainUpperBound(p);
        double pm;
        while (x0 < x1) {
            int xm = x0 + (x1 - x0) / 2;
            pm = cumulativeProbability(xm);
            if (pm > p) {
                if (xm == x1) {
                    --x1;
                } else {
                    x1 = xm;
                }
            } else {
                if (xm == x0) {
                    ++x0;
                } else {
                    x0 = xm;
                }
            }
        }
        pm = cumulativeProbability(x0);
        while (pm > p) {
            --x0;
            pm = cumulativeProbability(x0);
        }
        return x0;
    }
