    public static double psi(double x) {
        final double piov4 = .785398163397448;
        final double dx0 = 1.461632144968362341262659542325721325;
        final double[] p1 = new double[] { .0089538502298197, 4.77762828042627, 142.441585084029, 1186.45200713425, 3633.51846806499, 4138.10161269013, 1305.60269827897 };
        final double[] q1 = new double[] { 44.8452573429826, 520.752771467162, 2210.0079924783, 3641.27349079381, 1908.310765963, 6.91091682714533e-6 };
        final double[] p2 = new double[] { -2.12940445131011, -7.01677227766759, -4.48616543918019, -.648157123766197 };
        final double[] q2 = new double[] { 32.2703493791143, 89.2920700481861, 54.6117738103215, 7.77788548522962 };
        int i, m, n, nq;
        double d2;
        double w, z;
        double den, aug, sgn, xmx0, xmax1, upper, xsmall;
        xmax1 = (double) Integer.MAX_VALUE;
        d2 = 0.5 / Rf_d1mach(3);
        if (xmax1 > d2) xmax1 = d2;
        xsmall = 1e-9;
        aug = 0.0;
        if (x < 0.5) {
            if (Math.abs(x) <= xsmall) {
                if (x == 0.0) {
                    return 0.0;
                }
                aug = -1.0 / x;
            } else {
                w = -x;
                sgn = piov4;
                if (w <= 0.0) {
                    w = -w;
                    sgn = -sgn;
                }
                if (w >= xmax1) {
                    return 0.0;
                }
                nq = (int) w;
                w -= (double) nq;
                nq = (int) (w * 4.0);
                w = (w - (double) nq * 0.25) * 4.0;
                n = nq / 2;
                if (n + n != nq) {
                    w = 1.0 - w;
                }
                z = piov4 * w;
                m = n / 2;
                if (m + m != n) {
                    sgn = -sgn;
                }
                n = (nq + 1) / 2;
                m = n / 2;
                m += m;
                if (m == n) {
                    if (z == 0.0) {
                        return 0.0;
                    }
                    aug = sgn * (Math.cos(z) / Math.sin(z) * 4.0);
                } else {
                    aug = sgn * (Math.sin(z) / Math.cos(z) * 4.0);
                }
            }
            x = 1.0 - x;
        }
        if (x <= 3.0) {
            den = x;
            upper = p1[0] * x;
            for (i = 1; i <= 5; ++i) {
                den = (den + q1[i - 1]) * x;
                upper = (upper + p1[i]) * x;
            }
            den = (upper + p1[6]) / (den + q1[5]);
            xmx0 = x - dx0;
            return den * xmx0 + aug;
        }
        if (x < xmax1) {
            w = 1.0 / (x * x);
            den = w;
            upper = p2[0] * w;
            for (i = 1; i <= 3; ++i) {
                den = (den + q2[i - 1]) * w;
                upper = (upper + p2[i]) * w;
            }
            aug = upper / (den + q2[3]) - 0.5 / x + aug;
        }
        return aug + Math.log(x);
    }
