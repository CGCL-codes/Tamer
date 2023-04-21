    protected void calculate(double[][] database) {
        int SP_NUM = getPointCount() - 1;
        if (SP_NUM < 0) {
            koef = null;
            return;
        }
        koef = new double[SP_NUM][4];
        double[] h = new double[SP_NUM];
        for (int i = 0; i < SP_NUM; i++) {
            h[i] = database[i + 1][0] - database[i][0];
            koef[i][0] = database[i][1];
        }
        double[] sub = new double[SP_NUM];
        double[] diag = new double[SP_NUM];
        double[] sup = new double[SP_NUM];
        double[] f = new double[SP_NUM];
        for (int i = 1; i < SP_NUM - 1; i++) {
            sub[i] = h[i] / 3;
            sup[i] = h[i - 1] / 3;
            diag[i] = 2 * h[i] / 3 + 2 * h[i - 1] / 3;
            f[i] = (database[i + 1][1] + database[i][1]) / h[i] - (database[i][1] + database[i - 1][1]) / h[i - 1];
        }
        sub[0] = h[0] / 3;
        sup[SP_NUM - 1] = h[SP_NUM - 1] / 3;
        diag[0] = 2 * h[0] / 3;
        diag[SP_NUM - 1] = 2 * h[SP_NUM - 1] / 3;
        f[0] = (database[1][1] + database[0][1]) / h[0] - getBorderLeft();
        f[SP_NUM - 1] = -(database[SP_NUM][1] + database[SP_NUM - 1][1]) / h[SP_NUM - 1] + getBorderRight();
        solveTridiag(sub, diag, sup, f, SP_NUM);
        koef[0][1] = getBorderLeft();
        koef[SP_NUM - 1][1] = getBorderRight();
        for (int i = 1; i < SP_NUM - 1; i++) {
            koef[i][2] = f[i];
            koef[i][3] = (f[i + 1] - f[i]) / 3 / h[i];
            koef[i][1] = (f[i] + f[i - 1]) * h[i - 1] + koef[i - 1][1];
        }
        koef[0][2] = f[0];
        koef[SP_NUM - 1][2] = f[SP_NUM - 1];
        koef[0][3] = (f[1] - f[0]) / 3 / h[0];
        koef[SP_NUM - 1][3] = (database[SP_NUM][1] - database[SP_NUM][1]) / h[SP_NUM - 1] / h[SP_NUM - 1] / h[SP_NUM - 1] - f[SP_NUM - 1] / h[SP_NUM - 1] - koef[SP_NUM - 1][1] / h[SP_NUM - 1] / h[SP_NUM - 1];
    }
