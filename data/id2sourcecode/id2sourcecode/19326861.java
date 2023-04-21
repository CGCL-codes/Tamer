    @Override
    public double[] classDistribution(ICInstance instance) throws ICParameterException {
        double[] classDistrib = new double[_attList.get(_classIndex).getAttCardinality()];
        double minLogProb = 0;
        double maxLogProb = 0;
        int classIndex = instance.getClassIndexes().get(0);
        for (int c = 0; c < classDistrib.length; c++) {
            ICInstance i = new ICInstance(instance);
            i.setAttValue(classIndex, c);
            classDistrib[c] = getLogProbability(i);
            if (c == 0 || classDistrib[c] > maxLogProb) maxLogProb = classDistrib[c];
            if (c == 0 || classDistrib[c] < minLogProb) minLogProb = classDistrib[c];
        }
        double cte = (maxLogProb + minLogProb) / 2;
        double sum = 0;
        for (int i = 0; i < classDistrib.length; i++) {
            classDistrib[i] -= cte;
            classDistrib[i] = Math.pow(10, classDistrib[i]);
            sum += classDistrib[i];
        }
        for (int i = 0; i < classDistrib.length; i++) classDistrib[i] /= sum;
        return classDistrib;
    }
