        @Override
        public void update() {
            final double[] dx = new double[n - 1];
            final double[] S = new double[n - 1];
            double[] tmp = new double[n];
            for (int i = 0; i < n - 1; ++i) {
                dx[i] = vx_[i + 1] - vx_[i];
                S[i] = (vy_[i + 1] - vy_[i]) / dx[i];
            }
            if (da == CubicInterpolation.DerivativeApprox.Spline) {
                final TridiagonalOperator L = new TridiagonalOperator(n);
                for (int i = 1; i < n - 1; ++i) {
                    L.setMidRow(i, dx[i], 2.0 * (dx[i] + dx[i - 1]), dx[i - 1]);
                    tmp[i] = 3.0 * (dx[i] * S[i - 1] + dx[i - 1] * S[i]);
                }
                switch(leftType) {
                    case NotAKnot:
                        L.setFirstRow(dx[1] * (dx[1] + dx[0]), (dx[0] + dx[1]) * (dx[0] + dx[1]));
                        tmp[0] = S[0] * dx[1] * (2.0 * dx[1] + 3.0 * dx[0]) + S[1] * dx[0] * dx[0];
                        break;
                    case FirstDerivative:
                        L.setFirstRow(1.0, 0.0);
                        tmp[0] = leftValue;
                        break;
                    case SecondDerivative:
                        L.setFirstRow(2.0, 1.0);
                        tmp[0] = 3.0 * S[0] - leftValue * dx[0] / 2.0;
                        break;
                    case Periodic:
                    case Lagrange:
                        throw new LibraryException("this end condition is not implemented yet");
                    default:
                        throw new LibraryException("unknown end condition");
                }
                switch(rightType) {
                    case NotAKnot:
                        L.setLastRow(-(dx[n - 2] + dx[n - 3]) * (dx[n - 2] + dx[n - 3]), -dx[n - 3] * (dx[n - 3] + dx[n - 2]));
                        tmp[n - 1] = -S[n - 3] * dx[n - 2] * dx[n - 2] - S[n - 2] * dx[n - 3] * (3.0 * dx[n - 2] + 2.0 * dx[n - 3]);
                        break;
                    case FirstDerivative:
                        L.setLastRow(0.0, 1.0);
                        tmp[n - 1] = rightValue;
                        break;
                    case SecondDerivative:
                        L.setLastRow(1.0, 2.0);
                        tmp[n - 1] = 3.0 * S[n - 2] + rightValue * dx[n - 2] / 2.0;
                        break;
                    case Periodic:
                    case Lagrange:
                        throw new LibraryException("this end condition is not implemented yet");
                    default:
                        throw new LibraryException("unknown end condition");
                }
                tmp = L.solveFor(tmp);
            } else {
                if (n == 2) {
                    tmp[0] = tmp[1] = S[0];
                } else {
                    switch(da) {
                        case FourthOrder:
                            throw new LibraryException("FourthOrder not implemented yet");
                        case Parabolic:
                            throw new LibraryException("Parabolic not implemented yet");
                        case ModifiedParabolic:
                            throw new LibraryException("ModifiedParabolic not implemented yet");
                        case FritschButland:
                            throw new LibraryException("FritschButland not implemented yet");
                        case Akima:
                            throw new LibraryException("Akima not implemented yet");
                        case Kruger:
                            for (int i = 1; i < n - 1; ++i) {
                                if (S[i - 1] * S[i] < 0.0) {
                                    tmp[i] = 0.0;
                                } else {
                                    tmp[i] = 2.0 / (1.0 / S[i - 1] + 1.0 / S[i]);
                                }
                            }
                            tmp[0] = (3.0 * S[0] - tmp[1]) / 2.0;
                            tmp[n - 1] = (3.0 * S[n - 2] - tmp[n - 2]) / 2.0;
                            break;
                        default:
                            throw new LibraryException("unknown scheme");
                    }
                }
            }
            Arrays.fill(ma_, false);
            if (monotonic) {
                double correction;
                double pm, pu, pd, M;
                for (int i = 0; i < n; ++i) {
                    if (i == 0) {
                        if (tmp[i] * S[0] > 0.0) {
                            correction = tmp[i] / Math.abs(tmp[i]) * Math.min(Math.abs(tmp[i]), Math.abs(3.0 * S[0]));
                        } else {
                            correction = 0.0;
                        }
                        if (!Closeness.isClose(correction, tmp[i])) {
                            tmp[i] = correction;
                            ma_[i] = true;
                        }
                    } else if (i == n - 1) {
                        if (tmp[i] * S[n - 2] > 0.0) {
                            correction = tmp[i] / Math.abs(tmp[i]) * Math.min(Math.abs(tmp[i]), Math.abs(3.0 * S[n - 2]));
                        } else {
                            correction = 0.0;
                        }
                        if (!Closeness.isClose(correction, tmp[i])) {
                            tmp[i] = correction;
                            ma_[i] = true;
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
                        if (tmp[i] * pm > 0.0) {
                            correction = tmp[i] / Math.abs(tmp[i]) * Math.min(Math.abs(tmp[i]), M);
                        } else {
                            correction = 0.0;
                        }
                        if (!Closeness.isClose(correction, tmp[i])) {
                            tmp[i] = correction;
                            ma_[i] = true;
                        }
                    }
                }
            }
            for (int i = 0; i < n - 1; ++i) {
                va_[i] = tmp[i];
                vb_[i] = (3.0 * S[i] - tmp[i + 1] - 2.0 * tmp[i]) / dx[i];
                vc_[i] = (tmp[i + 1] + tmp[i] - 2.0 * S[i]) / (dx[i] * dx[i]);
            }
            vp_[0] = 0.0;
            for (int i = 1; i < n - 1; ++i) {
                vp_[i] = vp_[i - 1] + dx[i - 1] * (vy_[i - 1] + dx[i - 1] * (va_[i - 1] / 2.0 + dx[i - 1] * (vb_[i - 1] / 3.0 + dx[i - 1] * vc_[i - 1] / 4.0)));
            }
        }
