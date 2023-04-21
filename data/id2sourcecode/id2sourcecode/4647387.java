    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "IP_PARAMETER_IS_DEAD_BUT_OVERWRITTEN")
    RetVal RPSpos(int nr, double Tr[], double Xr[], double Yr[], double Zr[], double Vs, double Xt, double Yt, double Zt) {
        int i, j, k, ns, cmax;
        int[] ce = new int[NMAX];
        double Rq;
        double[] Rs = new double[NMAX];
        double[] Xs = new double[NMAX];
        double[] Ys = new double[NMAX];
        double[] Zs = new double[NMAX];
        double x, y, z, Rmax;
        double Ww, Xw, Yw, Zw, w, q;
        double err, var, vmax, vmin;
        j = k = 0;
        var = 0;
        vmax = SMAX * SMAX * Vs * Vs;
        vmin = 1.0 * Vs * Vs;
        ns = 0;
        Rs[NMAX - 1] = TMAX;
        Rmax = Vs * TMAX;
        for (i = 0; i < nr; i++) {
            if (Tr[i] == 0.0) continue;
            Rq = Vs * (Tr[i] + offset);
            if ((Rq >= Rmax) || (Rq < Vs * TMIN)) continue;
            if (ns == 0) {
                Rs[0] = Rq;
                Xs[0] = Xr[i];
                Ys[0] = Yr[i];
                Zs[0] = Zr[i];
                ns = 1;
            } else {
                j = ((ns == NMAX) ? (ns - 1) : (ns++));
                for (; ; j--) {
                    if ((j > 0) && (Rq < Rs[j - 1])) {
                        Rs[j] = Rs[j - 1];
                        Xs[j] = Xs[j - 1];
                        Ys[j] = Ys[j - 1];
                        Zs[j] = Zs[j - 1];
                    } else {
                        if ((j < NMAX - 1) || (Rq < Rs[j])) {
                            Rs[j] = Rq;
                            Xs[j] = Xr[i];
                            Ys[j] = Yr[i];
                            Zs[j] = Zr[i];
                        }
                        break;
                    }
                }
            }
        }
        for (i = 0; i < ns; i++) ce[i] = 0;
        for (i = 0; i < ns - 1; i++) {
            for (j = i + 1; j < ns; j++) {
                q = Math.sqrt((Xs[i] - Xs[j]) * (Xs[i] - Xs[j]) + (Ys[i] - Ys[j]) * (Ys[i] - Ys[j]) + (Zs[i] - Zs[j]) * (Zs[i] - Zs[j]));
                if ((Rs[i] + Rs[j] < q) || (Rs[i] - Rs[j] > q) || (Rs[j] - Rs[i] > q)) {
                    ++ce[i];
                    ++ce[j];
                }
            }
        }
        cmax = 1;
        while (cmax != 0) {
            cmax = 0;
            for (i = 0; i < ns; i++) {
                if (ce[i] >= cmax) {
                    cmax = ce[i];
                    j = i;
                }
            }
            if (cmax > 0) {
                for (i = 0; i < ns; i++) {
                    if (i == j) continue;
                    q = Math.sqrt((Xs[i] - Xs[j]) * (Xs[i] - Xs[j]) + (Ys[i] - Ys[j]) * (Ys[i] - Ys[j]) + (Zs[i] - Zs[j]) * (Zs[i] - Zs[j]));
                    if ((Rs[i] + Rs[j] < q) || (Rs[i] - Rs[j] > q) || (Rs[j] - Rs[i] > q)) {
                        --ce[i];
                    }
                }
                for (i = j; i < ns - 1; i++) {
                    Rs[i] = Rs[i + 1];
                    Xs[i] = Xs[i + 1];
                    Ys[i] = Ys[i + 1];
                    Zs[i] = Zs[i + 1];
                    ce[i] = ce[i + 1];
                }
                --ns;
            }
        }
        if (ns < 3) {
            Xt = Yt = Zt = 9.9999999e99;
            return new RetVal(1, Xt, Yt, Zt, Vs);
        }
        x = y = 0.0;
        z = -100000.0;
        for (i = 0; i < 1250; i++) {
            if (i < 50) j = k = i % ns; else if (i < 1000) {
                while ((j = (int) Math.floor((ns) * Math.random())) == k) {
                }
                k = j;
            } else j = (1249 - i) % ns;
            if (i < 750) w = 1.0; else {
                w = 1.0 - Rs[j] / Rmax;
                w = w * w;
            }
            if (i >= 1000) w *= 5.0 - 0.004 * i;
            q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
            q = w * (1.0 - Rs[j] / q);
            x += q * (Xs[j] - x);
            y += q * (Ys[j] - y);
            z += q * (Zs[j] - z);
        }
        for (i = 0; i < 15; i++) {
            Ww = Xw = Yw = Zw = var = 0.0;
            for (j = 0; j < ns; j++) {
                q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                err = q - Rs[j];
                q = 1.0 - Rs[j] / q;
                w = 1.0 - Rs[j] / Rmax;
                w = w * w;
                Xw += w * (x + q * (Xs[j] - x));
                Yw += w * (y + q * (Ys[j] - y));
                Zw += w * (z + q * (Zs[j] - z));
                Ww += w;
                var += w * err * err;
            }
            x = Xw / Ww;
            y = Yw / Ww;
            z = Zw / Ww;
            var = var / Ww;
        }
        Xt = x;
        Yt = y;
        Zt = z;
        if ((var > vmax) || ((ns == 3) && (var > vmin))) {
            return new RetVal(2, Xt, Yt, Zt, Vs);
        }
        return new RetVal(0, Xt, Yt, Zt, Vs);
    }
