    void prepareTorusColixes(Sasurface1.Torus torus, short[] convexColixes, Atom[] atoms) {
        int ixA = torus.ixA;
        int ixB = torus.ixB;
        int outerPointCount = torus.outerPointCount;
        short colixA = Graphics3D.inheritColix(torus.colixA, convexColixes[ixA], atoms[ixA].colixAtom);
        short colixB = Graphics3D.inheritColix(torus.colixB, convexColixes[ixB], atoms[ixB].colixAtom);
        if (colixA == colixB) {
            for (int i = outerPointCount; --i >= 0; ) torusColixes[i] = colixA;
            return;
        }
        if (colixA < 0 && colixB < 0) {
            short unmaskedA = Graphics3D.getChangableColixIndex(colixA);
            short unmaskedB = Graphics3D.getChangableColixIndex(colixB);
            if (unmaskedA >= JmolConstants.FORMAL_CHARGE_COLIX_RED && unmaskedA <= JmolConstants.FORMAL_CHARGE_COLIX_BLUE && unmaskedB >= JmolConstants.FORMAL_CHARGE_COLIX_RED && unmaskedB <= JmolConstants.FORMAL_CHARGE_COLIX_BLUE) {
                prepareFormalChargeTorusColixes(colixA, unmaskedA, colixB, unmaskedB, outerPointCount);
                return;
            }
        }
        int halfRoundedUp = (outerPointCount + 1) / 2;
        torusColixes[outerPointCount / 2] = colixA;
        for (int i = outerPointCount / 2; --i >= 0; ) {
            torusColixes[i] = colixA;
            torusColixes[i + halfRoundedUp] = colixB;
        }
    }
