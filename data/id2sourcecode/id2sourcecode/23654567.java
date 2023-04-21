    public int[] label(SparseVector[] x) {
        double[][][] scores = scores(x);
        double[][] gamma = new double[x.length][yAlphabet.size()];
        int[][] back = new int[x.length][yAlphabet.size()];
        for (int y = 0; y < yAlphabet.size(); y++) {
            gamma[0][y] = scores[0][0][y];
        }
        for (int t = 1; t < x.length; t++) {
            for (int yt = 0; yt < yAlphabet.size(); yt++) {
                gamma[t][yt] = Double.NEGATIVE_INFINITY;
                for (int ytm1 = 0; ytm1 < yAlphabet.size(); ytm1++) {
                    if (gamma[t][yt] < gamma[t - 1][ytm1] + scores[t][ytm1][yt]) {
                        back[t][yt] = ytm1;
                        gamma[t][yt] = gamma[t - 1][ytm1] + scores[t][ytm1][yt];
                    }
                }
            }
        }
        int[] tags = new int[x.length];
        for (int y = 0; y < yAlphabet.size(); y++) {
            if (gamma[x.length - 1][tags[x.length - 1]] < gamma[x.length - 1][y]) {
                tags[x.length - 1] = y;
            }
        }
        for (int t = x.length - 2; t >= 0; t--) {
            tags[t] = back[t + 1][tags[t + 1]];
        }
        return tags;
    }
