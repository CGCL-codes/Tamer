    public static double[][] compute2DNodesFromCells(double[][] zc, int ni, int nj) {
        double[][] zn = new double[nj + 1][ni + 1];
        int o2;
        int o3;
        int o4;
        zn[0][0] = zc[0][0];
        zn[0][ni] = zc[0][ni - 1];
        zn[nj][0] = zc[nj - 1][0];
        zn[nj][ni] = zc[nj - 1][ni - 1];
        for (int i = 1; i < ni; i++) {
            zn[0][i] = 0.5f * (zc[0][i - 1] + zc[0][i]);
            zn[nj][i] = 0.5f * (zc[nj - 1][i - 1] + zc[nj - 1][i]);
        }
        for (int j = 1; j < nj; j++) {
            zn[j][0] = 0.5f * (zc[j - 1][0] + zc[j][0]);
            zn[j][ni] = 0.5f * (zc[j - 1][ni - 1] + zc[j][ni - 1]);
        }
        for (int j = 1; j < nj; j++) {
            for (int i = 1; i < ni; i++) {
                zn[j][i] = 0.25f * (zc[j - 1][i - 1] + zc[j][i - 1] + zc[j - 1][i] + zc[j][i]);
            }
        }
        return zn;
    }
