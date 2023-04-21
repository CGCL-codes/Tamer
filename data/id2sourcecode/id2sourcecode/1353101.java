    public static float[][] resample_curve(float[][] curve, float increment) {
        int len = curve[0].length;
        float[] seg_length = new float[len - 1];
        float curve_length = curveLength(curve, seg_length);
        int npoints = 1 + (int) (curve_length / increment);
        float delta = curve_length / (npoints - 1);
        float[][] newcurve = new float[2][npoints];
        newcurve[0][0] = curve[0][0];
        newcurve[1][0] = curve[1][0];
        if (npoints < 2) return newcurve;
        int k = 0;
        float old_seg = seg_length[k];
        for (int i = 1; i < npoints - 1; i++) {
            float new_seg = delta;
            while (true) {
                if (old_seg < new_seg) {
                    new_seg -= old_seg;
                    k++;
                    if (k > len - 2) {
                        throw new VisADError("k = " + k + " i = " + i);
                    }
                    old_seg = seg_length[k];
                } else {
                    old_seg -= new_seg;
                    float a = old_seg / seg_length[k];
                    newcurve[0][i] = a * curve[0][k] + (1.0f - a) * curve[0][k + 1];
                    newcurve[1][i] = a * curve[1][k] + (1.0f - a) * curve[1][k + 1];
                    break;
                }
            }
        }
        newcurve[0][npoints - 1] = curve[0][len - 1];
        newcurve[1][npoints - 1] = curve[1][len - 1];
        return newcurve;
    }
