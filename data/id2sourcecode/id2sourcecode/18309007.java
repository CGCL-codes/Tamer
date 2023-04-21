    private int getFactorsCount(int k) {
        int res = 0;
        int i;
        for (i = 2; i * i <= k; i++) {
            while (k % i == 0) {
                k /= i;
                res++;
            }
        }
        if (k > 1) {
            res++;
        }
        return res;
    }
