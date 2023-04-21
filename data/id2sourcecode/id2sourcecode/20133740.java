    void computeDropoffFactors() {
        aT = (MMTc - MMTf) / ((float) (LR1 - LR2));
        bT = (MMTf * LR1 - MMTc * LR2) / ((float) (LR1 - LR2));
        a = Math.PI / (LR1 - LR2);
        b = -Math.PI * LR2 / (LR1 - LR2);
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
    }
