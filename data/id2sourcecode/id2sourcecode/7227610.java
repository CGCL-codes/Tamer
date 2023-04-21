    public void calculate(double[][] database) {
        int SP_NUM = getPointCount() - 1;
        if (SP_NUM < 0) {
            koef = null;
            return;
        }
        koef = new double[SP_NUM][3];
        double[] h = new double[SP_NUM];
        for (int i = 0; i < SP_NUM; i++) {
            if (database[i + 1] == null) {
                SP_NUM = i;
                break;
            }
            h[i] = database[i + 1][0] - database[i][0];
            koef[i][0] = database[i][1];
        }
        if (bType == false) {
            koef[0][1] = border;
            koef[0][2] = 2 * ((database[1][1] - database[0][1]) / h[0] / h[0] - koef[0][1] / h[0]);
            for (int i = 1; i < SP_NUM; i++) {
                koef[i][2] = 2 / h[i] * ((database[i + 1][1] - database[i][1]) / h[i] - (database[i][1] - database[i - 1][1]) / h[i - 1] - koef[i - 1][2] * h[i - 1] / 2);
                koef[i][1] = (database[i + 1][1] - database[i][1]) / h[i] - koef[i][2] / 2 * h[i];
            }
        } else {
            koef[SP_NUM - 1][2] = -2 * ((database[SP_NUM][1] - database[SP_NUM - 1][1]) / h[SP_NUM - 1] / h[SP_NUM - 1] - border / h[SP_NUM - 1]);
            koef[SP_NUM - 1][1] = border - koef[SP_NUM - 1][2] * h[SP_NUM - 1];
            for (int i = SP_NUM - 2; i >= 0; i--) {
                koef[i][2] = 2 / h[i] * ((database[i + 2][1] - database[i + 1][1]) / h[i + 1] - (database[i + 1][1] - database[i][1]) / h[i] - koef[i + 1][2] * h[i + 1] / 2);
                koef[i][1] = (database[i + 1][1] - database[i][1]) / h[i] - koef[i][2] / 2 * h[i];
            }
        }
    }
