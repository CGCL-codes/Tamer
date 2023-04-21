    public double[] extractFeature(double[] samples, double sampling_rate, double[][] other_feature_values) throws Exception {
        double ret[] = new double[10];
        double wk1[] = new double[samples.length];
        double wk2[] = new double[samples.length];
        double wkm[] = new double[num_dimensions];
        wk1[0] = samples[0];
        wk2[samples.length - 2] = samples[samples.length - 1];
        for (int i = 1; i < samples.length - 1; ++i) {
            wk1[i] = samples[i];
            wk2[i - 1] = samples[i];
        }
        for (int i = 0; i < num_dimensions; ++i) {
            double num = 0.0;
            double denom = 0.0;
            for (int j = 0; j < (samples.length - i); ++j) {
                num += wk1[j] * wk2[j];
                denom += wk1[j] * wk1[j] + wk2[j] * wk2[j];
            }
            ret[i] = 2.0 * num / denom;
            for (int j = 0; j < i; ++j) {
                ret[j] = wkm[j] - ret[i] * wkm[i - j];
            }
            for (int j = 0; j <= i; ++j) {
                wkm[j] = ret[j];
            }
            for (int j = 0; j < (samples.length - i - 1); ++j) {
                wk1[j] -= wkm[i] * wk2[j];
                wk2[j] = wk2[j + 1] - wkm[i] * wk1[j + 1];
            }
        }
        return ret;
    }
