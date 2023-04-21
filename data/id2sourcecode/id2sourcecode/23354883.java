    public static NurbsCurve degreeElevate(NurbsCurve curve, int t) {
        if (t <= 0) return curve;
        int dim = curve.getPhyDimension();
        KnotVector U = curve.getKnotVector();
        List<ControlPoint> Pw = curve.getControlPoints();
        int n = Pw.size() - 1;
        int p = curve.getDegree();
        int m = n + p + 1;
        int ph = p + t;
        int ph2 = ph / 2;
        double[][] bezalfs = allocate2DArray(ph + 1, p + 1);
        ControlPoint[] bpts = CONTROLPOINTARRAY_FACTORY.array(p + 1);
        ControlPoint[] ebpts = CONTROLPOINTARRAY_FACTORY.array(ph + 1);
        ControlPoint[] Nextbpts = CONTROLPOINTARRAY_FACTORY.array(p - 1);
        double[] alphas = ArrayFactory.DOUBLES_FACTORY.array(p - 1);
        double[][] Bin = allocate2DArray(ph + 1, ph2 + 1);
        binomialCoef(Bin, ph + 1, ph2 + 1);
        bezalfs[0][0] = bezalfs[ph][p] = 1.0;
        for (int i = 1; i <= ph2; i++) {
            double inv = 1.0 / Bin[ph][i];
            int mpi = min(p, i);
            for (int j = max(0, i - t); j <= mpi; j++) bezalfs[i][j] = inv * Bin[p][j] * Bin[t][i - j];
        }
        for (int i = ph2 + 1; i < ph; i++) {
            int mpi = min(p, i);
            for (int j = max(0, i - t); j <= mpi; j++) bezalfs[i][j] = bezalfs[ph - i][p - j];
        }
        ControlPoint[] Qw = CONTROLPOINTARRAY_FACTORY.array(Pw.size() + Pw.size() * t);
        double[] Uh = ArrayFactory.DOUBLES_FACTORY.array(Pw.size() + Pw.size() * t + ph + 1);
        int mh = ph;
        int kind = ph + 1;
        double ua = U.getValue(0);
        double ub = 0;
        int r = -1;
        int oldr = r;
        int a = p;
        int b = p + 1;
        int cind = 1;
        int rbz, lbz = 1;
        Qw[0] = Pw.get(0).copy();
        for (int i = 0; i <= ph; i++) Uh[i] = ua;
        for (int i = 0; i <= p; i++) bpts[i] = Pw.get(i).copy();
        while (b < m) {
            int i = b;
            while (b < m && abs(U.getValue(b) - U.getValue(b + 1)) < MathTools.EPS) ++b;
            int mul = b - i + 1;
            mh += mul + t;
            ub = U.getValue(b);
            oldr = r;
            r = p - mul;
            if (oldr > 0) lbz = (oldr + 2) / 2; else lbz = 1;
            if (r > 0) rbz = ph - (r + 1) / 2; else rbz = ph;
            if (r > 0) {
                double numer = ub - ua;
                for (int k = p; k > mul; k--) alphas[k - mul - 1] = numer / (U.getValue(a + k) - ua);
                for (int j = 1; j <= r; j++) {
                    int save = r - j;
                    int s = mul + j;
                    for (int k = p; k >= s; k--) {
                        bpts[k] = applyAlpha(bpts[k], bpts[k - 1], alphas[k - s]);
                    }
                    Nextbpts[save] = bpts[p];
                }
            }
            for (i = lbz; i <= ph; i++) {
                ebpts[i] = ControlPoint.newInstance(dim);
                int mpi = min(p, i);
                for (int j = max(0, i - t); j <= mpi; j++) {
                    ControlPoint q1 = bpts[j].applyWeight();
                    q1.times(bezalfs[i][j]);
                    ControlPoint q2 = ebpts[i].applyWeight();
                    q2.plus(q1);
                    ebpts[i] = q2.getHomogeneous();
                    ControlPoint.recycle(q1);
                    ControlPoint.recycle(q2);
                }
            }
            if (oldr > 1) {
                int first = kind - 2;
                int last = kind;
                double den = ub - ua;
                double bet = (ub - Uh[kind - 1]) / den;
                for (int tr = 1; tr < oldr; tr++) {
                    i = first;
                    int j = last;
                    int kj = j - kind + 1;
                    while (j - i > tr) {
                        if (i < cind) {
                            double alf = (ub - Uh[i]) / (ua - Uh[i]);
                            Qw[i] = applyAlpha(Qw[i], Qw[i - 1], alf);
                        }
                        if (j >= lbz) {
                            if (j - tr <= kind - ph + oldr) {
                                double gam = (ub - Uh[j - tr]) / den;
                                ebpts[kj] = applyAlpha(ebpts[kj], ebpts[kj + 1], gam);
                            } else {
                                ebpts[kj] = applyAlpha(ebpts[kj], ebpts[kj + 1], bet);
                            }
                        }
                        ++i;
                        --j;
                        --kj;
                    }
                    --first;
                    ++last;
                }
            }
            if (a != p) for (i = 0; i < ph - oldr; i++) {
                Uh[kind++] = ua;
            }
            for (int j = lbz; j <= rbz; j++) {
                Qw[cind++] = ebpts[j];
            }
            if (b < m) {
                for (int j = 0; j < r; j++) bpts[j] = Nextbpts[j];
                for (int j = r; j <= p; j++) bpts[j] = Pw.get(b - p + j).copy();
                a = b;
                ++b;
                ua = ub;
            } else {
                for (i = 0; i <= ph; i++) Uh[kind + i] = ub;
            }
        }
        FastTable<ControlPoint> newP = FastTable.newInstance();
        n = mh - ph;
        for (int i = 0; i < n; ++i) newP.add(Qw[i]);
        FastTable<Float64> newKVList = FastTable.newInstance();
        m = n + ph + 1;
        for (int i = 0; i < m; ++i) newKVList.add(Float64.valueOf(Uh[i]));
        KnotVector newKV = KnotVector.newInstance(ph, Float64Vector.valueOf(newKVList));
        BasicNurbsCurve newCrv = BasicNurbsCurve.newInstance(newP, newKV);
        recycle2DArray(bezalfs);
        recycle2DArray(Bin);
        ArrayFactory.DOUBLES_FACTORY.recycle(alphas);
        ArrayFactory.DOUBLES_FACTORY.recycle(Uh);
        CONTROLPOINTARRAY_FACTORY.recycle(bpts);
        CONTROLPOINTARRAY_FACTORY.recycle(ebpts);
        CONTROLPOINTARRAY_FACTORY.recycle(Nextbpts);
        CONTROLPOINTARRAY_FACTORY.recycle(Qw);
        FastTable.recycle((FastTable) Pw);
        FastTable.recycle(newP);
        FastTable.recycle(newKVList);
        return newCrv;
    }
