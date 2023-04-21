    public int[] getStateSequence(double[][] ob) {
        int l = ob.length;
        int stateCount = stProb.length;
        int[][] top = new int[l][stateCount];
        double[][] d = new double[l][stateCount];
        for (int i = 0; i < stateCount; i++) {
            d[0][i] = stProb[i] * getDensity(i, ob[0]);
        }
        for (int t = 1; t < l; t++) {
            for (int i = 0; i < stateCount; i++) {
                double max = -Double.MAX_VALUE;
                int top_state = -1;
                for (int j = 0; j < stateCount; j++) {
                    double v = d[t - 1][j] * transProb[j][i];
                    if (v > max) {
                        max = v;
                        top_state = j;
                    }
                }
                d[t][i] = max * getDensity(i, ob[t]);
                top[t][i] = top_state;
            }
        }
        int[] seq = new int[l];
        double max = -Double.MAX_VALUE;
        int maxi = -1;
        for (int i = 0; i < stateCount; i++) {
            double v = d[l - 1][i];
            if (v > max) {
                max = v;
                maxi = i;
            }
        }
        seq[l - 1] = maxi;
        for (int t = l - 2; t >= 0; t--) {
            seq[t] = top[t + 1][seq[t + 1]];
        }
        return seq;
    }
