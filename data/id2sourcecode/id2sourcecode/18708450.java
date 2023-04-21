    public static void transform(float[] v, int last) {
        float[] ans = new float[last];
        int half = (last + FilterType) / 2;
        if (2 * half - FilterType != last) throw new IllegalArgumentException("Illegal subband : " + last + " within array of length " + v.length);
        for (int k = 0; k < scaleLeft.length; k++) {
            for (int l = 0; l < scaleLeft[k].length; l++) {
                ans[k] += scaleLeft[k][l] * v[l];
            }
        }
        for (int k = scaleLeft.length; k < half - scaleRight.length; k++) {
            for (int l = 0; l < scale.length; l++) {
                ans[k] += scale[l] * v[2 * k + l - scaleLeft.length];
            }
        }
        for (int k = 0; k < scaleRight.length; k++) {
            for (int l = 0; l < scaleRight[k].length; l++) {
                ans[k + half - scaleRight.length] += scaleRight[k][l] * v[v.length - scaleRight[k].length + l];
            }
        }
        for (int k = 0; k < half - FilterType; k++) {
            for (int l = 0; l < wavelet.length; l++) {
                ans[k + half] += wavelet[l] * v[2 * k + l];
            }
        }
        System.arraycopy(ans, 0, v, 0, last);
    }
