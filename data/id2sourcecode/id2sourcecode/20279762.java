    public static void mcstep(double[] stx1, double[] fx1, double[] dx, double[] sty1, double[] fy1, double[] dy, double[] stp, double fp, double dp, boolean[] brackt1, double stpmin, double stpmax, int[] info, int[] iprint) {
        boolean bound;
        double gamma, p, q, r, s, sgnd, stpc, stpf, stpq, theta;
        info[0] = 0;
        if ((brackt1[0] && (stp[0] <= Math.min(stx1[0], sty1[0]) || stp[0] >= Math.max(stx1[0], sty1[0]))) || dx[0] * (stp[0] - stx1[0]) >= 0.0 || stpmax < stpmin) {
            if (iprint[0] > 0) System.err.println("mcstep=0 " + brackt1[0] + " " + stp[0] + " " + stx1[0] + " " + sty1[0] + " " + dx[0] + " " + stpmax + " " + stpmin);
            return;
        }
        sgnd = dp * (dx[0] / Math.abs(dx[0]));
        if (fp > fx1[0]) {
            info[0] = 1;
            bound = true;
            theta = 3 * (fx1[0] - fp) / (stp[0] - stx1[0]) + dx[0] + dp;
            s = max3(Math.abs(theta), Math.abs(dx[0]), Math.abs(dp));
            gamma = s * Math.sqrt(sqr(theta / s) - (dx[0] / s) * (dp / s));
            if (stp[0] < stx1[0]) gamma = -gamma;
            p = (gamma - dx[0]) + theta;
            q = ((gamma - dx[0]) + gamma) + dp;
            r = p / q;
            stpc = stx1[0] + r * (stp[0] - stx1[0]);
            stpq = stx1[0] + ((dx[0] / ((fx1[0] - fp) / (stp[0] - stx1[0]) + dx[0])) / 2) * (stp[0] - stx1[0]);
            if (Math.abs(stpc - stx1[0]) < Math.abs(stpq - stx1[0])) {
                stpf = stpc;
            } else {
                stpf = stpc + (stpq - stpc) / 2;
            }
            brackt1[0] = true;
        } else if (sgnd < 0.0) {
            info[0] = 2;
            bound = false;
            theta = 3 * (fx1[0] - fp) / (stp[0] - stx1[0]) + dx[0] + dp;
            s = max3(Math.abs(theta), Math.abs(dx[0]), Math.abs(dp));
            gamma = s * Math.sqrt(sqr(theta / s) - (dx[0] / s) * (dp / s));
            if (stp[0] > stx1[0]) gamma = -gamma;
            p = (gamma - dp) + theta;
            q = ((gamma - dp) + gamma) + dx[0];
            r = p / q;
            stpc = stp[0] + r * (stx1[0] - stp[0]);
            stpq = stp[0] + (dp / (dp - dx[0])) * (stx1[0] - stp[0]);
            if (Math.abs(stpc - stp[0]) > Math.abs(stpq - stp[0])) {
                stpf = stpc;
            } else {
                stpf = stpq;
            }
            brackt1[0] = true;
        } else if (Math.abs(dp) < Math.abs(dx[0])) {
            info[0] = 3;
            bound = true;
            theta = 3 * (fx1[0] - fp) / (stp[0] - stx1[0]) + dx[0] + dp;
            s = max3(Math.abs(theta), Math.abs(dx[0]), Math.abs(dp));
            gamma = s * Math.sqrt(Math.max(0, sqr(theta / s) - (dx[0] / s) * (dp / s)));
            if (stp[0] > stx1[0]) gamma = -gamma;
            p = (gamma - dp) + theta;
            q = (gamma + (dx[0] - dp)) + gamma;
            r = p / q;
            if (r < 0.0 && gamma != 0.0) {
                stpc = stp[0] + r * (stx1[0] - stp[0]);
            } else if (stp[0] > stx1[0]) {
                stpc = stpmax;
            } else {
                stpc = stpmin;
            }
            stpq = stp[0] + (dp / (dp - dx[0])) * (stx1[0] - stp[0]);
            if (brackt1[0]) {
                if (Math.abs(stp[0] - stpc) < Math.abs(stp[0] - stpq)) {
                    stpf = stpc;
                } else {
                    stpf = stpq;
                }
            } else {
                if (Math.abs(stp[0] - stpc) > Math.abs(stp[0] - stpq)) {
                    stpf = stpc;
                } else {
                    stpf = stpq;
                }
            }
        } else {
            info[0] = 4;
            bound = false;
            if (brackt1[0]) {
                theta = 3 * (fp - fy1[0]) / (sty1[0] - stp[0]) + dy[0] + dp;
                s = max3(Math.abs(theta), Math.abs(dy[0]), Math.abs(dp));
                gamma = s * Math.sqrt(sqr(theta / s) - (dy[0] / s) * (dp / s));
                if (stp[0] > sty1[0]) gamma = -gamma;
                p = (gamma - dp) + theta;
                q = ((gamma - dp) + gamma) + dy[0];
                r = p / q;
                stpc = stp[0] + r * (sty1[0] - stp[0]);
                stpf = stpc;
            } else if (stp[0] > stx1[0]) {
                stpf = stpmax;
            } else {
                stpf = stpmin;
            }
        }
        if (fp > fx1[0]) {
            sty1[0] = stp[0];
            fy1[0] = fp;
            dy[0] = dp;
        } else {
            if (sgnd < 0.0) {
                sty1[0] = stx1[0];
                fy1[0] = fx1[0];
                dy[0] = dx[0];
            }
            stx1[0] = stp[0];
            fx1[0] = fp;
            dx[0] = dp;
        }
        stpf = Math.min(stpmax, stpf);
        stpf = Math.max(stpmin, stpf);
        setSTP(stp, stpf);
        if (brackt1[0] && bound) {
            double possibleStep = stx1[0] + TWO_THIRDS * (sty1[0] - stx1[0]);
            if (sty1[0] > stx1[0]) {
                setSTP(stp, Math.min(possibleStep, stp[0]));
            } else {
                setSTP(stp, Math.max(possibleStep, stp[0]));
            }
        }
        return;
    }
