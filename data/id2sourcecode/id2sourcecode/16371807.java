    @Override
    public double[] jointClassDistribution(ICInstance instance) throws ICParameterException {
        int numClasses = _classIndexes.length;
        int cardJointClass = 1;
        ArrayList<ICAttribute> attributes = instance.getAttList();
        for (int c = 0; c < numClasses; c++) cardJointClass *= attributes.get(_classIndexes[c]).getAttCardinality();
        double[] jointClassDistribution = new double[cardJointClass];
        double minLogProb = 0;
        double maxLogProb = 0;
        for (int cv = 0; cv < cardJointClass; cv++) {
            double[] values = ICDataUtils.decomposeJointIndex(_classIndexes, instance.getAttList(), cv);
            for (int c = 0; c < numClasses; c++) instance.setAttValue(_classIndexes[c], values[c]);
            jointClassDistribution[cv] = getLogProbability(instance);
            if (cv == 0 || jointClassDistribution[cv] > maxLogProb) maxLogProb = jointClassDistribution[cv];
            if (cv == 0 || jointClassDistribution[cv] < minLogProb) minLogProb = jointClassDistribution[cv];
        }
        double cte = (maxLogProb + minLogProb) / 2;
        double sum = 0;
        for (int cv = 0; cv < jointClassDistribution.length; cv++) {
            jointClassDistribution[cv] += cte;
            jointClassDistribution[cv] = Math.pow(10, jointClassDistribution[cv]);
            sum += jointClassDistribution[cv];
        }
        for (int cv = 0; cv < jointClassDistribution.length; cv++) jointClassDistribution[cv] /= sum;
        return jointClassDistribution;
    }
