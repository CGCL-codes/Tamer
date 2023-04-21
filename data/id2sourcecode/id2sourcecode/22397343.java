    public void centermass(Byte image[][], int x1, int y1, int x2, int y2, int x[], int y[]) {
        int width = x2 - x1;
        int height = y2 - y1;
        int xmean = (x1 + x2) / 2;
        int ymean = (y1 + y2) / 2;
        int sum1 = 0;
        int sum2 = 0;
        int sum = 0;
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                sum1 += image[i][j] * xmean;
                sum2 += image[i][j] * ymean;
                sum += image[i][j];
            }
        }
        x[0] = sum1 / sum;
        y[0] = sum2 / sum;
    }
