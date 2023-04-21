    private static synchronized double calc_deltaT(double tjd, double tid_acc) {
        Trace.level++;
        Trace.trace(Trace.level, "SweDate.deltaT(double, double)");
        double ans = 0., ans2, ans3;
        double p, B = 0., B2, Y = 0., Ygreg, dd;
        int d[] = new int[6];
        int i, iy, k;
        int tabsiz = TABSIZ;
        int tabsiz = init_dt();
        int tabend = TABSTART + tabsiz - 1;
        Y = 2000.0 + (tjd - SwephData.J2000) / 365.25;
        Ygreg = 2000.0 + (tjd - SwephData.J2000) / 365.2425;
        if (Y < TAB2_START) {
            B = (Y - LTERM_EQUATION_YSTART) * 0.01;
            ans = -20 + LTERM_EQUATION_COEFF * B * B;
            ans = adjust_for_tidacc(tid_acc, ans, Y);
            if (Y >= TAB2_START - 100) {
                ans2 = adjust_for_tidacc(tid_acc, dt2[0], TAB2_START);
                B = (TAB2_START - LTERM_EQUATION_YSTART) * 0.01;
                ans3 = -20 + LTERM_EQUATION_COEFF * B * B;
                ans3 = adjust_for_tidacc(tid_acc, ans3, Y);
                dd = ans3 - ans2;
                B = (Y - (TAB2_START - 100)) * 0.01;
                ans = ans - dd * B;
            }
        }
        if (Y >= TAB2_START && Y < TAB2_END) {
            double Yjul = 2000 + (tjd - 2451557.5) / 365.25;
            p = SMath.floor(Yjul);
            iy = (int) ((p - TAB2_START) / TAB2_STEP);
            dd = (Yjul - (TAB2_START + TAB2_STEP * iy)) / TAB2_STEP;
            ans = dt2[iy] + (dt2[iy + 1] - dt2[iy]) * dd;
            ans = adjust_for_tidacc(tid_acc, ans, Y);
        }
        if (Y >= TAB2_END && Y < TABSTART) {
            B = TABSTART - TAB2_END;
            iy = (TAB2_END - TAB2_START) / TAB2_STEP;
            dd = (Y - TAB2_END) / B;
            ans = dt2[iy] + dd * (dt[0] / 100.0 - dt2[iy]);
            ans = adjust_for_tidacc(tid_acc, ans, Y);
        }
        if (Y >= TABSTART && Y <= tabend) {
            p = SMath.floor(Y);
            iy = (int) (p - TABSTART);
            ans = dt[iy];
            k = iy + 1;
            if (k >= tabsiz) return deltatIsDone(ans, Y, B, tid_acc, tabsiz, tabend);
            p = Y - p;
            ans += p * (dt[k] - dt[iy]);
            if ((iy - 1 < 0) || (iy + 2 >= tabsiz)) return deltatIsDone(ans, Y, B, tid_acc, tabsiz, tabend);
            k = iy - 2;
            for (i = 0; i < 5; i++) {
                if ((k < 0) || (k + 1 >= tabsiz)) d[i] = 0; else d[i] = dt[k + 1] - dt[k];
                k += 1;
            }
            for (i = 0; i < 4; i++) d[i] = d[i + 1] - d[i];
            B = 0.25 * p * (p - 1.0);
            ans += B * (d[1] + d[2]);
            if (iy + 2 >= tabsiz) return deltatIsDone(ans, Y, B, tid_acc, tabsiz, tabend);
            for (i = 0; i < 3; i++) d[i] = d[i + 1] - d[i];
            B = 2.0 * B / 3.0;
            ans += (p - 0.5) * B * d[1];
            if ((iy - 2 < 0) || (iy + 3 > tabsiz)) return deltatIsDone(ans, Y, B, tid_acc, tabsiz, tabend);
            for (i = 0; i < 2; i++) d[i] = d[i + 1] - d[i];
            B = 0.125 * B * (p + 1.0) * (p - 2.0);
            ans += B * (d[0] + d[1]);
        }
        return deltatIsDone(ans, Y, B, tid_acc, tabsiz, tabend);
    }
