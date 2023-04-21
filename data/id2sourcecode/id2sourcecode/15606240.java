    protected double lineSearch() {
        previousStepSize = 0;
        double magDescDir = l2Norm(descentDirection);
        if (magDescDir < 0.0001) {
            return 0;
        }
        double magLo = l2Norm(negativeGradient);
        step();
        getNegativeGradient();
        double magHi = l2Norm(negativeGradient);
        double m = magDescDir * magHi;
        double cos = dotProduct(negativeGradient, descentDirection) / m;
        double lo = 0, hi = Double.MAX_VALUE;
        int i = 0;
        while (((cos < 0) || (cos > maxCos)) && (hi - lo > 0.00000001)) {
            if (cos < 0) {
                hi = stepSize;
                stepSize = (lo + hi) / 2;
            } else {
                if (hi < Double.MAX_VALUE) {
                    lo = stepSize;
                    stepSize = (lo + hi) / 2;
                } else {
                    lo = stepSize;
                    stepSize *= 2;
                }
            }
            step();
            getNegativeGradient();
            m = magDescDir * l2Norm(negativeGradient);
            cos = dotProduct(negativeGradient, descentDirection) / m;
        }
        return l2Norm(negativeGradient);
    }
