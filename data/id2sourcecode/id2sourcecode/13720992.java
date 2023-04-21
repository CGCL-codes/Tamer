    public static BigDecimal secondsBigDecimalFromDuration(long s, int n) {
        if (n == 0) return BigDecimal.valueOf(s);
        int scale = 9;
        boolean huge = (int) s != s;
        long ns = huge ? n : s * 1000000000L + n;
        while (ns % 10 == 0) {
            ns = ns / 10;
            scale--;
        }
        BigDecimal dec = new BigDecimal(BigInteger.valueOf(ns), scale);
        if (huge) dec = BigDecimal.valueOf(s).add(dec);
        return dec;
    }
