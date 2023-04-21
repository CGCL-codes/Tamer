    private synchronized float calcTPS(long interval) {
        tps = (float) period * count / interval;
        avg = (avg + tps) / 2;
        if (tps > peak) {
            peak = Math.round(tps);
            peakWhen = System.currentTimeMillis();
        }
        count = 0;
        return tps;
    }
