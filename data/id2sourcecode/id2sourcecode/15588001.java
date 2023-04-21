    public static int lpc2lsp(final float[] a, final int lpcrdr, final float[] freq, final int nb, final float delta) {
        float psuml, psumr, psumm, temp_xr, xl, xr, xm = 0;
        float temp_psumr;
        int i, j, m, flag, k;
        float[] Q;
        float[] P;
        int px;
        int qx;
        int p;
        int q;
        float[] pt;
        int roots = 0;
        flag = 1;
        m = lpcrdr / 2;
        Q = new float[m + 1];
        P = new float[m + 1];
        px = 0;
        qx = 0;
        p = px;
        q = qx;
        P[px++] = 1.0f;
        Q[qx++] = 1.0f;
        for (i = 1; i <= m; i++) {
            P[px++] = a[i] + a[lpcrdr + 1 - i] - P[p++];
            Q[qx++] = a[i] - a[lpcrdr + 1 - i] + Q[q++];
        }
        px = 0;
        qx = 0;
        for (i = 0; i < m; i++) {
            P[px] = 2 * P[px];
            Q[qx] = 2 * Q[qx];
            px++;
            qx++;
        }
        px = 0;
        qx = 0;
        xr = 0;
        xl = 1.0f;
        for (j = 0; j < lpcrdr; j++) {
            if (j % 2 != 0) pt = Q; else pt = P;
            psuml = cheb_poly_eva(pt, xl, lpcrdr);
            flag = 1;
            while ((flag == 1) && (xr >= -1.0)) {
                float dd;
                dd = (float) (delta * (1f - .9f * xl * xl));
                if (Math.abs(psuml) < .2f) dd *= .5f;
                xr = xl - dd;
                psumr = cheb_poly_eva(pt, xr, lpcrdr);
                temp_psumr = psumr;
                temp_xr = xr;
                if ((psumr * psuml) < 0.0) {
                    roots++;
                    psumm = psuml;
                    for (k = 0; k <= nb; k++) {
                        xm = (xl + xr) / 2;
                        psumm = cheb_poly_eva(pt, xm, lpcrdr);
                        if (psumm * psuml > 0.) {
                            psuml = psumm;
                            xl = xm;
                        } else {
                            psumr = psumm;
                            xr = xm;
                        }
                    }
                    freq[j] = xm;
                    xl = xm;
                    flag = 0;
                } else {
                    psuml = temp_psumr;
                    xl = temp_xr;
                }
            }
        }
        return roots;
    }
