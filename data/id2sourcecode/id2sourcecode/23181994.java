    public static double getIsoelectricPoint(final Peptide peptide, AAPKaManager pkaManager) {
        final double epsilon = 0.001;
        final int iterationMax = 10000;
        int counter = 0;
        double pHs = 0;
        double pHe = 14;
        double pHm = 0;
        while ((counter < iterationMax) && (Math.abs(pHs - pHe) >= epsilon)) {
            pHm = (pHs + pHe) / 2;
            if (log.isDebugEnabled()) {
                log.debug("[" + pHs + ", " + pHm + "]");
            }
            final double ncs = getNetCharge(peptide, pHs, pkaManager);
            final double ncm = getNetCharge(peptide, pHm, pkaManager);
            if (ncs < 0) {
                return pHs;
            }
            if (((ncs < 0) && (ncm > 0)) || ((ncs > 0) && (ncm < 0))) {
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
