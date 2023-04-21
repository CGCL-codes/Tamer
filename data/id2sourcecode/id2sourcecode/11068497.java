    long find(long n) {
        if (n == 0) return 0;
        long k = (n + 1) / 2;
        System.out.println(String.format("%d * %d + find(%d/2)", k, k, n));
        return k * k + find(n / 2);
    }
