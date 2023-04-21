    public void collectPoint(Object x) {
        OptimizingVector v = pointFactory.createVector((DhbVector) x, f);
        if (fillIndex == 0 || bestPoints[fillIndex - 1].betterThan(v)) {
            bestPoints[fillIndex++] = v;
            return;
        }
        int n = 0;
        int m = fillIndex - 1;
        if (bestPoints[0].betterThan(v)) {
            int k;
            while (m - n > 1) {
                k = (n + m) / 2;
                if (v.betterThan(bestPoints[k])) m = k; else n = k;
            }
            n = m;
        }
        for (m = fillIndex; m > n; m--) bestPoints[m] = bestPoints[m - 1];
        bestPoints[n] = v;
        fillIndex += 1;
    }
