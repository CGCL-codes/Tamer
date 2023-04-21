    private void locatePolesAndZeros() {
        pReal = new double[order + 1];
        pImag = new double[order + 1];
        z = new double[order + 1];
        double ln10 = Math.log(10.0);
        for (int k = 1; k <= order; k++) {
            pReal[k] = 0.0;
            pImag[k] = 0.0;
        }
        int n = order;
        if (filterType == FilterType.BP) n = n / 2;
        int ir = n % 2;
        int n1 = n + ir;
        int n2 = (3 * n + ir) / 2 - 1;
        double f1;
        switch(filterType) {
            case LP:
                f1 = fp2;
                break;
            case HP:
                f1 = fN - fp1;
                break;
            case BP:
                f1 = fp2 - fp1;
                break;
            default:
                f1 = 0.0;
        }
        double tanw1 = Math.tan(0.5 * Math.PI * f1 / fN);
        double tansqw1 = sqr(tanw1);
        double t, a = 1.0, r = 1.0, i = 1.0;
        for (int k = n1; k <= n2; k++) {
            t = 0.5 * (2 * k + 1 - ir) * Math.PI / (double) n;
            switch(prototype) {
                case BUTTERWORTH:
                    double b3 = 1.0 - 2.0 * tanw1 * Math.cos(t) + tansqw1;
                    r = (1.0 - tansqw1) / b3;
                    i = 2.0 * tanw1 * Math.sin(t) / b3;
                    break;
                case CHEBYSHEV:
                    double d = 1.0 - Math.exp(-0.05 * ripple * ln10);
                    double e = 1.0 / Math.sqrt(1.0 / sqr(1.0 - d) - 1.0);
                    double x = Math.pow(Math.sqrt(e * e + 1.0) + e, 1.0 / (double) n);
                    a = 0.5 * (x - 1.0 / x);
                    double b = 0.5 * (x + 1.0 / x);
                    double c3 = a * tanw1 * Math.cos(t);
                    double c4 = b * tanw1 * Math.sin(t);
                    double c5 = sqr(1.0 - c3) + sqr(c4);
                    r = 2.0 * (1.0 - c3) / c5 - 1.0;
                    i = 2.0 * c4 / c5;
                    break;
            }
            int m = 2 * (n2 - k) + 1;
            pReal[m + ir] = r;
            pImag[m + ir] = Math.abs(i);
            pReal[m + ir + 1] = r;
            pImag[m + ir + 1] = -Math.abs(i);
        }
        if (isOdd(n)) {
            if (prototype == Prototype.BUTTERWORTH) r = (1.0 - tansqw1) / (1.0 + 2.0 * tanw1 + tansqw1);
            if (prototype == Prototype.CHEBYSHEV) r = 2.0 / (1.0 + a * tanw1) - 1.0;
            pReal[1] = r;
            pImag[1] = 0.0;
        }
        switch(filterType) {
            case LP:
                for (int m = 1; m <= n; m++) z[m] = -1.0;
                break;
            case HP:
                for (int m = 1; m <= n; m++) {
                    pReal[m] = -pReal[m];
                    z[m] = 1.0;
                }
                break;
            case BP:
                for (int m = 1; m <= n; m++) {
                    z[m] = 1.0;
                    z[m + n] = -1.0;
                }
                double f4 = 0.5 * Math.PI * fp1 / fN;
                double f5 = 0.5 * Math.PI * fp2 / fN;
                double aa = Math.cos(f4 + f5) / Math.cos(f5 - f4);
                double aR, aI, h1, h2, p1R, p2R, p1I, p2I;
                for (int m1 = 0; m1 <= (order - 1) / 2; m1++) {
                    int m = 1 + 2 * m1;
                    aR = pReal[m];
                    aI = pImag[m];
                    if (Math.abs(aI) < 0.0001) {
                        h1 = 0.5 * aa * (1.0 + aR);
                        h2 = sqr(h1) - aR;
                        if (h2 > 0.0) {
                            p1R = h1 + Math.sqrt(h2);
                            p2R = h1 - Math.sqrt(h2);
                            p1I = 0.0;
                            p2I = 0.0;
                        } else {
                            p1R = h1;
                            p2R = h1;
                            p1I = Math.sqrt(Math.abs(h2));
                            p2I = -p1I;
                        }
                    } else {
                        double fR = aa * 0.5 * (1.0 + aR);
                        double fI = aa * 0.5 * aI;
                        double gR = sqr(fR) - sqr(fI) - aR;
                        double gI = 2 * fR * fI - aI;
                        double sR = Math.sqrt(0.5 * Math.abs(gR + Math.sqrt(sqr(gR) + sqr(gI))));
                        double sI = gI / (2.0 * sR);
                        p1R = fR + sR;
                        p1I = fI + sI;
                        p2R = fR - sR;
                        p2I = fI - sI;
                    }
                    pReal[m] = p1R;
                    pReal[m + 1] = p2R;
                    pImag[m] = p1I;
                    pImag[m + 1] = p2I;
                }
                if (isOdd(n)) {
                    pReal[2] = pReal[n + 1];
                    pImag[2] = pImag[n + 1];
                }
                for (int k = n; k >= 1; k--) {
                    int m = 2 * k - 1;
                    pReal[m] = pReal[k];
                    pReal[m + 1] = pReal[k];
                    pImag[m] = Math.abs(pImag[k]);
                    pImag[m + 1] = -Math.abs(pImag[k]);
                }
                break;
            default:
        }
    }
