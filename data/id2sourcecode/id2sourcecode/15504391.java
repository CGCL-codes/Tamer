    public void computeBoxSize() {
        double bestLow = Double.MAX_VALUE;
        double bestHigh = Double.MIN_VALUE;
        double sumHighLessLow = 0;
        int bi;
        for (bi = 0; bi < lows.length; bi++) {
            bestLow = Math.min(bestLow, lows[bi]);
            bestHigh = Math.max(bestHigh, highs[bi]);
            sumHighLessLow += (highs[bi] - lows[bi]);
        }
        boxSize = (bestLow + bestHigh) / 2;
        int bs = Double.valueOf(boxSize + .5).intValue();
        boxSize = Double.valueOf("" + bs) / 100;
        boxSize = sumHighLessLow / bi;
    }
