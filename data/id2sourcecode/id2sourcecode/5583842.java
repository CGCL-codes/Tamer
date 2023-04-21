    protected static double[] getTopResidual(double[] maCoef, double[] maseq) {
        int maLevel = maCoef.length - 1;
        if (maLevel > 0) {
            double[] q = new double[maLevel];
            for (int i = 0; i < maLevel; i++) {
                q[i] = maCoef[maLevel - i];
            }
            Matrix Q = new Matrix(q, 1);
            double[][] m = new double[maLevel][maLevel + 1];
            for (int i = 0; i < maLevel; i++) {
                m[i][i] = 1;
            }
            Matrix M = new Matrix(m);
            double[][] k = new double[maLevel][maLevel];
            for (int i = 0; i < k.length; i++) {
                k[i][i] = 1;
            }
            Matrix K = new Matrix(k);
            double[] b = new double[maLevel];
            Matrix B = new Matrix(b, b.length);
            for (int i = 0; i < maseq.length; i++) {
                double[] c = Q.times(-1).times(M).getRowPackedCopy();
                c[maLevel] += maseq[i];
                for (int j = 0; j < maLevel; j++) {
                    b[j] += c[j] * c[maLevel];
                    k[j][j] += c[j] * c[j];
                    for (int l = 0; l < maLevel; l++) {
                        if (l != j) {
                            k[j][l] += c[j] * c[l];
                        }
                    }
                }
                for (int j = 0; j < maLevel - 1; j++) {
                    m[j] = m[j + 1];
                }
                m[maLevel - 1] = c;
            }
            double[] x = null;
            try {
                x = K.inverse().times(B).getRowPackedCopy();
            } catch (Exception e) {
                log.warn(e);
                x = new double[maLevel];
            }
            double[] e = new double[maseq.length];
            for (int i = 0; i < e.length; i++) {
                e[i] = maseq[i];
                for (int j = 0; j < maLevel; j++) {
                    if (i - j - 1 >= 0) {
                        e[i] -= q[maLevel - j - 1] * e[i - j - 1];
                    } else {
                        e[i] -= q[maLevel - j - 1] * x[x.length + i - j - 1];
                    }
                }
            }
            return e;
        } else {
            return maseq;
        }
    }
