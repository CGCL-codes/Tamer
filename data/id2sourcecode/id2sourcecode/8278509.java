    public static int findSpan(int n, int p, double u, double[] U) {
        BigDecimal s_case = new BigDecimal(U[n + 1]);
        double d_s_case = s_case.setScale(12, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (u == d_s_case) return n;
        int high = n + 1;
        int low = p;
        int mid = (low + high) / 2;
        BigDecimal u_mid = new BigDecimal(U[mid]);
        double d_u_mid = u_mid.setScale(12, BigDecimal.ROUND_HALF_UP).doubleValue();
        BigDecimal u_mid1 = new BigDecimal(U[mid + 1]);
        double d_u_mid1 = u_mid1.setScale(12, BigDecimal.ROUND_HALF_UP).doubleValue();
        while (u < d_u_mid || u >= d_u_mid1) {
            if (u < d_u_mid) high = mid; else low = mid;
            mid = (low + high) / 2;
            u_mid = new BigDecimal(U[mid]);
            d_u_mid = u_mid.setScale(12, BigDecimal.ROUND_HALF_UP).doubleValue();
            u_mid1 = new BigDecimal(U[mid + 1]);
            d_u_mid1 = u_mid1.setScale(12, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return mid;
    }
