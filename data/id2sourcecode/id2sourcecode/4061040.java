    float solve(float[] solution, boolean lmObjective, int mytry) {
        float min, fit, localftol;
        float[] myxy;
        float[][] p;
        float[] y;
        int i, j, restarted, num;
        num = solution.length;
        localftol = ftol;
        myxy = new float[num + 1];
        p = new float[num + 1 + 1][];
        p[1] = myxy;
        for (i = 2; i <= num + 1; i++) {
            p[i] = new float[num + 1];
        }
        y = new float[num + 1 + 1];
        while (true) {
            min = MAX_ERR;
            for (i = 0; i < mytry; i++) {
                restarted = 0;
                for (j = 1; j <= num; j++) {
                    if (randomInit) myxy[j] = (float) (Math.random() * range) - (range / 2); else myxy[j] = 0;
                }
                boolean restart;
                restart = true;
                while (restart) {
                    float[] tmpstore = copyOne(myxy);
                    y[1] = funObjective(tmpstore, lmObjective);
                    tmpstore = null;
                    for (j = 2; j <= num + 1; j++) {
                        for (int tmpc = 0; tmpc <= num; tmpc++) p[j][tmpc] = myxy[tmpc];
                        p[j][j - 1] += lambda;
                        tmpstore = copyOne(p[j]);
                        y[j] = funObjective(tmpstore, lmObjective);
                        tmpstore = null;
                    }
                    amoeba(p, y, num, localftol, lmObjective, j);
                    if (j < 0) {
                        System.out.println("No answer");
                        restart = false;
                        break;
                    }
                    if (restarted < restarts) {
                        restarted++;
                        continue;
                    } else {
                        break;
                    }
                }
                fit = funObjective(copyOne(myxy), lmObjective);
                if (restart) {
                    float[] tmpstore = copyOne(myxy);
                    fit = funObjective(tmpstore, lmObjective);
                    if (fit < min) {
                        min = fit;
                        for (j = 0; j < num; j++) {
                            solution[j] = myxy[j + 1];
                        }
                    }
                }
            }
            if (min == MAX_ERR) {
                localftol = localftol * 10;
                continue;
            } else {
                break;
            }
        }
        y = null;
        myxy = null;
        for (i = 2; i <= num + 1; i++) {
            p[i] = null;
        }
        p = null;
        myxy = null;
        return min;
    }
