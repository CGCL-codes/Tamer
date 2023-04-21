    public double TforY(double y) {
        if (y == y0) return 0;
        if (y == y1) return 1;
        if (y == YforT1) return TforY1;
        if (y == YforT2) return TforY2;
        if (y == YforT3) return TforY3;
        if (y < y0 || y > y1) {
            throw new InternalError("bad y (" + y + ", " + y0 + "=>" + y1 + ")");
        }
        double a = ycoeff2;
        double b = ycoeff1;
        double c = ycoeff0 - y;
        if (ycoeff3 == 0.0) {
            double[] eqn = { c, b, a };
            int numroots = QuadCurve2D.solveQuadratic(eqn, eqn);
            return firstValidRoot(eqn, numroots);
        }
        a /= ycoeff3;
        b /= ycoeff3;
        c /= ycoeff3;
        int roots = 0;
        double Q = (a * a - 3.0 * b) / 9.0;
        double R = (2.0 * a * a * a - 9.0 * a * b + 27.0 * c) / 54.0;
        double R2 = R * R;
        double Q3 = Q * Q * Q;
        double a_3 = a / 3.0;
        double t;
        if (R2 < Q3) {
            double theta = Math.acos(R / Math.sqrt(Q3));
            Q = -2.0 * Math.sqrt(Q);
            t = refine(a, b, c, y, Q * Math.cos(theta / 3.0) - a_3);
            if (t < 0) {
                t = refine(a, b, c, y, Q * Math.cos((theta + Math.PI * 2.0) / 3.0) - a_3);
            }
            if (t < 0) {
                t = refine(a, b, c, y, Q * Math.cos((theta - Math.PI * 2.0) / 3.0) - a_3);
            }
        } else {
            boolean neg = (R < 0.0);
            double S = Math.sqrt(R2 - Q3);
            if (neg) {
                R = -R;
            }
            double A = Math.pow(R + S, 1.0 / 3.0);
            if (!neg) {
                A = -A;
            }
            double B = (A == 0.0) ? 0.0 : (Q / A);
            t = refine(a, b, c, y, (A + B) - a_3);
        }
        if (t < 0) {
            double t0 = 0;
            double t1 = 1;
            while (true) {
                t = (t0 + t1) / 2;
                if (t == t0 || t == t1) {
                    break;
                }
                double yt = YforT(t);
                if (yt < y) {
                    t0 = t;
                } else if (yt > y) {
                    t1 = t;
                } else {
                    break;
                }
            }
        }
        if (t >= 0) {
            TforY3 = TforY2;
            YforT3 = YforT2;
            TforY2 = TforY1;
            YforT2 = YforT1;
            TforY1 = t;
            YforT1 = y;
        }
        return t;
    }
