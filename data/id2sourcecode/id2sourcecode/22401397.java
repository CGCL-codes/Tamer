    public double[] doRegression(double[] tmpdata, double tmp2, double tmp3) {
        this.data = tmpdata;
        double a = 1.05;
        sumlog();
        double lf = f(a);
        double rf, mf;
        double la, ra, ma;
        ma = la = a;
        rf = lf;
        for (ra = la; lf * rf > 0; la = ra, ra += STEP) {
            rf = f(ra);
            if (ra > 1000) {
                return null;
            }
        }
        for (int i = 0; i < 10; ++i) {
            ma = (ra + la) / 2;
            if ((mf = f(ma)) * rf < 0) {
                lf = mf;
                la = ma;
            } else {
                rf = mf;
                ra = ma;
            }
        }
        System.out.println(ma);
        System.out.println(mean);
        gd = new GammaDistribution(ma, ma / mean, 1000, 10000);
        return null;
    }
