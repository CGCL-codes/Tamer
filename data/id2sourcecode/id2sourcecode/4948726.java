    @Override
    public void reload() {
        final TridiagonalOperator L = new TridiagonalOperator(n);
        Array tmp = new Array(n);
        final double[] dx = new double[n];
        final double[] S = new double[n];
        int i = 0;
        dx[i] = vx[i + 1] - vx[i];
        S[i] = (vy[i + 1] - vy[i]) / dx[i];
        for (i = 1; i < n - 1; i++) {
            dx[i] = vx[i + 1] - vx[i];
            S[i] = (vy[i + 1] - vy[i]) / dx[i];
            L.setMidRow(i, dx[i], 2.0 * (dx[i] + dx[i - 1]), dx[i - 1]);
            tmp.set(i, 3.0 * (dx[i] * S[i - 1] + dx[i - 1] * S[i]));
        }
        switch(leftType) {
            case NotAKnot:
                L.setFirstRow(dx[1] * (dx[1] + dx[0]), (dx[0] + dx[1]) * (dx[0] + dx[1]));
                tmp.set(0, S[0] * dx[1] * (2.0 * dx[1] + 3.0 * dx[0]) + S[1] * dx[0] * dx[0]);
                break;
            case FirstDerivative:
                L.setFirstRow(1.0, 0.0);
                tmp.set(0, leftValue);
                break;
            case SecondDerivative:
                L.setFirstRow(2.0, 1.0);
                tmp.set(0, 3.0 * S[0] - leftValue * dx[0] / 2.0);
                break;
            case Periodic:
            case Lagrange:
                throw new UnsupportedOperationException("this end condition is not implemented yet");
            default:
                throw new UnsupportedOperationException("unknown end condition");
        }
        switch(rightType) {
            case NotAKnot:
                L.setLastRow(-(dx[n - 2] + dx[n - 3]) * (dx[n - 2] + dx[n - 3]), -dx[n - 3] * (dx[n - 3] + dx[n - 2]));
                tmp.set(n - 1, -S[n - 3] * dx[n - 2] * dx[n - 2] - S[n - 2] * dx[n - 3] * (3.0 * dx[n - 2] + 2.0 * dx[n - 3]));
                break;
            case FirstDerivative:
                L.setLastRow(0.0, 1.0);
                tmp.set(n - 1, rightValue);
                break;
            case SecondDerivative:
                L.setLastRow(1.0, 2.0);
                tmp.set(n - 1, 3.0 * S[n - 2] + rightValue * dx[n - 2] / 2.0);
                break;
            case Periodic:
            case Lagrange:
                throw new UnsupportedOperationException("this end condition is not implemented yet");
            default:
                throw new UnsupportedOperationException("unknown end condition");
        }
        tmp = L.solveFor(tmp);
        if (constrained) {
            double correction;
            double pm, pu, pd, M;
            for (i = 0; i < n; i++) {
                if (i == 0) {
                    if (tmp.get(i) * S[0] > 0.0) {
                        correction = tmp.get(i) / Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), Math.abs(3.0 * S[0]));
                    } else {
                        correction = 0.0;
                    }
                    if (!Closeness.isClose(correction, tmp.get(i))) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                } else if (i == n - 1) {
                    if (tmp.get(i) * S[n - 2] > 0.0) {
                        correction = tmp.get(i) / Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), Math.abs(3.0 * S[n - 2]));
                    } else {
                        correction = 0.0;
                    }
                    if (!Closeness.isClose(correction, tmp.get(i))) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                } else {
                    pm = (S[i - 1] * dx[i] + S[i] * dx[i - 1]) / (dx[i - 1] + dx[i]);
                    M = 3.0 * Math.min(Math.min(Math.abs(S[i - 1]), Math.abs(S[i])), Math.abs(pm));
                    if (i > 1) {
                        if ((S[i - 1] - S[i - 2]) * (S[i] - S[i - 1]) > 0.0) {
                            pd = (S[i - 1] * (2.0 * dx[i - 1] + dx[i - 2]) - S[i - 2] * dx[i - 1]) / (dx[i - 2] + dx[i - 1]);
                            if (pm * pd > 0.0 && pm * (S[i - 1] - S[i - 2]) > 0.0) {
                                M = Math.max(M, 1.5 * Math.min(Math.abs(pm), Math.abs(pd)));
                            }
                        }
                    }
                    if (i < n - 2) {
                        if ((S[i] - S[i - 1]) * (S[i + 1] - S[i]) > 0.0) {
                            pu = (S[i] * (2.0 * dx[i] + dx[i + 1]) - S[i + 1] * dx[i]) / (dx[i] + dx[i + 1]);
                            if (pm * pu > 0.0 && -pm * (S[i] - S[i - 1]) > 0.0) {
                                M = Math.max(M, 1.5 * Math.min(Math.abs(pm), Math.abs(pu)));
                            }
                        }
                    }
                    if (tmp.get(i) * pm > 0.0) {
                        correction = tmp.get(i) / Math.abs(tmp.get(i)) * Math.min(Math.abs(tmp.get(i)), M);
                    } else {
                        correction = 0.0;
                    }
                    if (!Closeness.isClose(correction, tmp.get(i))) {
                        tmp.set(i, correction);
                        monotone = true;
                    }
                }
            }
        }
        for (i = 0; i < n - 1; i++) {
            va[i] = tmp.get(i);
            vb[i] = (3.0 * S[i] - tmp.get(i + 1) - 2.0 * tmp.get(i)) / dx[i];
            vc[i] = (tmp.get(i + 1) + tmp.get(i) - 2.0 * S[i]) / (dx[i] * dx[i]);
        }
        vp[0] = 0.0;
        for (i = 1; i < n - 1; i++) {
            vp[i] = vp[i - 1] + dx[i - 1] * (vy[i - 1] + dx[i - 1] * (va[i - 1] / 2.0 + dx[i - 1] * (vb[i - 1] / 3.0 + dx[i - 1] * vc[i - 1] / 4.0)));
        }
    }
