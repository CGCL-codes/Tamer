    private double integrateRecursively(final Ops.DoubleOp f, final double a, final double b, final double tolerance) {
        final double halflength = (b - a) / 2;
        final double center = (a + b) / 2;
        double g7;
        double k15;
        double t, fsum;
        final double fc = f.op(center);
        g7 = fc * g7w[0];
        k15 = fc * k15w[0];
        int j, j2;
        for (j = 1, j2 = 2; j < 4; j++, j2 += 2) {
            t = halflength * k15t[j2];
            fsum = f.op(center - t) + f.op(center + t);
            g7 += fsum * g7w[j];
            k15 += fsum * k15w[j2];
        }
        for (j2 = 1; j2 < 8; j2 += 2) {
            t = halflength * k15t[j2];
            fsum = f.op(center - t) + f.op(center + t);
            k15 += fsum * k15w[j2];
        }
        g7 = halflength * g7;
        k15 = halflength * k15;
        increaseNumberOfEvaluations(15);
        if (Math.abs(k15 - g7) < tolerance) return k15; else {
            QL.require(numberOfEvaluations() + 30 <= maxEvaluations(), "maximum number of function evaluations exceeded");
            return integrateRecursively(f, a, center, tolerance / 2) + integrateRecursively(f, center, b, tolerance / 2);
        }
    }
