    protected static void zRoots(float[] coeffBuf, int coeffNum, float[] roots, boolean polish) {
        int i, j, jj;
        float bRe, bIm, cRe, cIm;
        int coeffNum2 = coeffNum << 1;
        float[] ad = new float[coeffNum2 + 2];
        float[] x = new float[2];
        System.arraycopy(coeffBuf, 0, ad, 0, coeffNum2 + 2);
        for (j = coeffNum2; j >= 2; j -= 2) {
            jj = j - 2;
            x[0] = 0.0f;
            x[1] = 0.0f;
            i = laguerre(ad, j >> 1, x);
            if (Math.abs(x[1]) <= EXPECTEDERROR2 * Math.abs(x[0])) {
                x[1] = 0.0f;
            }
            roots[jj] = x[0];
            roots[jj + 1] = x[1];
            bRe = ad[j];
            bIm = ad[j + 1];
            for (; jj >= 0; jj -= 2) {
                cRe = ad[jj];
                cIm = ad[jj + 1];
                ad[jj] = bRe;
                ad[jj + 1] = bIm;
                bRe = x[0] * bRe - x[1] * bIm + cRe;
                bIm = x[1] * bRe + x[0] * bIm + cIm;
            }
        }
        if (polish) {
            for (j = 0; j < coeffNum2; ) {
                x[0] = roots[j];
                x[1] = roots[j + 1];
                laguerre(coeffBuf, coeffNum, x);
                roots[j++] = x[0];
                roots[j++] = x[1];
            }
        }
        for (j = 2; j < coeffNum2; j += 2) {
            x[0] = roots[j];
            x[1] = roots[j + 1];
            for (i = j - 2; i >= 2; i -= 2) {
                if (roots[i] <= x[0]) break;
                roots[i + 2] = roots[i];
                roots[i + 3] = roots[i + 1];
            }
            roots[i + 2] = x[0];
            roots[i + 3] = x[1];
        }
    }
