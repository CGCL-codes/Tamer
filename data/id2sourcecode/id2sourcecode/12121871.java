    public void updateRates() {
        if (getState() < Realized) return;
        long now = System.currentTimeMillis();
        long rate, avg;
        if (now == lastStatsTime) rate = lastBitRate; else rate = (long) (getBitRate() * 8.0 / (now - lastStatsTime) * 1000.0);
        avg = (lastBitRate + rate) / 2;
        if (bitRateControl != null) {
            bitRateControl.setBitRate((int) avg);
        }
        lastBitRate = rate;
        lastStatsTime = now;
        resetBitRate();
        for (int i = 0; i < trackControls.length; i++) {
            trackControls[i].updateRates(now);
        }
        source.checkLatency();
    }
