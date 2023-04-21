    public static int lpc2lsp(final float[] a, final int lpcrdr, final float[] freq, final int nb, final float delta) {
        float psuml, psumr, psumm, temp_xr, xl, xr, xm = 0;
        float temp_psumr;
        int i, j, m, flag, k;
        int px;
        int qx;
        int p;
        int q;
        float[] pt;
        int roots = 0;
        flag = 1;
        m = lpcrdr / 2;
        Lsp.lpc2lsp_Q = Bits.newOrZeroFloatArray(Lsp.lpc2lsp_Q, m + 1);
        Lsp.lpc2lsp_P = Bits.newOrZeroFloatArray(Lsp.lpc2lsp_P, m + 1);
        px = 0;
        qx = 0;
        p = px;
        q = qx;
        lpc2lsp_P[px++] = 1.0f;
        lpc2lsp_Q[qx++] = 1.0f;
        for (i = 1; i <= m; i++) {
            lpc2lsp_P[px++] = a[i] + a[lpcrdr + 1 - i] - lpc2lsp_P[p++];
            lpc2lsp_Q[qx++] = a[i] - a[lpcrdr + 1 - i] + lpc2lsp_Q[q++];
        }
        px = 0;
        qx = 0;
        for (i = 0; i < m; i++) {
            lpc2lsp_P[px] = 2 * lpc2lsp_P[px];
            lpc2lsp_Q[qx] = 2 * lpc2lsp_Q[qx];
            px++;
            qx++;
        }
        px = 0;
        qx = 0;
        xr = 0;
        xl = 1.0f;
        for (j = 0; j < lpcrdr; j++) {
            if (j % 2 != 0) pt = lpc2lsp_Q; else pt = lpc2lsp_P;
            psuml = cheb_poly_eva(pt, xl, lpcrdr);
            flag = 1;
            while ((flag == 1) && (xr >= -1.0)) {
                float dd;
                dd = (float) (delta * (1 - .9 * xl * xl));
                if (Math.abs(psuml) < .2) dd *= .5;
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
