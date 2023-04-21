    public float derTimeWarpingPointInterpol(double[][] dataFocal, double[][] dataComp, double[] sdf) {
        int length1 = dataFocal[0].length;
        int length2 = dataComp[0].length;
        int length21 = dataComp.length - 1;
        double min, sc, sc2, d1, a1, b1, c1, xx1, xx2, xx3, x1, x2, x3;
        ;
        int x, y, z, id, i, j, k, locx, locy;
        x1 = 0;
        x2 = 0;
        x3 = 0;
        sc2 = 0;
        j = 0;
        for (i = 0; i < length2; i++) {
            if (dataComp[length21][i] == 0) {
                j++;
            }
        }
        int length3 = j;
        int[] w = new int[length3];
        j = 0;
        for (i = 0; i < length2; i++) {
            if (dataComp[length21][i] == 0) {
                w[j] = i;
                j++;
            }
        }
        double[][] seg1 = new double[length3][dims];
        double[][] seg2 = new double[length3][dims];
        double[] d2 = new double[length3];
        double[] d3 = new double[length3];
        j = 0;
        for (i = 0; i < length2; i++) {
            if (dataComp[length21][i] == 0) {
                for (id = 0; id < dims; id++) {
                    a1 = dataComp[id][w[j]] * sdf[id];
                    b1 = dataComp[id][w[j] + 1] * sdf[id];
                    seg1[j][id] = a1;
                    seg2[j][id] = b1;
                    c1 = b1 - a1;
                    d2[j] += c1 * c1;
                }
                d3[j] = Math.sqrt(d2[j]);
                j++;
            }
        }
        for (i = 0; i < length1; i++) {
            for (j = 0; j < length3; j++) {
                s[i][j] = 0;
                xx1 = 0;
                xx2 = 0;
                for (id = 0; id < dims; id++) {
                    d1 = dataFocal[id][i] * sdf[id];
                    xx1 += (d1 - seg1[j][id]) * (d1 - seg1[j][id]);
                    xx2 += (d1 - seg2[j][id]) * (d1 - seg2[j][id]);
                }
                if ((xx2 - d2[j] - xx1) > 0) {
                    x1 = Math.sqrt(xx1);
                    s[i][j] = x1;
                    x = 1;
                } else if ((xx1 - d2[j] - xx2) > 0) {
                    x2 = Math.sqrt(xx2);
                    s[i][j] = x2;
                    x = 2;
                } else {
                    x1 = Math.sqrt(xx1);
                    x2 = Math.sqrt(xx2);
                    x3 = d3[j];
                    sc = 0.5 * (x3 + x1 + x2);
                    xx1 = sc - x1;
                    xx2 = sc - x2;
                    xx3 = sc - x3;
                    if (xx3 >= 0) {
                        sc2 = Math.sqrt(sc * xx1 * xx2 * xx3);
                        s[i][j] = 2 * sc2 / d3[j];
                    } else {
                        s[i][j] = 0;
                    }
                    x = 3;
                }
                if (Double.isNaN(s[i][j])) {
                    System.out.println(x + " " + x1 + " " + x2 + " " + x3 + " " + sc2);
                }
            }
        }
        r[0][0] = s[0][0];
        p[0][0] = 1;
        if (weightByAmp) {
            q[0][0] = dataFocal[dims][0];
        }
        for (i = 0; i < length1; i++) {
            for (j = 0; j < length3; j++) {
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
                            proute[i][j] = k;
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
        int bb = length3 - 1;
        double den = p[ba][bb];
        if (weightByAmp) {
            den = q[ba][bb];
        }
        j = bb;
        double finalScore = 0;
        den = 0;
        for (i = ba; i >= 0; i--) {
            sc2 = s[i][j];
            sc = 1;
            while (proute[i][j] == 2) {
                j--;
                sc2 += s[i][j];
                sc++;
            }
            sc2 /= sc;
            if (weightByAmp) {
                finalScore += sc2 * dataFocal[dims][i];
                den += dataFocal[dims][i];
            } else {
                finalScore += sc2;
            }
            if (proute[i][j] == 1) {
                j--;
            }
        }
        if (weightByAmp) {
            finalScore /= den;
        } else {
            finalScore /= length1 + 0.0;
        }
        float result = (float) finalScore;
        return result;
    }
