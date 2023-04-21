    protected static final void determineThresholdsG(int i, int j, double[][] in0, double[][] inG, double[][] in2, double[] ave0, double[] Gave, double[] ave2, int[] ind) {
        for (int k = 0; k < ind.length; k++) {
            switch(ind[k]) {
                case 0:
                    Gave[0] = 1. / 2. * (inG[i][j] + inG[i - 2][j]);
                    ave2[0] = in2[i - 1][j];
                    ave0[0] = 1. / 4. * (in0[i - 2][j - 1] + in0[i - 2][j + 1] + in0[i][j - 1] + in0[i][j + 1]);
                    break;
                case 1:
                    Gave[1] = 1. / 2. * (inG[i][j] + inG[i][j + 2]);
                    ave0[1] = in0[i][j + 1];
                    ave2[1] = 1. / 4. * (in2[i - 1][j] + in2[i + 1][j] + in2[i - 1][j + 2] + in2[i + 1][j + 2]);
                    break;
                case 2:
                    Gave[2] = 1. / 2. * (inG[i][j] + inG[i + 2][j]);
                    ave2[2] = in2[i + 1][j];
                    ave0[2] = 1. / 4. * (in0[i][j - 1] + in0[i][j + 1] + in0[i + 2][j - 1] + in0[i + 2][j + 1]);
                    break;
                case 3:
                    Gave[3] = 1. / 2. * (inG[i][j] + inG[i][j - 2]);
                    ave0[3] = in0[i][j - 1];
                    ave2[3] = 1. / 4. * (in2[i - 1][j - 2] + in2[i - 1][j] + in2[i + 1][j - 2] + in2[i + 1][j]);
                    break;
                case 4:
                    ave0[4] = 1. / 2. * (in0[i - 2][j + 1] + in0[i][j + 1]);
                    ave2[4] = 1. / 2. * (in2[i - 1][j] + in2[i - 1][j + 2]);
                    Gave[4] = inG[i - 1][j + 1];
                    break;
                case 5:
                    ave0[5] = 1. / 2. * (in0[i][j + 1] + in0[i + 2][j + 1]);
                    ave2[5] = 1. / 2. * (in2[i + 1][j] + in2[i + 1][j + 2]);
                    Gave[5] = inG[i + 1][j + 1];
                    break;
                case 6:
                    ave0[6] = 1. / 2. * (in0[i][j - 1] + in0[i - 2][j - 1]);
                    ave2[6] = 1. / 2. * (in2[i - 1][j - 2] + in2[i - 1][j]);
                    Gave[6] = inG[i - 1][j - 1];
                    break;
                case 7:
                    ave0[7] = 1. / 2. * (in0[i][j - 1] + in0[i + 2][j - 1]);
                    ave2[7] = 1. / 2. * (in2[i + 1][j - 2] + in2[i + 1][j]);
                    Gave[7] = inG[i + 1][j - 1];
                    break;
            }
        }
    }
