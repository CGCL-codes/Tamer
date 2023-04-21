    private double calcAlpha(Matrix x, Matrix dx, Matrix gradAtX) {
        double c1 = 0.0001;
        double c2 = 0.9;
        double dPhiAtZero = MathUtils.dotProduct(gradAtX.getColumnPackedCopy(), dx.getColumnPackedCopy());
        double phiAtZero = problem.getFunctionValAt(x.getColumnPackedCopy());
        double oldAlpha = 0;
        double newAlpha = 1;
        double maxAlpha = 2000;
        double incr = 1.1;
        double lowAlpha;
        double highAlpha;
        double phiAtLowAlpha;
        for (int i = 1; ; i++) {
            double phiAtOldAlpha = problem.getFunctionValAt(x.plus(dx.times(oldAlpha)).getColumnPackedCopy());
            double phiAtNewAlpha = problem.getFunctionValAt(x.plus(dx.times(newAlpha)).getColumnPackedCopy());
            if (phiAtNewAlpha > phiAtZero + c1 * newAlpha * dPhiAtZero || (i > 1 && phiAtNewAlpha >= phiAtOldAlpha)) {
                lowAlpha = oldAlpha;
                highAlpha = newAlpha;
                phiAtLowAlpha = phiAtOldAlpha;
                break;
            }
            double dPhiAtNewAlpha = MathUtils.dotProduct(problem.getGradientAt(x.plus(dx.times(newAlpha)).getColumnPackedCopy()), dx.getColumnPackedCopy());
            if (Math.abs(dPhiAtNewAlpha) <= -1.0 * c2 * dPhiAtZero) {
                return newAlpha;
            }
            if (dPhiAtNewAlpha >= 0) {
                lowAlpha = newAlpha;
                highAlpha = maxAlpha;
                phiAtLowAlpha = phiAtNewAlpha;
            }
            oldAlpha = newAlpha;
            newAlpha = oldAlpha * incr;
        }
        for (int i = 1; ; i++) {
            double currAlpha = (lowAlpha + highAlpha) / 2;
            double phiAtCurrAlpha = problem.getFunctionValAt(x.plus(dx.times(currAlpha)).getColumnPackedCopy());
            if (phiAtCurrAlpha > phiAtZero + c1 * currAlpha * dPhiAtZero || phiAtCurrAlpha >= phiAtLowAlpha) {
                highAlpha = currAlpha;
            } else {
                double dPhiAtCurrAlpha = MathUtils.dotProduct(problem.getGradientAt(x.plus(dx.times(currAlpha)).getColumnPackedCopy()), dx.getColumnPackedCopy());
                if (Math.abs(dPhiAtCurrAlpha) <= -1.0 * c2 * dPhiAtZero) {
                    return currAlpha;
                }
                if (dPhiAtCurrAlpha * (highAlpha - lowAlpha) < 0) {
                    highAlpha = lowAlpha;
                }
                lowAlpha = currAlpha;
            }
            if (i == 40) {
                return currAlpha;
            }
        }
    }
