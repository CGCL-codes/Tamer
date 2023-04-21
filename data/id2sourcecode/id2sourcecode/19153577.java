    void computeDropoffFactors() {
        aT = Math.PI / (LR1 - LR2);
        bT = -Math.PI * LR2 / (LR1 - LR2);
        cT = (MMTf - MMTc) / 2;
        eT = (MMTf + MMTc) / 2;
    }
