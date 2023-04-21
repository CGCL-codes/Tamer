    public double density(double x) {
        final double n = degreesOfFreedom;
        final double nPlus1Over2 = (n + 1) / 2;
        return FastMath.exp(Gamma.logGamma(nPlus1Over2) - 0.5 * (FastMath.log(FastMath.PI) + FastMath.log(n)) - Gamma.logGamma(n / 2) - nPlus1Over2 * FastMath.log(1 + x * x / n));
    }
