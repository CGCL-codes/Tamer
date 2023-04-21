    public double optimize(double[] line, double initialStep) {
        assert (initialStep > 0);
        double[] parameters = new double[optimizable.getNumParameters()];
        double[] gradient = new double[optimizable.getNumParameters()];
        optimizable.getParameters(parameters);
        optimizable.getValueGradient(gradient);
        double ax, bx, cx, tx;
        double ay, by, cy, ty;
        double ag, bg, cg, tg;
        double ox;
        double origY;
        tx = ax = bx = cx = ox = 0;
        ty = ay = by = cy = origY = optimizable.getValue();
        tg = ag = bg = MatrixOps.dotProduct(gradient, line);
        if (ag <= 0) {
            throw new InvalidOptimizableException("The search direction \"line\" does not point down uphill.  " + "gradient.dotProduct(line)=" + ag + ", but should be positive");
        }
        int iterations = 0;
        do {
            if (iterations++ > maxIterations) throw new IllegalStateException("Exceeded maximum number allowed iterations searching for gradient cross-over.");
            ax = bx;
            ay = by;
            ag = bg;
            bx = tx;
            by = ty;
            bg = tg;
            if (tx == 0) {
                if (initialStep < 1.0) {
                    tx = initialStep;
                } else {
                    tx = 1.0;
                }
            } else {
                tx *= 3.0;
            }
            MatrixOps.plusEquals(parameters, line, tx - ox);
            optimizable.setParameters(parameters);
            ty = optimizable.getValue();
            optimizable.getValueGradient(gradient);
            tg = MatrixOps.dotProduct(gradient, line);
            ox = tx;
        } while (tg > 0);
        cx = tx;
        cy = ty;
        cg = tg;
        assert (!Double.isNaN(by));
        while (by <= ay || by <= cy || bx == ax) {
            if (iterations++ > maxIterations) throw new IllegalStateException("Exceeded maximum number allowed iterations searching for bracketed minimum, iteratation count = " + iterations);
            if ((Math.abs(bg) < 100 || Math.abs(ay - by) < 10 || Math.abs(by - cy) < 10) && bx != ax) break;
            assert (!Double.isNaN(bg));
            if (bg > 0) {
                assert (by >= ay);
                ax = bx;
                ay = by;
                ag = bg;
            } else {
                assert (by >= cy);
                cx = bx;
                cy = by;
                cg = bg;
            }
            bx = (ax + cx) / 2;
            MatrixOps.plusEquals(parameters, line, bx - ox);
            optimizable.setParameters(parameters);
            by = optimizable.getValue();
            assert (!Double.isNaN(by));
            optimizable.getValueGradient(gradient);
            bg = MatrixOps.dotProduct(gradient, line);
            ox = bx;
        }
        tx = ax + (((bx - ax) * (bx - ax) * (cy - ay) - (cx - ax) * (cx - ax) * (by - ay)) / (2.0 * ((bx - ax) * (cy - ay) - (cx - ax) * (by - ay))));
        MatrixOps.plusEquals(parameters, line, tx - ox);
        optimizable.setParameters(parameters);
        logger.info("Ending cost = " + optimizable.getValue());
        return Math.max(1, tx - initialStep);
    }
