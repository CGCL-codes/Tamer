    private double getX(double r, double rm, double r1, double rm1, double s, double sm, double s1, double sm1) {
        double x = 0;
        double dzero = r - s;
        double dminuszero = rm - sm;
        double done = r1 - s1;
        double dminusone = rm1 - sm1;
        double a = 2 * (done + dzero);
        double b = dminuszero - dminusone - done - (3 * dzero);
        double c = dzero - dminuszero;
        if (a == 0) {
            x = c / b;
        }
        double discriminant = Math.pow(b, 2) - (4 * a * c);
        if (discriminant >= 0) {
            double rootpos = ((-1 * b) + Math.sqrt(discriminant)) / (2 * a);
            double rootneg = ((-1 * b) - Math.sqrt(discriminant)) / (2 * a);
            if (Math.abs(rootpos) <= Math.abs(rootneg)) {
                x = rootpos;
            } else {
                x = rootneg;
            }
        } else {
            double cr = (rm - r) / (r1 - r + rm - rm1);
            double cs = (sm - s) / (s1 - s + sm - sm1);
            x = (cr + cs) / 2;
        }
        if (x == 0) {
            double ar = ((rm1 - r1 + r - rm) + (rm - r) / x) / (x - 1);
            double as = ((sm1 - s1 + s - sm) + (sm - s) / x) / (x - 1);
            if (as > 0 | ar < 0) {
                double cr = (rm - r) / (r1 - r + rm - rm1);
                double cs = (sm - s) / (s1 - s + sm - sm1);
                x = (cr + cs) / 2;
            }
        }
        return x;
    }
