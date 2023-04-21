    private static BsplineKnotVector treat_closed_bsp(BsplineKnotVector knotData, int n_kel, double para, double p_tol) {
        int degree = knotData.degree();
        double[] simple_knots;
        int[] simple_knot_multi;
        int ick;
        int chck_intvl;
        int chck_head_s, chck_head_e;
        int chck_tail_s, chck_tail_e;
        int refer;
        int i, j, k;
        simple_knots = new double[n_kel];
        simple_knot_multi = new int[n_kel];
        ick = (-1);
        for (i = 0; i < n_kel; i++) {
            simple_knots[i] = knotData.knotValueAt(i);
            simple_knot_multi[i] = 1;
            if ((ick < 0) && (Math.abs(simple_knots[i] - para) < p_tol)) ick = i;
        }
        chck_intvl = 2 * degree;
        chck_head_s = 1;
        chck_head_e = 2 * degree;
        chck_tail_s = n_kel - (2 * degree);
        chck_tail_e = n_kel - 1;
        refer = 0;
        if ((chck_head_s <= ick) && (ick <= chck_head_e)) refer |= 1;
        if ((chck_tail_s <= ick) && (ick <= chck_tail_e)) refer |= 2;
        if (refer == 0) {
            return knotData;
        }
        switch(refer) {
            case 1:
                j = chck_head_s + 1;
                k = chck_tail_s + 1;
                for (i = 1; i < chck_intvl; i++) {
                    simple_knots[k] = simple_knots[k - 1] + (simple_knots[j] - simple_knots[j - 1]);
                    j++;
                    k++;
                }
                j -= chck_intvl + 1;
                k -= chck_intvl + 1;
                simple_knots[j] = simple_knots[j + 1] - (simple_knots[k + 1] - simple_knots[k]);
                break;
            case 2:
                j = chck_head_e - 1;
                k = chck_tail_e - 1;
                for (i = 1; i < chck_intvl; i++) {
                    simple_knots[j] = simple_knots[j + 1] - (simple_knots[k + 1] - simple_knots[k]);
                    j--;
                    k--;
                }
                simple_knots[j] = simple_knots[j + 1] - (simple_knots[k + 1] - simple_knots[k]);
                break;
            case 3:
                j = chck_head_e - 1;
                k = chck_tail_e - 1;
                simple_knots[k + 1] = simple_knots[k] + (simple_knots[j + 1] - simple_knots[j]);
                j = chck_head_s + 1;
                k = chck_tail_s + 1;
                simple_knots[j - 1] = simple_knots[j] - (simple_knots[k] - simple_knots[k - 1]);
                simple_knots[j - 2] = simple_knots[j - 1] - (simple_knots[k - 1] - simple_knots[k - 2]);
                break;
        }
        try {
            int uicp = 0;
            for (i = 0; i < n_kel; i++) uicp += simple_knot_multi[i];
            if (knotData.isPeriodic()) uicp -= 2 * degree + 1; else uicp -= degree + 1;
            return new BsplineKnotVector(degree, knotData.knotSpec(), knotData.isPeriodic(), n_kel, simple_knot_multi, simple_knots, uicp).beautify();
        } catch (ExceptionGeometryInvalidArgumentValue e) {
            throw new ExceptionGeometryFatal();
        }
    }
