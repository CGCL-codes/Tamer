    double calculate_rho() {
        int nr_free1 = 0, nr_free2 = 0;
        double ub1 = INF, ub2 = INF;
        double lb1 = -INF, lb2 = -INF;
        double sum_free1 = 0, sum_free2 = 0;
        for (int i = 0; i < active_size; i++) {
            if (y[i] == +1) {
                if (is_lower_bound(i)) {
                    ub1 = Math.min(ub1, G[i]);
                } else if (is_upper_bound(i)) {
                    lb1 = Math.max(lb1, G[i]);
                } else {
                    ++nr_free1;
                    sum_free1 += G[i];
                }
            } else {
                if (is_lower_bound(i)) {
                    ub2 = Math.min(ub2, G[i]);
                } else if (is_upper_bound(i)) {
                    lb2 = Math.max(lb2, G[i]);
                } else {
                    ++nr_free2;
                    sum_free2 += G[i];
                }
            }
        }
        double r1, r2;
        if (nr_free1 > 0) {
            r1 = sum_free1 / nr_free1;
        } else {
            r1 = (ub1 + lb1) / 2;
        }
        if (nr_free2 > 0) {
            r2 = sum_free2 / nr_free2;
        } else {
            r2 = (ub2 + lb2) / 2;
        }
        si.r = (r1 + r2) / 2;
        return (r1 - r2) / 2;
    }
