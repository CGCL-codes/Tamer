    private final int findPeak(final double[] f) {
        double minF = Double.MAX_VALUE;
        double maxF = Double.MIN_VALUE;
        for (int i = 0; i < f.length; i++) {
            if (f[i] > maxF) {
                maxF = f[i];
            }
            if (f[i] < minF) {
                minF = f[i];
            }
        }
        final double spread = maxF - minF;
        final double threshold = minF + (spread * PEAK_RATIO);
        int start = -1;
        for (int i = 0; (-1 == start) && (i < f.length); i++) {
            if (f[i] > threshold) {
                start = i;
            }
        }
        int end = -1;
        if (start > 0) {
            for (int i = start; (-1 == end) && (i < f.length); i++) {
                if (f[i] < threshold) {
                    end = i - 1;
                }
            }
        }
        final int peak = (start + end) / 2;
        return peak;
    }
