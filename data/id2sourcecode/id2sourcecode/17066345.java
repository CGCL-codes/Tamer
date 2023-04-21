    private static int[] getNeighbors(int imgval[][], int x, int y, int w, int h) {
        int a[] = new int[10];
        for (int n = 1; n < 10; n++) {
            a[n] = 0;
        }
        if (y - 1 >= 0) {
            a[2] = imgval[x][y - 1];
            if (x + 1 < w) {
                a[3] = imgval[x + 1][y - 1];
            }
            if (x - 1 >= 0) {
                a[9] = imgval[x - 1][y - 1];
            }
        }
        if (y + 1 < h) {
            a[6] = imgval[x][y + 1];
            if (x + 1 < w) {
                a[5] = imgval[x + 1][y + 1];
            }
            if (x - 1 >= 0) {
                a[7] = imgval[x - 1][y + 1];
            }
        }
        if (x + 1 < w) {
            a[4] = imgval[x + 1][y];
        }
        if (x - 1 >= 0) {
            a[8] = imgval[x - 1][y];
        }
        return a;
    }
