    private static int packSizeNum(long n, int e) {
        if (n == 0) return 1;
        if (n < 0) n = -n;
        for (; (e % 4) != 0; --e) n *= 10;
        while (n % 10000 == 0) n /= 10000;
        return 2 + 2 * packshorts(n);
    }
