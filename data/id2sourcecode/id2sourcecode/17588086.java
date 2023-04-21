    private static double[][][] createStandardEllipsoid(int nr, int nu, int nv, double angleu1, double angleu2, double anglev1, double anglev2, boolean top, boolean bottom, boolean left, boolean right) {
        int totalN = nu * nv;
        if (Math.abs(anglev2 - anglev1) < 180) {
            if (bottom) {
                totalN += nr * nu;
            }
            if (top) {
                totalN += nr * nu;
            }
        }
        if (Math.abs(angleu2 - angleu1) < 360) {
            if (left) {
                totalN += nr * nv;
            }
            if (right) {
                totalN += nr * nv;
            }
        }
        double[][][] data = new double[totalN][4][3];
        double[] cosu = new double[nu + 1], sinu = new double[nu + 1];
        double[] cosv = new double[nv + 1], sinv = new double[nv + 1];
        for (int u = 0; u <= nu; u++) {
            double angle = ((nu - u) * angleu1 + u * angleu2) * TO_RADIANS / nu;
            cosu[u] = Math.cos(angle);
            sinu[u] = Math.sin(angle);
        }
        for (int v = 0; v <= nv; v++) {
            double angle = ((nv - v) * anglev1 + v * anglev2) * TO_RADIANS / nv;
            cosv[v] = Math.cos(angle) / 2;
            sinv[v] = Math.sin(angle) / 2;
        }
        int tile = 0;
        double[] center = new double[] { 0, 0, 0 };
        {
            for (int v = 0; v < nv; v++) {
                for (int u = 0; u < nu; u++, tile++) {
                    for (int k = 0; k < 3; k++) {
                        data[tile][0][k] = (cosu[u] * vectorx[k] + sinu[u] * vectory[k]) * cosv[v] + sinv[v] * vectorz[k];
                        data[tile][1][k] = (cosu[u + 1] * vectorx[k] + sinu[u + 1] * vectory[k]) * cosv[v] + sinv[v] * vectorz[k];
                        data[tile][2][k] = (cosu[u + 1] * vectorx[k] + sinu[u + 1] * vectory[k]) * cosv[v + 1] + sinv[v + 1] * vectorz[k];
                        data[tile][3][k] = (cosu[u] * vectorx[k] + sinu[u] * vectory[k]) * cosv[v + 1] + sinv[v + 1] * vectorz[k];
                    }
                }
            }
        }
        if (Math.abs(anglev2 - anglev1) < 180) {
            if (bottom) {
                center[2] = sinv[0];
                for (int u = 0; u < nu; u++) {
                    for (int i = 0; i < nr; i++, tile++) {
                        for (int k = 0; k < 3; k++) {
                            data[tile][0][k] = ((nr - i) * center[k] + i * data[u][0][k]) / nr;
                            data[tile][1][k] = ((nr - i - 1) * center[k] + (i + 1) * data[u][0][k]) / nr;
                            data[tile][2][k] = ((nr - i - 1) * center[k] + (i + 1) * data[u][1][k]) / nr;
                            data[tile][3][k] = ((nr - i) * center[k] + i * data[u][1][k]) / nr;
                        }
                    }
                }
            }
            if (top) {
                center[2] = sinv[nv];
                int ref = nu * (nv - 1);
                for (int u = 0; u < nu; u++) {
                    for (int i = 0; i < nr; i++, tile++) {
                        for (int k = 0; k < 3; k++) {
                            data[tile][0][k] = ((nr - i) * center[k] + i * data[ref + u][3][k]) / nr;
                            data[tile][1][k] = ((nr - i - 1) * center[k] + (i + 1) * data[ref + u][3][k]) / nr;
                            data[tile][2][k] = ((nr - i - 1) * center[k] + (i + 1) * data[ref + u][2][k]) / nr;
                            data[tile][3][k] = ((nr - i) * center[k] + i * data[ref + u][2][k]) / nr;
                        }
                    }
                }
            }
        }
        if (Math.abs(angleu2 - angleu1) < 360) {
            double[] nextCenter = new double[] { 0, 0, 0 };
            if (right) {
                int ref = 0;
                for (int j = 0; j < nv; j++, ref += nu) {
                    center[2] = sinv[j];
                    nextCenter[2] = sinv[j + 1];
                    for (int i = 0; i < nr; i++, tile++) {
                        for (int k = 0; k < 3; k++) {
                            data[tile][0][k] = ((nr - i) * center[k] + i * data[ref][0][k]) / nr;
                            data[tile][1][k] = ((nr - i - 1) * center[k] + (i + 1) * data[ref][0][k]) / nr;
                            data[tile][2][k] = ((nr - i - 1) * nextCenter[k] + (i + 1) * data[ref][3][k]) / nr;
                            data[tile][3][k] = ((nr - i) * nextCenter[k] + i * data[ref][3][k]) / nr;
                        }
                    }
                }
            }
            if (left) {
                int ref = nu - 1;
                for (int j = 0; j < nv; j++, ref += nu) {
                    center[2] = sinv[j];
                    nextCenter[2] = sinv[j + 1];
                    for (int i = 0; i < nr; i++, tile++) {
                        for (int k = 0; k < 3; k++) {
                            data[tile][0][k] = ((nr - i) * center[k] + i * data[ref][1][k]) / nr;
                            data[tile][1][k] = ((nr - i - 1) * center[k] + (i + 1) * data[ref][1][k]) / nr;
                            data[tile][2][k] = ((nr - i - 1) * nextCenter[k] + (i + 1) * data[ref][2][k]) / nr;
                            data[tile][3][k] = ((nr - i) * nextCenter[k] + i * data[ref][2][k]) / nr;
                        }
                    }
                }
            }
        }
        return data;
    }
