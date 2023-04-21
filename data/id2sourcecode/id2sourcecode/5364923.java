    public float derTimeWarpingPointOld(double[][] dataFocal, double[][] dataComp, double[] sdf) {
        int length1 = dataFocal[0].length - 1;
        int length2 = dataComp[0].length;
        double min, sc2, b, d1, d, a1, b1, c1;
        int x, y, z, id, i, j, k, locx, locy;
        double[][][] seg1 = new double[length1][dims][2];
        double[][] seg2 = new double[dims][2];
        double[] vec2 = new double[dims];
        double[] d2 = new double[length1];
        double[] point2 = new double[dims];
        double[][] vec1 = new double[length1][dims];
        for (i = 0; i < length1; i++) {
            for (id = 0; id < dims; id++) {
                a1 = dataFocal[id][i] * sdf[id];
                b1 = dataFocal[id][i + 1] * sdf[id];
                seg1[i][id][0] = a1;
                seg1[i][id][1] = b1;
                c1 = b1 - a1;
                d2[i] += c1 * c1;
                vec1[i][id] = c1;
            }
        }
        for (i = 0; i < length1; i++) {
            for (j = 0; j < length2; j++) {
                s[i][j] = 0;
                d1 = 0;
                for (id = 0; id < dims; id++) {
                    seg2[id][1] = seg1[i][id][0];
                    seg2[id][0] = dataComp[id][j] * sdf[id];
                    vec2[id] = seg2[id][1] - seg2[id][0];
                    d1 += vec1[i][id] * vec2[id];
                }
                if (d1 <= 0) {
                    d = 0;
                    for (id = 0; id < dims; id++) {
                        d += (seg1[i][id][0] - seg2[id][0]) * (seg1[i][id][0] - seg2[id][0]);
                    }
                    s[i][j] = d;
                } else {
                    if (d2[i] <= d1) {
                        d = 0;
                        for (id = 0; id < dims; id++) {
                            d += (seg1[i][id][1] - seg2[id][0]) * (seg1[i][id][1] - seg2[id][0]);
                        }
                        s[i][j] = d;
                    } else {
                        b = d1 / d2[i];
                        for (id = 0; id < dims; id++) {
                            point2[id] = seg1[i][id][0] + b * vec1[i][id];
                        }
                        d = 0;
                        for (id = 0; id < dims; id++) {
                            d += (point2[id] - seg2[id][0]) * (point2[id] - seg2[id][0]);
                        }
                        s[i][j] = d;
                    }
                }
                if (weightByAmp) {
                    q[i][j] = 0.5 * (dataFocal[dims][i] + dataFocal[dims][i + 1]);
                } else {
                    q[i][j] = 1;
                }
            }
        }
        r[0][0] = s[0][0];
        p[0][0] = q[0][0];
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
                    if (weightByAmp) {
                        r[i][j] = r[locx][locy] + s[i][j] * q[i][j];
                        p[i][j] = q[i][j] + p[locx][locy];
                    } else {
                        r[i][j] = r[locx][locy] + s[i][j];
                        p[i][j] = p[locx][locy] + 1;
                    }
                }
            }
        }
        return (float) Math.sqrt(r[length1 - 1][length2 - 1] / p[length1 - 1][length2 - 1]);
    }
