    public static double getIsoelectricPoint(JPLIAASequence sequence) {
        double epsilon = 0.001;
        int iterationMax = 10000;
        int counter = 0;
        double pHs = -2;
        double pHe = 16;
        double pHm = 0;
        while (counter < iterationMax && Math.abs(pHs - pHe) >= epsilon) {
            pHm = (pHs + pHe) / 2;
            if (log.isDebugEnabled()) {
                log.debug("[" + pHs + ", " + pHm + "]");
            }
            double pcs = getPartialCharges(sequence, pHs);
            double pcm = getPartialCharges(sequence, pHm);
            if (pcs < 0) {
                System.err.println("at pH " + pHs + ", partial charge is " + pcs);
                return pHs;
            }
            if ((pcs < 0 && pcm > 0) || (pcs > 0 && pcm < 0)) {
                pHe = pHm;
            } else {
                pHs = pHm;
            }
            counter++;
        }
        if (log.isDebugEnabled()) {
            log.debug("[" + pHs + "," + pHe + "], iteration = " + counter);
        }
        return (pHs + pHe) / 2;
    }
