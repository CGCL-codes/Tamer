    protected int randomIndex(double[] randomScale) {
        double roll = chromosomeManager.nextDouble();
        if (roll < randomScale[0]) return 0;
        int n = 0;
        int m = randomScale.length;
        int k;
        while (n < m - 1) {
            k = (n + m) / 2;
            if (roll < randomScale[k]) m = k; else n = k;
        }
        return m;
    }
