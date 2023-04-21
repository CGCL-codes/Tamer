    public MOLLnav(int[] iparms) throws IllegalArgumentException {
        double res;
        double x;
        int i;
        if (iparms[0] != MOLL) throw new IllegalArgumentException("Invalid navigation type" + iparms[0]);
        itype = 2;
        xrow = iparms[1];
        xcol = iparms[2];
        xqlon = iparms[4];
        drad = iparms[6] / 1000.e0;
        double r = drad;
        if (iparms[14] == KMPP || iparms[14] == PPMK) {
            res = iparms[3];
            rpix = (0.7071 * r) / res;
        } else {
            rpix = iparms[3];
        }
        decc = iparms[7] / 1.e6;
        iwest = iparms[9];
        if (iwest >= 0) iwest = 1;
        icord = iparms[8];
        asq = drad * drad;
        ecc = decc;
        eccsqr = ecc * ecc;
        double dpole = Math.sqrt(asq * (1.e0 - eccsqr));
        bsq = dpole * dpole;
        ab = drad * dpole;
        if (iwest < 0) kwest = 1;
        if (icord < 0) kcord = -1;
        for (i = 0; i < tlat.length; i++) {
            x = i / 100.;
            if (x >= 1.) {
                t[i] = 1.;
                tlat[i] = 1.57080 / DEGREES_TO_RADIANS;
            } else {
                t[i] = x;
                tlat[i] = Math.asin((Math.asin(x) + x * Math.sqrt(1.0 - x * x)) / 1.57080) / DEGREES_TO_RADIANS;
            }
        }
        int n = 100;
        int m = n - 1;
        double w, z, t1, s, u, v, zs, zq, ws, wq, aa, ba, ca, da;
        for (i = 0; i < n; i++) {
            if (i != 0) t1 = (t[i + 1] - t[i - 1]) / (tlat[i + 1] - tlat[i - 1]); else {
                w = (t[1] + t[2]) / 2.0;
                z = (tlat[1] + tlat[2]) / 2.0;
                t1 = (w - t[0]) / (z - tlat[0]);
                t1 = 2.0 * (t[1] - t[0]) / (tlat[1] - tlat[0]) - t1;
            }
            if (i != m) s = (t[i + 2] - t[i]) / (tlat[i + 2] - tlat[i]); else {
                w = (t[n - 1] + t[n - 2]) / 2.0;
                z = (tlat[n - 1] + tlat[n - 2]) / 2.0;
                s = (t[n] - w) / (tlat[n] - z);
                s = 2.0 * (t[n] - t[n - 1]) / (tlat[n] - tlat[n - 1]) - s;
            }
            u = t[i + 1];
            v = t[i];
            w = (tlat[i + 1] + tlat[i]) / 2.0;
            z = (tlat[i + 1] - tlat[i]) / 2.0;
            zs = z * z;
            zq = z * zs;
            ws = w * w;
            wq = w * ws;
            aa = .5 * (u + v) - .25 * z * (s - t1);
            ba = .75 * (u - v) / z - .25 * (s + t1);
            ca = .25 * (s - t1) / z;
            da = .25 * (s + t1) / zs - .25 * (u - v) / zq;
            coef[0][i] = aa - ba * w + ca * ws - da * wq;
            coef[1][i] = ba - 2.0 * ca * w + 3.0 * da * ws;
            coef[2][i] = ca - 3.0 * da * w;
            coef[3][i] = da;
        }
        for (int j = 0; j < 4; j++) {
            coef[j][n] = coef[j][n - 1];
        }
        i = 0;
        int j, k;
        for (int l = 0; l < 91; l++) {
            u = l;
            if (i >= n - 1) i = 0;
            if (u < tlat[i] || u > tlat[i + 1]) {
                i = 0;
                j = n;
                do {
                    k = (i + j) / 2;
                    if (u < tlat[k]) j = k;
                    if (u >= tlat[k]) i = k;
                } while (j > i + 1);
            }
            lattbl[l] = coef[0][i] + u * (coef[1][i] + u * (coef[2][i] + u * coef[3][i]));
        }
    }
