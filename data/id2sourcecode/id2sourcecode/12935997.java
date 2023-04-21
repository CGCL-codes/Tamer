    private boolean isOK(long b, int exp, long n) {
        while (n % b == 0) {
            n /= b;
            exp--;
        }
        return exp == 0 && n == 1;
    }
