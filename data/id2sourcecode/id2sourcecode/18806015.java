    public double calcMassAlignmentError() {
        int totNrPeaks = 0;
        for (PeakList spectrum : spectra) {
            totNrPeaks += spectrum.size();
        }
        double[] masses = new double[totNrPeaks];
        double[] mzDiffSingle = new double[totNrPeaks];
        double[] mzDiffComb = new double[totNrPeaks];
        int pos = 0;
        int j = 0;
        for (PeakList spectrum : spectra) {
            double[] mz = ((PeakListImpl) spectrum).getMzs(masses, pos);
            pos += spectrum.size();
            for (int i = 1; i < mz.length; i++) {
                mzDiffSingle[j] = mz[i] - mz[i - 1];
                j++;
            }
        }
        for (; j < totNrPeaks; j++) mzDiffSingle[j] = 10000.0;
        Arrays.sort(mzDiffSingle);
        Arrays.sort(masses);
        double lambda = 0;
        int cnt = 0;
        for (int i = 0; i < masses.length - 1; i++) {
            mzDiffComb[i] = masses[i + 1] - masses[i];
            if (mzDiffComb[i] < specAlignMZTol) {
                lambda += mzDiffComb[i];
                cnt++;
            }
        }
        lambda = lambda / cnt;
        if (cnt < 10) {
            throw new IllegalStateException("Not enough matching peaks between spectra!");
        }
        double th = -lambda * Math.log(0.01);
        cnt = 0;
        for (int i = 0; mzDiffSingle[i] < th; i++) cnt++;
        if (cnt / spectra.size() > 5) {
            throw new IllegalStateException("Peaks within same spectrum are too close!");
        }
        return th;
    }
