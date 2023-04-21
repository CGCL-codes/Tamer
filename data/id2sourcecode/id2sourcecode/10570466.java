    private double[] performCalculation() {
        if (considerHillCoefficient && !hillCoefficientSet) {
            final int MINIMUM_HILL_COEFFICIENT = 0;
            final int MAXIMUM_HILL_COEFFICIENT = 10;
            final double PHI = (1 + Math.sqrt(2)) / 2;
            final double RESPHI = 2 - PHI;
            double a = MINIMUM_HILL_COEFFICIENT;
            double b = hillCoefficient;
            double c = MAXIMUM_HILL_COEFFICIENT;
            double fb = hillFit(MathUtils.mapPow(nonControlSubstrateConcentrations, b), nonControlInitialRates)[RESIDUES];
            boolean finished = false;
            while (!finished) {
                double x;
                if (c - b > b - a) {
                    x = b + RESPHI * (c - b);
                } else {
                    x = b - RESPHI * (b - a);
                }
                if (Math.abs(c - a) < TAU * (Math.abs(b) + Math.abs(x))) {
                    hillCoefficient = (c + a) / 2;
                    finished = true;
                    break;
                }
                double fx = hillFit(MathUtils.mapPow(nonControlSubstrateConcentrations, x), nonControlInitialRates)[RESIDUES];
                double aNew;
                double bNew;
                double cNew;
                if (fx < fb) {
                    bNew = x;
                    fb = fx;
                    if (c - b > b - a) {
                        aNew = b;
                        cNew = c;
                    } else {
                        aNew = a;
                        cNew = b;
                    }
                } else {
                    bNew = b;
                    if (c - b > b - a) {
                        aNew = a;
                        cNew = x;
                    } else {
                        aNew = x;
                        cNew = c;
                    }
                }
                a = aNew;
                b = bNew;
                c = cNew;
            }
        }
        return performFit();
    }
