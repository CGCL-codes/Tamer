    public static double getLogEmissionProbability(Exon exon, Exon expected, Exon stdDev, State s, int nSamples, Gamma gamma, double gammaK, double gammaTheta, double gammaSNPsK, double gammaSNPsTheta) {
        double observedSNPs = exon.SNPs;
        double expectedSNPs = expected.SNPs;
        double observedFPKM = exon.FPKM;
        double expectedFPKM = expected.FPKM;
        double a = .05, b = 0;
        double lambdaSNPs = (s.snpRatio * nSamples * expectedSNPs + a * exon.length()) / (nSamples + b);
        double logProbSNPs = logPoisson(observedSNPs, lambdaSNPs);
        a = 100;
        b = 2;
        double lambdaFPKM = (s.rpkmRatio * nSamples * expectedFPKM + a * exon.length()) / (nSamples + b);
        double logProbFPKM = logPoisson(observedFPKM, lambdaFPKM);
        if (lambdaFPKM == 0.0) if (observedFPKM == 0.0) logProbFPKM = 0; else logProbFPKM = Double.NEGATIVE_INFINITY;
        if (lambdaSNPs == 0.0) if (observedSNPs == 0.0) logProbSNPs = 0; else logProbSNPs = Double.NEGATIVE_INFINITY;
        double newGammaK = updateGammaK(s.rpkmRatio, nSamples, gammaK, expectedFPKM);
        double newGammaTheta = updateGammaTheta(nSamples, gammaTheta);
        gamma = new Gamma(newGammaK, newGammaTheta, null);
        logProbFPKM = Double.NEGATIVE_INFINITY;
        jsc.distributions.Gamma gammaDist = new jsc.distributions.Gamma(newGammaK, newGammaTheta);
        double prevx = 0;
        for (double p = 0.1; p <= 0.9; p += 0.1) {
            double x = gammaDist.inverseCdf(p);
            if (prevx == 0) prevx = x;
            x = (x + prevx) / 2;
            logProbFPKM = Probability.logSum(logProbFPKM, logPoisson(observedFPKM, x) + logGamma(x, newGammaK, newGammaTheta));
            prevx = x;
        }
        if (observedFPKM == 0.0) if (s.rpkmRatio * expectedFPKM == 0) logProbFPKM = 0;
        return (logProbFPKM + logProbSNPs);
    }
