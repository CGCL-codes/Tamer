    public void sort(final int[] dataEvent) {
        final int energy = dataEvent[idE];
        final int eDE = dataEvent[idDE];
        final int ecE = energy >> 3;
        final int ecDE = eDE >> 3;
        final int sum = (energy + eDE) / 2;
        hEnergy.inc(energy);
        hDE.inc(eDE);
        hSum.inc(sum);
        hEvsDE.inc(ecE, ecDE);
        if (gEvsDE.inGate(ecE, ecDE)) {
            hSumGate.inc(sum);
        }
    }
