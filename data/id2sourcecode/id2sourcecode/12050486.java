    public static int sum(int number) {
        if (number == 0) return 0;
        int prod = 1;
        for (int k = 2; k * k <= number; ++k) {
            int p = 1;
            while (number % k == 0) {
                p = p * k + 1;
                number /= k;
            }
            prod *= p;
        }
        if (number > 1) prod *= 1 + number;
        return prod;
    }
