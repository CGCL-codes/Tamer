    public void fft(final Datensatz d, boolean invert) {
        int mit2, iter, irem, it, it2, nxp, nxp2, m, mxp, j1, j2, k, n, i, j;
        double wre, wim, tre, tim;
        n = d.xre.length;
        for (iter = 0, irem = n / 2; irem != 0; irem /= 2, iter++) ;
        if (this.dimension != n) this.ini_fft(n, iter);
        for (it = 0, nxp2 = n, it2 = 1; it < iter; it++, it2 *= 2) {
            nxp = nxp2;
            nxp2 = nxp / 2;
            for (m = 0, mit2 = 0; m < nxp2; m++, mit2 += it2) {
                wre = this.cosinus[mit2];
                wim = invert ? this.sinus[mit2] : -this.sinus[mit2];
                for (mxp = nxp, j1 = m; mxp <= n; mxp += nxp, j1 += nxp) {
                    j2 = j1 + nxp2;
                    tre = d.xre[j1] - d.xre[j2];
                    tim = d.xim[j1] - d.xim[j2];
                    d.xre[j1] += d.xre[j2];
                    d.xre[j2] = tre * wre - tim * wim;
                    d.xim[j1] += d.xim[j2];
                    d.xim[j2] = tre * wim + tim * wre;
                }
            }
        }
        for (i = 0, j = 0; i < n - 1; i++) {
            if (i < j) {
                tre = d.xre[j];
                d.xre[j] = d.xre[i];
                d.xre[i] = tre;
                tim = d.xim[j];
                d.xim[j] = d.xim[i];
                d.xim[i] = tim;
            }
            k = n / 2;
            while (k <= j) {
                j -= k;
                k = (k + 1) / 2;
            }
            j += k;
        }
        if (!invert) for (i = 0; i < n; i++) {
            d.xre[i] /= n;
            d.xim[i] /= n;
        }
    }
