    RetVal RPSpos(int nr, double Tr[], double Xr[], double Yr[], double Zr[], double Vs, double Xt, double Yt, double Zt) {
        int i = 0, j = 0, jmax = 0, k = 0, l = 0, ns, nss, nxx, nox = 0, S, cmax;
        int[] ce = new int[NMAX];
        double Rq;
        double[] Rs = new double[NMAX], Xs = new double[NMAX], Ys = new double[NMAX], Zs = new double[NMAX];
        double x, y, z, xo = 0., yo = 0., zo = 0., Rmax, Ww, Xw, Yw, Zw, w, q;
        double err, emax, var = 0, vmax, vmin, vold;
        double[] vex = new double[NERR];
        vmax = SMAX * SMAX * Vs * Vs;
        vmin = 1.0 * Vs * Vs;
        ns = 0;
        Rmax = Vs * TMAX;
        Rs[NMAX - 1] = TMAX;
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
        nxx = 0;
        while (cmax != 0) {
            cmax = 0;
            for (i = 0; i < ns; i++) {
                if (ce[i] >= cmax) {
                    if (ce[i] > 0) nxx = ((ce[i] == cmax) ? nxx + 1 : 1);
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
        nss = ns;
        if (ns < 3) {
            Xt = Yt = Zt = 9.9999999e99;
            return new RetVal(0, Xt, Yt, Zt, Vs);
        }
        S = i = 0;
        x = y = 0.0;
        z = -100000.0;
        while (++i < 5000) {
            if (S == 0) {
                j = k = (i - 1) % ns;
                w = 1.0;
            } else if (S == 1) {
                j = k = ns - 1 - i % ns;
                w = 1.0;
            } else {
                if (--k < 0) {
                    j = k = 0;
                    w = 0.0;
                } else {
                    j = k % ns;
                    if ((S == 3) && (j == l)) {
                        j = ((--k > 0) ? k % ns : 0);
                    }
                    w = 1.0 - Rs[j] / Rmax;
                    w = w * w;
                    if (k < 50) w *= 0.02 * (k + 1); else w *= 0.005 * (k + 150);
                }
            }
            if (k >= 0) {
                q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                q = w * (1.0 - Rs[j] / q);
                x += q * (Xs[j] - x);
                y += q * (Ys[j] - y);
                z += q * (Zs[j] - z);
            }
            if (((S == 1) && (i % (50 + ns) == 51)) || ((S >= 2) && (k <= 0))) {
                vold = var;
                Ww = Xw = Yw = Zw = var = emax = 0.0;
                for (j = 0; j < ns; j++) {
                    if ((S == 3) && (j == l)) continue;
                    q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                    err = q - Rs[j];
                    err = err * err;
                    q = 1.0 - Rs[j] / q;
                    if (S >= 2) {
                        w = 1.0 - Rs[j] / Rmax;
                        w = w * w;
                    } else w = 1.0;
                    Xw += w * (x + q * (Xs[j] - x));
                    Yw += w * (y + q * (Ys[j] - y));
                    Zw += w * (z + q * (Zs[j] - z));
                    Ww += w;
                    var += w * err;
                    if (w * err > emax) {
                        emax = w * err;
                        jmax = j;
                    }
                }
                x = Xw / Ww;
                y = Yw / Ww;
                z = Zw / Ww;
                var = var / Ww;
                i += ns - 1;
                if (((S == 2) && (ns > NERR) && (var > 3 * vmax)) || ((S == 4) && (ns == 4))) {
                    --ns;
                    nox = 0;
                    q = Rs[jmax];
                    Rs[jmax] = Rs[ns];
                    Rs[ns] = q;
                    q = Xs[jmax];
                    Xs[jmax] = Xs[ns];
                    Xs[ns] = q;
                    q = Ys[jmax];
                    Ys[jmax] = Ys[ns];
                    Ys[ns] = q;
                    q = Zs[jmax];
                    Zs[jmax] = Zs[ns];
                    Zs[ns] = q;
                } else ++nox;
                if (S == 1) {
                    if (var < vmax) {
                        k = 250;
                        nox = 0;
                        S = 6;
                    } else if ((var < 3 * vmax) || (i >= 750)) {
                        if (ns <= 4) {
                            k = 300;
                            S = (var > 36 * vmax) ? 4 : 5;
                        } else if (ns <= NERR) {
                            l = 0;
                            xo = x;
                            yo = y;
                            zo = z;
                            k = 250;
                            S = 3;
                        } else {
                            k = 200;
                            S = 2;
                        }
                    }
                } else if (S == 2) {
                    if (var < vmax) {
                        k = 300;
                        S = 5;
                    } else if (ns <= NERR) {
                        l = 0;
                        xo = x;
                        yo = y;
                        zo = z;
                        k = 250;
                        S = 3;
                    } else if (i >= 2000) {
                        ns = NERR;
                        l = 0;
                        xo = x;
                        yo = y;
                        zo = z;
                        k = 250;
                        S = 3;
                    }
                    k = 200;
                } else if (S == 3) {
                    if (ns > 4) {
                        vex[l] = var;
                        if (++l < ns) {
                            k = 250;
                            x = xo;
                            y = yo;
                            z = zo;
                        } else {
                            var = vex[j = 0];
                            for (l = 1; l < ns; l++) {
                                if (vex[l] < var) var = vex[j = l];
                            }
                            --ns;
                            q = Rs[j];
                            Rs[j] = Rs[ns];
                            Rs[ns] = q;
                            q = Xs[j];
                            Xs[j] = Xs[ns];
                            Xs[ns] = q;
                            q = Ys[j];
                            Ys[j] = Ys[ns];
                            Ys[ns] = q;
                            q = Zs[j];
                            Zs[j] = Zs[ns];
                            Zs[ns] = q;
                            if (var < vmax) {
                                k = 300;
                                S = 5;
                            } else if (ns <= 4) {
                                k = 300;
                                S = (var > 36 * vmax) ? 4 : 5;
                            } else {
                                l = 0;
                                xo = x;
                                yo = y;
                                zo = z;
                                k = 250;
                            }
                        }
                    }
                } else if (S == 4) {
                    k = 300;
                    S = 5;
                } else if (S == 5) {
                    for (j = ns; j < nss; j++) {
                        q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                        if ((Rs[j] - q) * (Rs[j] - q) < 4 * vmax) {
                            q = Rs[j];
                            Rs[j] = Rs[ns];
                            Rs[ns] = q;
                            q = Xs[j];
                            Xs[j] = Xs[ns];
                            Xs[ns] = q;
                            q = Ys[j];
                            Ys[j] = Ys[ns];
                            Ys[ns] = q;
                            q = Zs[j];
                            Zs[j] = Zs[ns];
                            Zs[ns] = q;
                            ++ns;
                        }
                    }
                    k = 250;
                    nox = 0;
                    S = 6;
                } else if (S == 6) {
                    if ((nox >= 1 + 110 / (ns + 5)) && ((var > 0.999 * vold) || (var < vmin))) {
                        break;
                    }
                }
            }
            if ((S == 0) && (i >= 50)) {
                k = j;
                S = 1;
            }
        }
        Xt = x;
        Yt = y;
        Zt = z;
        if ((var > vmax) || ((ns == 3) && (var > vmin))) {
            return new RetVal(-ns, Xt, Yt, Zt, Vs);
        }
        if ((ns == 3) && (nxx > 1)) {
            return new RetVal(1, Xt, Yt, Zt, Vs);
        }
        if (nss >= (3 * ns - 5)) {
            return new RetVal(2, Xt, Yt, Zt, Vs);
        }
        return new RetVal(ns, Xt, Yt, Zt, Vs);
    }
