    public static void genPts(ParametricCurve pc, double t_min, double t_max, MultiPath mp) {
        if (t_min > t_max) throw new IllegalArgumentException("t_min <= t_max required.");
        int n = mp.getDimension();
        double t1 = t_min;
        double t2 = t_max;
        double[][] stack = new double[10][];
        int count = 0;
        double[] rdy = new double[n + 1];
        rdy[n] = t1;
        pc.eval(rdy);
        double[] p = new double[n + 1];
        p[n] = t2;
        pc.eval(p);
        stack[count++] = p;
        double[][] limit = new double[pc.getSampleLimit()][];
        double flatSq = mp.getFlatness() * mp.getFlatness();
        double[] d = new double[n + 1];
        while (true) {
            double m = (t1 + t2) / 2;
            double[] pt = new double[n + 1];
            pt[n] = m;
            pc.eval(pt);
            double dist = Geom.ptSegDistSq(rdy, stack[count - 1], pt, d, n);
            if (Double.isNaN(dist) || Double.isInfinite(dist)) {
                String msg = "NaN or infinity resulted from calling the eval method of the " + pc.getClass().getName() + " class.";
                throw new RuntimeException(msg);
            }
            boolean flag = false;
            if (dist < flatSq) {
                int i = 0;
                double mm = 0;
                for (; i < limit.length; i++) {
                    mm = (t1 + m) / 2;
                    double[] q = new double[n + 1];
                    limit[i] = q;
                    q[n] = mm;
                    pc.eval(q);
                    if (Geom.ptSegDistSq(rdy, pt, q, d, n) >= flatSq) break; else m = mm;
                }
                if (i == limit.length) flag = true; else {
                    stack = checkSpace(stack, count);
                    stack[count++] = pt;
                    for (int j = 0; j <= i; j++) {
                        stack = checkSpace(stack, count);
                        stack[count++] = limit[j];
                    }
                    t2 = mm;
                }
            }
            if (flag) {
                mp.lineTo(rdy);
                mp.lineTo(pt);
                rdy = stack[--count];
                if (count == 0) break;
                pt = stack[count - 1];
                t1 = t2;
                t2 = pt[n];
            } else if (t2 > m) {
                stack = checkSpace(stack, count);
                stack[count++] = pt;
                t2 = m;
            }
        }
        mp.lineTo(rdy);
    }
