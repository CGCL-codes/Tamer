    protected static float lpCoeffs(float[] data, int dataOff, int dataLen, float[] coeffBuf, int coeffNum) {
        int i, j, k;
        float f;
        float[] wk1 = new float[dataLen - 1];
        float[] wk2 = new float[dataLen - 1];
        float[] wkm = new float[coeffNum - 1];
        float xms, num, denom;
        for (j = dataLen, i = dataLen + dataOff, f = 0.0f; j < i; j++) {
            f += data[j] * data[j];
        }
        xms = f / dataLen;
        wk1[0] = data[0];
        wk2[dataLen - 2] = data[dataLen - 1];
        System.arraycopy(data, dataOff, wk1, 0, dataLen - 1);
        System.arraycopy(data, dataOff + 1, wk2, 0, dataLen - 1);
        for (k = 0; ; ) {
            num = 0.0f;
            denom = 0.0f;
            for (j = 0, i = dataLen - k - 1; j < i; j++) {
                num += wk1[j] * wk2[j];
                denom += wk1[j] * wk1[j] + wk2[j] * wk2[j];
            }
            if (denom > 0.0f) {
                f = 2.0f * num / denom;
            } else {
                f = 1.0f;
            }
            coeffBuf[k] = f;
            xms *= 1.0f - f * f;
            for (i = 0; i < k; i++) {
                coeffBuf[i] = wkm[i] - f * wkm[k - i - 1];
            }
            if (++k == coeffNum) return xms;
            System.arraycopy(coeffBuf, 0, wkm, 0, k);
            for (j = 0, i = dataLen - k - 1; j < i; j++) {
                wk1[j] -= f * wk2[j];
                wk2[j] = wk2[j + 1] - f * wk1[j + 1];
            }
        }
    }
