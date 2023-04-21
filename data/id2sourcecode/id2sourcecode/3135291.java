    public int[][] alignSongPair(float[][] songPair) {
        int n = songPair.length;
        int m = songPair[0].length;
        float[][] st = new float[n][m];
        st[0][0] = songPair[0][0];
        int[] locsX = { 0, -1, -1 };
        int[] locsY = { -1, 0, -1 };
        int x, y, z, locx, locy, i, j, k;
        float min;
        for (i = 0; i < n; i++) {
            for (j = 0; j < m; j++) {
                min = 1000000f;
                locx = 0;
                locy = 0;
                z = -1;
                for (k = 0; k < 3; k++) {
                    x = i + locsX[k];
                    y = j + locsY[k];
                    if ((x >= 0) && (y >= 0)) {
                        if (st[x][y] < min) {
                            min = st[x][y];
                            locx = x;
                            locy = y;
                            z = k;
                        }
                    }
                }
                if (z >= 0) {
                    st[i][j] = min + songPair[i][j];
                }
            }
        }
        int[][] trail = new int[n][m];
        int g = n - 1;
        int h = m - 1;
        trail[g][h] = 1;
        while ((g > 0) || (h > 0)) {
            min = 1000000f;
            locx = 0;
            locy = 0;
            z = -1;
            for (k = 0; k < 3; k++) {
                x = g + locsX[k];
                y = h + locsY[k];
                if ((x >= 0) && (y >= 0)) {
                    if (st[x][y] < min) {
                        min = st[x][y];
                        locx = x;
                        locy = y;
                        z = k;
                    }
                }
            }
            if (z >= 0) {
                g = locx;
                h = locy;
                trail[g][h] = 1;
            }
        }
        int[][] results = new int[2][];
        results[0] = new int[n];
        results[1] = new int[m];
        for (i = 0; i < n; i++) {
            int loc = -1;
            min = 1000000f;
            for (j = 0; j < m; j++) {
                if ((trail[i][j] == 1) && (min > songPair[i][j])) {
                    min = songPair[i][j];
                    loc = j;
                }
            }
            results[0][i] = loc;
        }
        for (i = 0; i < m; i++) {
            int loc = -1;
            min = 1000000f;
            for (j = 0; j < n; j++) {
                if ((trail[j][i] == 1) && (min > songPair[j][i])) {
                    min = songPair[j][i];
                    loc = j;
                }
            }
            results[1][i] = loc;
        }
        return results;
    }
