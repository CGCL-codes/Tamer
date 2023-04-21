    public double refine(double a, double b, double c, double target, double t) {
        if (t < -0.1 || t > 1.1) {
            return -1;
        }
        double y = YforT(t);
        double t0, t1;
        if (y < target) {
            t0 = t;
            t1 = 1;
        } else {
            t0 = 0;
            t1 = t;
        }
        double origt = t;
        double origy = y;
        boolean useslope = true;
        while (y != target) {
            if (!useslope) {
                double t2 = (t0 + t1) / 2;
                if (t2 == t0 || t2 == t1) {
                    break;
                }
                t = t2;
            } else {
                double slope = dYforT(t, 1);
                if (slope == 0) {
                    useslope = false;
                    continue;
                }
                double t2 = t + ((target - y) / slope);
                if (t2 == t || t2 <= t0 || t2 >= t1) {
                    useslope = false;
                    continue;
                }
                t = t2;
            }
            y = YforT(t);
            if (y < target) {
                t0 = t;
            } else if (y > target) {
                t1 = t;
            } else {
                break;
            }
        }
        boolean verbose = false;
        if (false && t >= 0 && t <= 1) {
            y = YforT(t);
            long tdiff = diffbits(t, origt);
            long ydiff = diffbits(y, origy);
            long yerr = diffbits(y, target);
            if (yerr > 0 || (verbose && tdiff > 0)) {
                System.out.println("target was y = " + target);
                System.out.println("original was y = " + origy + ", t = " + origt);
                System.out.println("final was y = " + y + ", t = " + t);
                System.out.println("t diff is " + tdiff);
                System.out.println("y diff is " + ydiff);
                System.out.println("y error is " + yerr);
                double tlow = prev(t);
                double ylow = YforT(tlow);
                double thi = next(t);
                double yhi = YforT(thi);
                if (Math.abs(target - ylow) < Math.abs(target - y) || Math.abs(target - yhi) < Math.abs(target - y)) {
                    System.out.println("adjacent y's = [" + ylow + ", " + yhi + "]");
                }
            }
        }
        return (t > 1) ? -1 : t;
    }
