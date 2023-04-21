    public static long[][] getBinomialDistribution(int min, int max, long total) {
        Random rand = new Random(System.currentTimeMillis());
        int n = max - min;
        long[][] ret = new long[2][n + 1];
        int mean = (n + 1) / 2;
        float p = 1;
        if (n > 0) {
            p = (float) mean / (float) n;
        }
        long count = 0;
        for (int i = 0; i <= n; i++) {
            double p_i = MathUtil.combination(n, i) * Math.pow(p, i) * Math.pow((1 - p), (n - i));
            long count_i = (long) (total * p_i);
            ret[0][i] = i + min;
            ret[1][i] = count_i;
            count += count_i;
        }
        while (count < total) {
            int i = rand.nextInt(n + 1);
            ret[1][i]++;
            count++;
        }
        return ret;
    }
