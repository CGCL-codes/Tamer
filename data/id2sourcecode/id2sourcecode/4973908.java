    public double[] getModelWeights() throws OperatorException {
        if (this.getLabel().getMapping().size() != 2) throw new UserError(null, 114, "BayBoostModel", this.getLabel());
        int maxWeight = 10;
        final int pos = this.getLabel().getMapping().getPositiveIndex();
        final int neg = this.getLabel().getMapping().getNegativeIndex();
        double[] weights = new double[this.getNumberOfModels() + 1];
        double odds = this.getPriorOfClass(pos) / this.getPriorOfClass(neg);
        weights[0] = Math.log(odds);
        for (int i = 1; i < weights.length; i++) {
            double logPosRatio, logNegRatio;
            {
                double liftRatiosPos[] = this.getFactorsForModel(i - 1, pos);
                logPosRatio = Math.log(liftRatiosPos[pos]);
                logPosRatio = Math.min(maxWeight, Math.max(-maxWeight, logPosRatio));
                double liftRatiosNeg[] = this.getFactorsForModel(i - 1, neg);
                logNegRatio = Math.log(liftRatiosNeg[pos]);
                logNegRatio = Math.min(maxWeight, Math.max(-maxWeight, logNegRatio));
            }
            double indep = (logPosRatio + logNegRatio) / 2;
            if (Tools.isEqual(indep, maxWeight) || Tools.isEqual(indep, -maxWeight)) {
                logPosRatio = 10 * indep;
                indep = 0;
            }
            weights[0] += indep;
            logPosRatio -= indep;
            weights[i] = logPosRatio;
        }
        return weights;
    }
