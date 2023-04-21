    @Override
    double calculateRho() {
        int nrFree1 = 0, nrFree2 = 0;
        double ub1 = INF, ub2 = INF;
        double lb1 = -INF, lb2 = -INF;
        double sumFree1 = 0, sumFree2 = 0;
        for (int i = 0; i < active_size; i++) {
            if (y[i] == +1) {
                if (is_lower_bound(i)) {
                    ub1 = Math.min(ub1, G[i]);
                } else if (is_upper_bound(i)) {
                    lb1 = Math.max(lb1, G[i]);
                } else {
                    ++nrFree1;
                    sumFree1 += G[i];
                }
            } else {
                if (is_lower_bound(i)) {
                    ub2 = Math.min(ub2, G[i]);
                } else if (is_upper_bound(i)) {
                    lb2 = Math.max(lb2, G[i]);
                } else {
                    ++nrFree2;
                    sumFree2 += G[i];
                }
            }
        }
        double r1, r2;
        if (nrFree1 > 0) {
            r1 = sumFree1 / nrFree1;
        } else {
            r1 = (ub1 + lb1) / 2;
        }
        if (nrFree2 > 0) {
            r2 = sumFree2 / nrFree2;
        } else {
            r2 = (ub2 + lb2) / 2;
        }
        si.r = (r1 + r2) / 2;
        return (r1 - r2) / 2;
    }
