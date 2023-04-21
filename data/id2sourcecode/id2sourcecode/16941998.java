    public static void centerTrials(double[][] trials) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double y = 0;
        for (int i = 0; i < trials.length; i++) {
            if (i % 2 == 0) y += trials[i][1]; else y -= trials[i][1];
            trials[i][3] = y;
            min = Math.min(min, y);
            max = Math.max(max, y);
        }
        double middle = (min + max) / 2;
        for (int i = 0; i < trials.length; i++) trials[i][3] = trials[i][3] - middle;
    }
