    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "IP_PARAMETER_IS_DEAD_BUT_OVERWRITTEN")
    RetVal RPSpos(int nr, double Tr[], double Xr[], double Yr[], double Zr[], double Vs, double Xt, double Yt, double Zt) {
        int i, j, k, ns;
        double Rq;
        double Rs[] = new double[NMAX];
        double Xs[] = new double[NMAX];
        double Ys[] = new double[NMAX];
        double Zs[] = new double[NMAX];
        double d, da, db, d11, d12, d21, d22;
        double x1a = 0, y1a = 0, z1a = 0, x1b = 0, y1b = 0, z1b = 0;
        double x2a = 0, y2a = 0, z2a = 0, x2b = 0, y2b = 0, z2b = 0;
        double Ww, Xw, Yw, Zw, w;
        ns = 0;
        Rs[NMAX - 1] = TMAX;
        Rmax = Vs * TMAX;
        for (i = 0; i < nr; i++) {
            if (Tr[i] == 0) continue;
            Rq = Vs * (Tr[i] + OFFSET);
            if (Rq >= Rmax) continue;
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
        if (ns < 3) {
            Xt = Yt = Zt = 9.9999999e99;
            return new RetVal(1, Xt, Yt, Zt, Vs);
        }
        da = db = 0.0;
        for (i = 0; i < ns; i++) {
            j = (i + 1) % ns;
            k = (i + 2) % ns;
            xi = Xs[i];
            yi = Ys[i];
            zi = Zs[i];
            ri = Rs[i];
            xj = Xs[j];
            yj = Ys[j];
            zj = Zs[j];
            rj = Rs[j];
            xk = Xs[k];
            yk = Ys[k];
            zk = Zs[k];
            rk = Rs[k];
            if (gps3() == 0) {
                x = x1;
                y = y1;
                z = z1;
                if (wgt() > 0.0) {
                    x = x2;
                    y = y2;
                    z = z2;
                    if (wgt() > 0.0) {
                        d = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
                        if ((d < da) || (da == 0.0)) {
                            da = d;
                            x1a = x1;
                            y1a = y1;
                            z1a = z1;
                            x2a = x2;
                            y2a = y2;
                            z2a = z2;
                        }
                        if (d >= db) {
                            db = d;
                            x1b = x1;
                            y1b = y1;
                            z1b = z1;
                            x2b = x2;
                            y2b = y2;
                            z2b = z2;
                        }
                    }
                }
            }
        }
        if ((da == 0.0) && (db == 0.0)) {
            x = y = z = 0.0;
        } else {
            d11 = (x1a - x1b) * (x1a - x1b) + (y1a - y1b) * (y1a - y1b) + (z1a - z1b) * (z1a - z1b);
            d12 = (x1a - x2b) * (x1a - x2b) + (y1a - y2b) * (y1a - y2b) + (z1a - z2b) * (z1a - z2b);
            d21 = (x2a - x1b) * (x2a - x1b) + (y2a - y1b) * (y2a - y1b) + (z2a - z1b) * (z2a - z1b);
            d22 = (x2a - x2b) * (x2a - x2b) + (y2a - y2b) * (y2a - y2b) + (z2a - z2b) * (z2a - z2b);
            if ((d11 < d12) && (d11 < d21) && (d11 < d22)) {
                x = (x1a + x1b) / 2;
                y = (y1a + y1b) / 2;
                z = (z1a + z1b) / 2;
            } else if ((d12 < d21) && (d12 < d22)) {
                x = (x1a + x2b) / 2;
                y = (y1a + y2b) / 2;
                z = (z1a + z2b) / 2;
            } else if (d21 < d22) {
                x = (x2a + x1b) / 2;
                y = (y2a + y1b) / 2;
                z = (z2a + z1b) / 2;
            } else {
                x = (x2a + x2b) / 2;
                y = (y2a + y2b) / 2;
                z = (z2a + z2b) / 2;
            }
        }
        Ww = Xw = Yw = Zw = 0.0;
        for (i = 0; i < ns - 2; i++) {
            xi = Xs[i];
            yi = Ys[i];
            zi = Zs[i];
            ri = Rs[i];
            for (j = i + 1; j < ns - 1; j++) {
                xj = Xs[j];
                yj = Ys[j];
                zj = Zs[j];
                rj = Rs[j];
                for (k = j + 1; k < ns; k++) {
                    xk = Xs[k];
                    yk = Ys[k];
                    zk = Zs[k];
                    rk = Rs[k];
                    if (gps3() == 0) {
                        if (Ww == 0.0) {
                            x = x0;
                            y = y0;
                            z = z0;
                        }
                        if ((w = wgt()) > 0.0) {
                            Ww += w;
                            Xw += w * x0;
                            Yw += w * y0;
                            Zw += w * z0;
                            x = Xw / Ww;
                            y = Yw / Ww;
                            z = Zw / Ww;
                        }
                    }
                }
            }
        }
        if (Ww > 0.0) {
            Xt = x;
            Yt = y;
            Zt = z;
            return new RetVal(0, Xt, Yt, Zt, Vs);
        } else {
            Xt = Yt = Zt = 9.9999999e99;
            return new RetVal(2, Xt, Yt, Zt, Vs);
        }
    }
