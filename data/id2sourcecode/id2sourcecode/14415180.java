    private int takeStep(int i1, int i2) {
        if (i1 == i2) return 0;
        int y1 = 0, y2 = 0, s = 0;
        double alph1 = 0, alph2 = 0;
        double a1 = 0, a2 = 0;
        double E1 = 0, E2 = 0, L = 0, H = 0, k11 = 0, k22 = 0, k12 = 0, eta = 0, Lobj = 0, Hobj = 0;
        alph1 = alph[i1];
        y1 = target[i1];
        if (alph1 > 0 && alph1 < C) {
            E1 = errorCache[i1];
        } else {
            E1 = learnedFunc(i1) - y1;
        }
        alph2 = alph[i2];
        y2 = target[i2];
        if (alph2 > 0 && alph2 < C) {
            E2 = errorCache[i2];
        } else {
            E2 = learnedFunc(i2) - y2;
        }
        s = y1 * y2;
        if (y1 == y2) {
            double gamma = alph1 + alph2;
            if (gamma > C) {
                L = gamma - C;
                H = C;
            } else {
                L = 0;
                H = gamma;
            }
        } else {
            double gamma = alph1 - alph2;
            if (gamma > 0) {
                L = 0;
                H = C - gamma;
            } else {
                L = -gamma;
                H = C;
            }
        }
        if (L == H) return 0;
        k11 = kernel.eval(precomputedDots.position(i1, i1));
        k12 = kernel.eval(precomputedDots.position(i1, i2));
        k22 = kernel.eval(precomputedDots.position(i2, i2));
        eta = 2 * k12 - k11 - k22;
        if (eta < 0) {
            a2 = alph2 + y2 * (E2 - E1) / eta;
            if (a2 < L) {
                a2 = L;
            } else if (a2 > H) {
                a2 = H;
            }
        } else {
            double c1 = eta / 2;
            double c2 = y2 * (E1 - E2) - eta * alph2;
            Lobj = c1 * L * L + c2 * L;
            Hobj = c1 * H * H + c2 * H;
            if (Lobj > Hobj + epsilon) {
                a2 = L;
            } else if (Lobj < Hobj - epsilon) {
                a2 = H;
            } else {
                a2 = alph2;
            }
        }
        if (Math.abs(a2 - alph2) < epsilon * (a2 + alph2 + epsilon)) return 0;
        a1 = alph1 - s * (a2 - alph2);
        if (a1 < 0) {
            a2 += s * a1;
            a1 = 0;
        } else if (a1 > C) {
            double t = a1 - C;
            a2 += s * t;
            a1 = C;
        }
        double b1 = 0, b2 = 0, bnew = 0;
        if (a1 > 0 && a1 < C) {
            bnew = b + E1 + y1 * (a1 - alph1) * k11 + y2 * (a2 - alph2) * k12;
        } else {
            if (a2 > 0 && a2 < C) {
                bnew = b + E2 + y1 * (a1 - alph1) * k12 + y2 * (a2 - alph2) * k22;
            } else {
                b1 = b + E1 + y1 * (a1 - alph1) * k11 + y2 * (a2 - alph2) * k12;
                b2 = b + E2 + y1 * (a1 - alph1) * k12 + y2 * (a2 - alph2) * k22;
                bnew = (b1 + b2) / 2;
            }
        }
        deltaB = bnew - b;
        b = bnew;
        double t1 = y1 * (a1 - alph1);
        double t2 = y2 * (a2 - alph2);
        for (int i = 0; i < nSupportVectors; i++) {
            if (0 < alph[i] && alph[i] < C) {
                double tmp = errorCache[i];
                tmp += (t1 * kernel.eval(precomputedDots.position(i1, i))) + (t2 * kernel.eval(precomputedDots.position(i2, i))) - deltaB;
                errorCache[i] = tmp;
            }
        }
        errorCache[i1] = 0.0;
        errorCache[i2] = 0.0;
        alph[i1] = a1;
        alph[i2] = a2;
        return 1;
    }
