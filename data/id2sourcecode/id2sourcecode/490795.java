    protected double partonx6(int iprtn, double x, double q) {
        if (x != xold || q != qold) {
            if (setChange) {
                setChange = false;
                xvpow = new double[xv.length];
                xvpow[0] = 0.0;
                for (int i = 1; i <= nx; ++i) xvpow[i] = Math.pow(xv[i], XPOW);
            }
            xold = x;
            qold = q;
            tt = Math.log(Math.log(q / alambda));
            jlx = -1;
            int ju = nx + 1;
            while (ju - jlx > 1) {
                int jm = (ju + jlx) / 2;
                if (x >= xv[jm]) jlx = jm; else ju = jm;
            }
            jx = -1;
            if (jlx <= -1) {
                throw new IllegalArgumentException("x <= 0 in partonx6, x = " + x);
            } else if (jlx == 0) {
                jx = 0;
            } else if (jlx <= nx - 2) {
                jx = jlx - 1;
            } else if (jlx == nx - 1 || x < ONEP) {
                jx = jlx - 2;
            } else {
                throw new IllegalArgumentException("x > 1 in partonx6, x = " + x);
            }
            ss = Math.pow(x, XPOW);
            if (jlx >= 2 && jlx <= nx - 2) {
                double svec1 = xvpow[jx];
                double svec2 = xvpow[jx + 1];
                double svec3 = xvpow[jx + 2];
                double svec4 = xvpow[jx + 3];
                double s12 = svec1 - svec2;
                double s13 = svec1 - svec3;
                s23 = svec2 - svec3;
                double s24 = svec2 - svec4;
                double s34 = svec3 - svec4;
                sy2 = ss - svec2;
                sy3 = ss - svec3;
                const1 = s13 / s23;
                const2 = s12 / s23;
                const3 = s34 / s23;
                const4 = s24 / s23;
                double s1213 = s12 + s13;
                double s2434 = s24 + s34;
                double sdet = s12 * s34 - s1213 * s2434;
                double tmp = sy2 * sy3 / sdet;
                const5 = (s34 * sy2 - s2434 * sy3) * tmp / s12;
                const6 = (s1213 * sy2 - s12 * sy3) * tmp / s34;
            }
            jlq = -1;
            ju = nt + 1;
            while (ju - jlq > 1) {
                int jm = (ju + jlq) / 2;
                if (tt >= tv[jm]) jlq = jm; else ju = jm;
            }
            if (jlq <= 0) {
                jq = 0;
            } else if (jlq <= nt - 2) {
                jq = jlq - 1;
            } else {
                jq = nt - 3;
            }
            if (jlq >= 1 && jlq <= nt - 2) {
                double tvec1 = tv[jq];
                double tvec2 = tv[jq + 1];
                double tvec3 = tv[jq + 2];
                double tvec4 = tv[jq + 4];
                t12 = tvec1 - tvec2;
                t13 = tvec1 - tvec3;
                t23 = tvec2 - tvec3;
                t24 = tvec2 - tvec4;
                t34 = tvec3 - tvec4;
                ty2 = tt - tvec2;
                ty3 = tt - tvec3;
                tmp1 = t12 + t13;
                tmp2 = t24 + t34;
                tdet = t12 * t34 - tmp1 * tmp2;
            }
        }
        int ip = (iprtn > mxval ? -iprtn : iprtn);
        int jtmp = ((ip + nfmx) * (nt + 1) + (jq - 1)) * (nx + 1) + jx + 1;
        double[] fvec = new double[NQVEC];
        for (int it = 1; it <= NQVEC; ++it) {
            int j1 = jtmp + it * (nx + 1);
            if (jx == 0) {
                double[] fij = new double[4];
                fij[0] = 0.0;
                fij[1] = getUpd(j1 + 1) * xv[1] * xv[1];
                fij[2] = getUpd(j1 + 2) * xv[2] * xv[2];
                fij[3] = getUpd(j1 + 3) * xv[3] * xv[3];
                double fx = polint4f(xvpow, 0, fij, 0, ss);
                if (x > 0.0) fvec[it - 1] = fx / (x * x);
            } else if (jlx == nx - 1) {
                double fx = polint4f(xvpow, nx - 3, upd, j1 - 1, ss);
                fvec[it - 1] = fx;
            } else {
                double sf2 = getUpd(j1 + 1);
                double sf3 = getUpd(j1 + 2);
                double g1 = sf2 * const1 - sf3 * const2;
                double g4 = -sf2 * const3 + sf3 * const4;
                fvec[it - 1] = (const5 * (getUpd(j1) - g1) + const6 * (getUpd(j1 + 3) - g4) + sf2 * sy3 - sf3 * sy2) / s23;
            }
        }
        double ff = -1.0;
        if (jlq <= 0) {
            ff = polint4f(tv, 0, fvec, 0, tt);
        } else if (jlq >= nt - 1) {
            ff = polint4f(tv, nt - 3, fvec, 0, tt);
        } else {
            double tf2 = fvec[1];
            double tf3 = fvec[2];
            double g1 = (tf2 * t13 - tf3 * t12) / t23;
            double g4 = (-tf2 * t34 + tf3 * t24) / t23;
            double h00 = ((t34 * ty2 - tmp2 * ty3) * (fvec[0] - g1) / t12 + (tmp1 * ty2 - t12 * ty3) * (fvec[3] - g4) / t34);
            ff = (h00 * ty2 * ty3 / tdet + tf2 * ty3 - tf3 * ty2) / t23;
        }
        return ff;
    }
