    public static void gaussQuadCoeff(double[] gaussQuadDist, double[] gaussQuadWeight, int n) {
        double z = 0.0D, z1 = 0.0D;
        double pp = 0.0D, p1 = 0.0D, p2 = 0.0D, p3 = 0.0D;
        double eps = 3e-11;
        double x1 = -1.0D;
        double x2 = 1.0D;
        int m = (n + 1) / 2;
        double xm = 0.5D * (x2 + x1);
        double xl = 0.5D * (x2 - x1);
        for (int i = 1; i <= m; i++) {
            z = Math.cos(Math.PI * (i - 0.25D) / (n + 0.5D));
            do {
                p1 = 1.0D;
                p2 = 0.0D;
                for (int j = 1; j <= n; j++) {
                    p3 = p2;
                    p2 = p1;
                    p1 = ((2.0D * j - 1.0D) * z * p2 - (j - 1.0D) * p3) / j;
                }
                pp = n * (z * p1 - p2) / (z * z - 1.0D);
                z1 = z;
                z = z1 - p1 / pp;
            } while (Math.abs(z - z1) > eps);
            gaussQuadDist[i - 1] = xm - xl * z;
            gaussQuadDist[n - i] = xm + xl * z;
            gaussQuadWeight[i - 1] = 2.0 * xl / ((1.0 - z * z) * pp * pp);
            gaussQuadWeight[n - i] = gaussQuadWeight[i - 1];
        }
    }
