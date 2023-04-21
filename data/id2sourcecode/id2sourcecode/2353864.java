    double calculate_rho() {
        double r;
        int nr_free = 0;
        double ub = INF, lb = -INF, sum_free = 0;
        for (int i = 0; i < active_size; i++) {
            double yG = y[i] * G[i];
            if (is_lower_bound(i)) {
                if (y[i] > 0) ub = Math.min(ub, yG); else lb = Math.max(lb, yG);
            } else if (is_upper_bound(i)) {
                if (y[i] < 0) ub = Math.min(ub, yG); else lb = Math.max(lb, yG);
            } else {
                ++nr_free;
                sum_free += yG;
            }
        }
        if (nr_free > 0) r = sum_free / nr_free; else r = (ub + lb) / 2;
        return r;
    }
