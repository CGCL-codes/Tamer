    protected static final void determineThresholdsRB(int i, int j, double[][] in0, double[][] inG, double[][] in2, double[] ave0, double[] Gave, double[] ave2, int[] ind) {
        for (int k = 0; k < ind.length; k++) {
            switch(ind[k]) {
                case 0:
                    ave0[0] = 1. / 2. * (in0[i][j] + in0[i - 2][j]);
                    Gave[0] = inG[i - 1][j];
                    ave2[0] = 1. / 2. * (in2[i - 1][j - 1] + in2[i - 1][j + 1]);
                    break;
                case 1:
                    ave0[1] = 1. / 2. * (in0[i][j] + in0[i][j + 2]);
                    Gave[1] = inG[i][j + 1];
                    ave2[1] = 1. / 2. * (in2[i - 1][j + 1] + in2[i + 1][j + 1]);
                    break;
                case 2:
                    ave0[2] = 1. / 2. * (in0[i][j] + in0[i + 2][j]);
                    Gave[2] = inG[i + 1][j];
                    ave2[2] = 1. / 2. * (in2[i + 1][j - 1] + in2[i + 1][j + 1]);
                    break;
                case 3:
                    ave0[3] = 1. / 2. * (in0[i][j] + in0[i][j - 2]);
                    Gave[3] = inG[i][j - 1];
                    ave2[3] = 1. / 2. * (in2[i - 1][j - 1] + in2[i + 1][j - 1]);
                    break;
                case 4:
                    ave0[4] = 1. / 2. * (in0[i][j] + in0[i - 2][j + 2]);
                    Gave[4] = 1. / 4. * (inG[i][j + 1] + inG[i - 1][j + 2] + inG[i - 1][j] + inG[i - 2][j + 1]);
                    ave2[4] = in2[i - 1][j + 1];
                    break;
                case 5:
                    ave0[5] = 1. / 2. * (in0[i][j] + in0[i + 2][j + 2]);
                    Gave[5] = 1. / 4. * (inG[i][j + 1] + inG[i + 1][j + 2] + inG[i + 1][j] + inG[i + 2][j + 1]);
                    ave2[5] = in2[i + 1][j + 1];
                    break;
                case 6:
                    ave0[6] = 1. / 2. * (in0[i][j] + in0[i - 2][j - 2]);
                    Gave[6] = 1. / 4. * (inG[i][j - 1] + inG[i - 1][j - 2] + inG[i - 1][j] + inG[i - 2][j - 1]);
                    ave2[6] = in2[i - 1][j - 1];
                    break;
                case 7:
                    ave0[7] = 1. / 2. * (in0[i][j] + in0[i + 2][j - 2]);
                    Gave[7] = 1. / 4. * (inG[i][j - 1] + inG[i + 1][j - 2] + inG[i + 1][j] + inG[i + 2][j - 1]);
                    ave2[7] = in2[i + 1][j - 1];
                    break;
            }
        }
    }
