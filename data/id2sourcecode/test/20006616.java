    public static double newtonBisection(Function f, double xleft, double xright, double tol, int icmax) {
        double rtest = 10 * tol;
        double xbest, fleft, fright, fbest, derfbest, delta;
        int icount = 0, iflag = 0;
        fleft = f.evaluate(xleft);
        fright = f.evaluate(xright);
        if (fleft * fright >= 0) {
            iflag = 1;
        }
        switch(iflag) {
            case 1:
                System.out.println("No solution possible");
                break;
        }
        if (Math.abs(fleft) <= Math.abs(fright)) {
            xbest = xleft;
            fbest = fleft;
        } else {
            xbest = xright;
            fbest = fright;
        }
        derfbest = fxprime(f, xbest, tol);
        while ((icount < icmax) && (rtest > tol)) {
            icount++;
            if ((derfbest * (xbest - xleft) - fbest) * (derfbest * (xbest - xright) - fbest) <= 0) {
                delta = -fbest / derfbest;
                xbest = xbest + delta;
            } else {
                delta = (xright - xleft) / 2;
                xbest = (xleft + xright) / 2;
            }
            rtest = Math.abs(delta / xbest);
            if (rtest <= tol) {
            } else {
                fbest = f.evaluate(xbest);
                derfbest = fxprime(f, xbest, tol);
                if (fleft * fbest <= 0) {
                    xright = xbest;
                    fright = fbest;
                } else {
                    xleft = xbest;
                    fleft = fbest;
                }
            }
        }
        if ((icount > icmax) || (rtest > tol)) {
            NumericsLog.fine(icmax + " Newton and bisection trials made - no convergence achieved");
            return Double.NaN;
        }
        return xbest;
    }
