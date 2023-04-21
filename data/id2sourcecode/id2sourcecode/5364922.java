    public float derTimeWarpingPointFast(double[][] dataFocal, double[][] dataComp, double[] sdf) {
        int length1 = dataFocal[0].length;
        int length2 = dataComp[0].length;
        double min, sc, sc2;
        int x, y, z, i, j, k, locx, locy;
        for (i = 0; i < length1; i++) {
            for (j = 0; j < length2; j++) {
                sc = 0;
                for (k = 0; k < dims; k++) {
                    sc2 = (dataComp[k][j] - dataFocal[k][i]) * sdf[k];
                    sc += sc2 * sc2;
                }
                s[i][j] = Math.sqrt(sc);
            }
        }
        r[0][0] = s[0][0];
        p[0][0] = 1;
        if (weightByAmp) {
            q[0][0] = dataFocal[dims][0];
        }
        for (i = 0; i < length1; i++) {
            for (j = 0; j < length2; j++) {
                min = 1000000000;
                locx = 0;
                locy = 0;
                z = -1;
                for (k = 0; k < 3; k++) {
                    x = i + locsX[k];
                    y = j + locsY[k];
                    if ((x >= 0) && (y >= 0)) {
                        sc2 = r[x][y] / p[x][y];
                        if (sc2 < min) {
                            min = sc2;
                            locx = x;
                            locy = y;
                            z = k;
                        }
                    }
                }
                if (z >= 0) {
                    r[i][j] = r[locx][locy] + s[i][j];
                    p[i][j] = p[locx][locy] + 1;
                    if (weightByAmp) {
                        q[i][j] = q[locx][locy] + dataFocal[dims][i];
                    }
                }
            }
        }
        int ba = length1 - 1;
        int bb = length2 - 1;
        double den = p[ba][bb];
        if (weightByAmp) {
            den = q[ba][bb];
        }
        float result = (float) (r[ba][bb] / den);
        return result;
    }
