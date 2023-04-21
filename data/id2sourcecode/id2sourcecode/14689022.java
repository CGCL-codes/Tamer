    public void set(double data[][], int n) {
        double x1 = data[0][1];
        System.out.println("Filter lower wavelength " + x1 + "A");
        double x2 = data[0][data[0].length - 1];
        System.out.println("Filter upper wavelength " + x2 + "A");
        double xl, xm, z1, z, pp, p3, p2, p1;
        int m = (n + 1) / 2;
        double EPS = 3 * Math.pow(10, -11);
        double x[] = new double[n + 1];
        double w[] = new double[n + 1];
        xm = 0.5 * (x1 + x2);
        xl = 0.5 * (x2 - x1);
        for (int i = 1; i <= m; i++) {
            z = Math.cos(Math.PI * (i - 0.25) / (n + 0.5));
            do {
                p1 = 1.0;
                p2 = 0.0;
                for (int j = 1; j <= n; j++) {
                    p3 = p2;
                    p2 = p1;
                    p1 = ((2.0 * j - 1.0) * z * p2 - (j - 1.0) * p3) / j;
                }
                pp = n * (z * p1 - p2) / (z * z - 1.0);
                z1 = z;
                z = z1 - p1 / pp;
            } while (Math.abs(z - z1) > EPS);
            x[i] = xm - xl * z;
            x[n + 1 - i] = xm + xl * z;
            w[i] = 2.0 * xl / ((1.0 - z * z) * pp * pp);
            w[n + 1 - i] = w[i];
        }
        setX(x);
        setW(w);
        setN(n);
    }
