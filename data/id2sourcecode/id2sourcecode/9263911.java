    public boolean guessAndFit() {
        int n = ds.size();
        double y_min = Double.MAX_VALUE;
        double y_max = -Double.MAX_VALUE;
        double y = 0.;
        for (int i = 0; i < n; i++) {
            y = ds.getY(i);
            if (y > y_max) {
                y_max = y;
            }
            if (y < y_min) {
                y_min = y;
            }
        }
        if (y_min > y_max) {
            return false;
        }
        double y_level = 0.607 * (y_max - y_min) + y_min;
        int n_cross = 0;
        double x_min = Double.MAX_VALUE;
        double x_max = -Double.MAX_VALUE;
        int i_x_min = -1;
        int i_x_max = -1;
        for (int i = 1; i < n; i++) {
            if ((y_level - ds.getY(i - 1)) * (y_level - ds.getY(i)) <= 0.) {
                n_cross++;
                if (x_min > ds.getArrX(i)[0]) {
                    x_min = ds.getArrX(i)[0];
                    i_x_min = i;
                }
                if (x_max < ds.getArrX(i)[0]) {
                    x_max = ds.getArrX(i)[0];
                    i_x_max = i;
                }
            }
        }
        if (x_max <= x_min || i_x_min < 0 || i_x_max < 0) {
            return false;
        }
        if ((i_x_max - i_x_min) < 3) {
            sigma = Math.abs(x_min - x_max) / 2.0;
            center0 = (x_min + x_max) / 2.0 - sigma * 0.1;
            center1 = (x_min + x_max) / 2.0 + sigma * 0.1;
            pedestal = Math.min(Math.abs(y_min), Math.abs(y_max));
            amp = (y_max - y_min);
        } else {
            int i_cent = (i_x_min + i_x_max) / 2;
            int i_min = -1;
            for (int i = i_x_min; i < i_cent; i++) {
                if (ds.getY(i + 1) > ds.getY(i)) {
                    i_min = i + 1;
                }
            }
            if (i_min < 0) {
                return false;
            }
            center0 = ds.getArrX(i_min)[0];
            double sig0 = ds.getArrX(i_min)[0] - ds.getArrX(i_x_min)[0];
            int i_max = -1;
            for (int i = i_x_max; i > i_cent; i--) {
                if (ds.getY(i - 1) > ds.getY(i)) {
                    i_max = i - 1;
                }
            }
            if (i_max < 0) {
                return false;
            }
            center1 = ds.getArrX(i_max)[0];
            double sig1 = ds.getArrX(i_max)[0] - ds.getArrX(i_x_max)[0];
            sigma = (Math.abs(sig0) + Math.abs(sig1)) / 2.0;
            pedestal = Math.min(Math.abs(y_min), Math.abs(y_max));
            amp = (y_max - y_min);
        }
        boolean sigma_incl_ini = sigma_incl;
        boolean amp_incl_ini = amp_incl;
        boolean center0_incl_ini = center0_incl;
        boolean center1_incl_ini = center1_incl;
        boolean pedestal_incl_ini = pedestal_incl;
        sigma_incl = false;
        amp_incl = true;
        center0_incl = false;
        center1_incl = false;
        pedestal_incl = false;
        boolean res = fit(4);
        if (res == false) {
            return res;
        }
        sigma_incl = sigma_incl_ini;
        amp_incl = amp_incl_ini;
        center0_incl = center0_incl_ini;
        center1_incl = center1_incl_ini;
        pedestal_incl = pedestal_incl_ini;
        res = fit(1);
        if (res == false) {
            return res;
        }
        return res;
    }
