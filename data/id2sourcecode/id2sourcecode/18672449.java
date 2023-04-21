    private void initialise(double[][] cdata) {
        this.nCurves = cdata.length / 2;
        this.nPoints = new int[nCurves];
        this.lineOpt = new int[nCurves];
        this.dashLength = new int[nCurves];
        this.trimOpt = new boolean[nCurves];
        this.minMaxOpt = new boolean[nCurves];
        this.pointOpt = new int[nCurves];
        this.pointSize = new int[nCurves];
        this.errorBar = new boolean[nCurves];
        this.nmPoints = 0;
        int ll = 0;
        for (int i = 0; i < 2 * nCurves; i++) {
            if ((ll = cdata[i].length) > nmPoints) nmPoints = ll;
        }
        this.data = new double[2 * nCurves][nmPoints];
        this.copy = new double[2 * nCurves][nmPoints];
        this.errors = new double[nCurves][nmPoints];
        this.errorsCopy = new double[nCurves][nmPoints];
        int k = 0, l1 = 0, l2 = 0;
        boolean testlen = true;
        for (int i = 0; i < nCurves; i++) {
            k = 2 * i;
            testlen = true;
            l1 = cdata[k].length;
            l2 = cdata[k + 1].length;
            if (l1 != l2) throw new IllegalArgumentException("an x and y array length differ");
            nPoints[i] = l1;
        }
        k = 0;
        boolean testopt = true;
        for (int i = 0; i < nCurves; i++) {
            testlen = true;
            l1 = nPoints[i];
            while (testlen) {
                if (l1 < 0) throw new IllegalArgumentException("curve array index  " + k + ": blank array");
                if (cdata[k][l1 - 1] == dataFill) {
                    if (cdata[k + 1][l1 - 1] == dataFill) {
                        l1--;
                        testopt = false;
                    } else {
                        testlen = false;
                    }
                } else {
                    testlen = false;
                }
            }
            nPoints[i] = l1;
            k += 2;
        }
        k = 0;
        for (int i = 0; i < nCurves; i++) {
            double[][] xxx = new double[2][nPoints[i]];
            for (int j = 0; j < nPoints[i]; j++) {
                xxx[0][j] = cdata[k][j];
                xxx[1][j] = cdata[k + 1][j];
            }
            xxx = doubleSelectionSort(xxx);
            for (int j = 0; j < nPoints[i]; j++) {
                cdata[k][j] = xxx[0][j];
                cdata[k + 1][j] = xxx[1][j];
            }
            k += 2;
        }
        k = 0;
        int kk = 1;
        for (int i = 0; i < nCurves; i++) {
            int rev = 1;
            for (int j = 1; j < nPoints[i]; j++) {
                if (cdata[k][j] < cdata[k][j - 1]) rev++;
            }
            if (rev == nPoints[i]) {
                double[] hold = new double[nPoints[i]];
                for (int j = 0; j < nPoints[i]; j++) hold[j] = cdata[k][j];
                for (int j = 0; j < nPoints[i]; j++) cdata[k][j] = hold[nPoints[i] - j - 1];
                for (int j = 0; j < nPoints[i]; j++) hold[j] = cdata[k + 1][j];
                for (int j = 0; j < nPoints[i]; j++) cdata[k + 1][j] = hold[nPoints[i] - j - 1];
            }
            for (int j = 0; j < nPoints[i]; j++) {
                this.data[k][j] = cdata[k][j];
                this.data[k + 1][j] = cdata[k + 1][j];
                this.copy[k][j] = cdata[k][j];
                this.copy[k + 1][j] = cdata[k + 1][j];
            }
            this.lineOpt[i] = 1;
            this.dashLength[i] = 5;
            this.trimOpt[i] = false;
            if (this.lineOpt[i] == 1) trimOpt[i] = true;
            this.minMaxOpt[i] = true;
            this.pointSize[i] = 6;
            this.errorBar[i] = false;
            this.pointOpt[i] = kk;
            k += 2;
            kk++;
            if (kk > npTypes) kk = 1;
        }
    }
