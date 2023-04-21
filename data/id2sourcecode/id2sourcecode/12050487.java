    public static long sum(long number) {
        if (number == 0) return 0;
        long prod = 1;
        for (long k = 2; k * k <= number; ++k) {
            long p = 1;
            while (number % k == 0) {
                p = p * k + 1;
                number /= k;
            }
            prod *= p;
        }
        if (number > 1) prod *= 1 + number;
        return prod;
    }
