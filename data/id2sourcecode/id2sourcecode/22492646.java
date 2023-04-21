    public void steplsqr(PaceMatrix b, IntVector pvt, int ks, int j, boolean adjoin) {
        final int kp = pvt.size();
        int[] p = pvt.getArray();
        if (adjoin) {
            int pj = p[j];
            pvt.swap(ks, j);
            double dq[] = h1(pj, ks);
            int pk;
            for (int k = ks + 1; k < kp; k++) {
                pk = p[k];
                h2(pj, ks, dq[1], this, pk);
            }
            h2(pj, ks, dq[1], b, 0);
            A[ks][pj] = dq[0];
            for (int k = ks + 1; k < m; k++) A[k][pj] = 0;
        } else {
            int pj = p[j];
            for (int i = j; i < ks - 1; i++) p[i] = p[i + 1];
            p[ks - 1] = pj;
            double[] cs;
            for (int i = j; i < ks - 1; i++) {
                cs = g1(A[i][p[i]], A[i + 1][p[i]]);
                for (int l = i; l < kp; l++) g2(cs, i, i + 1, p[l]);
                for (int l = 0; l < b.n; l++) b.g2(cs, i, i + 1, l);
            }
        }
    }
