    private boolean wan_shr_adap(Vector<TaggedInstance> A, double EXTTRESH) {
        int samples = A.size();
        double[] CE = new double[samples];
        int MAXITER = 100;
        double NRWAN = 30;
        double[] ME1 = mean(A);
        double[] DMI = calculateDistances(A, ME1);
        double maxDMI = DMI[0];
        double minDMI = DMI[0];
        for (int i = 1; i < DMI.length; i++) {
            if (DMI[i] > maxDMI) maxDMI = DMI[i];
            if (DMI[i] < minDMI) minDMI = DMI[i];
        }
        EXTTRESH2 = maxDMI;
        double MDIS = minDMI;
        if (MathUtils.eq(MDIS, EXTTRESH2)) {
            ME = ME1;
            for (int i = 0; i < CE.length; i++) CE[i] = 1;
            EXTTRESH2 += 0.000001;
            System.out.println("Cluster center localisation did not reach preliminary estimate of radius!");
            return true;
        }
        double DELTARAD = (EXTTRESH2 - EXTTRESH) / NRWAN;
        double RADPR = EXTTRESH2;
        EXTTRESH2 = EXTTRESH2 - DELTARAD;
        if (EXTTRESH2 <= MDIS) {
            EXTTRESH2 = (RADPR + MDIS) / 2;
        }
        Vector<Integer> Q = findLower(DMI, EXTTRESH2);
        for (int i = 0; Q.size() != 0 && i < MAXITER; i++) {
            double[] ME2 = mean(select(A, Q));
            if (MathUtils.eq(ME1, ME2) && MathUtils.eq(RADPR, EXTTRESH2)) {
                ME = ME2;
                for (Integer index : Q) {
                    CE[index] = 1;
                }
                return true;
            }
            RADPR = EXTTRESH2;
            DMI = calculateDistances(A, ME2);
            if (EXTTRESH2 > EXTTRESH) {
                EXTTRESH2 = Math.max(EXTTRESH, EXTTRESH2 - DELTARAD);
                if (EXTTRESH2 < MathUtils.min(DMI)) {
                    EXTTRESH2 = RADPR;
                }
            }
            Q = findLower(DMI, EXTTRESH2);
            ME1 = ME2;
        }
        System.out.println("Preliminary cluster location did not converge");
        System.out.println("\t EXTTRESH2 = " + EXTTRESH2);
        return false;
    }
