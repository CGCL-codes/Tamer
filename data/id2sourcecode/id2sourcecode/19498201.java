    private static void lbfgsmcstep(MDouble stx, MDouble fx, MDouble dx, MDouble sty, MDouble fy, MDouble dy, MDouble stp, double fp, double dp, MBool brackt, double stmin, double stmax, MInt info) {
        Boolean bound = false;
        double gamma = 0;
        double p = 0;
        double q = 0;
        double r = 0;
        double s = 0;
        double sgnd = 0;
        double stpc = 0;
        double stpf = 0;
        double stpq = 0;
        double theta = 0;
        info.value = 0;
        if (brackt.value & (stp.value <= Math.min(stx.value, sty.value) | stp.value >= Math.max(stx.value, sty.value)) | dx.value * (stp.value - stx.value) >= 0 | stmax < stmin) {
            return;
        }
        sgnd = dp * (dx.value / Math.abs(dx.value));
        if (fp > fx.value) {
            info.value = 1;
            bound = true;
            theta = 3 * (fx.value - fp) / (stp.value - stx.value) + dx.value + dp;
            s = Math.max(Math.abs(theta), Math.max(Math.abs(dx.value), Math.abs(dp)));
            gamma = s * Math.sqrt(Math.pow(theta / s, 2.0) - dx.value / s * (dp / s));
            if (stp.value < stx.value) {
                gamma = -gamma;
            }
            p = gamma - dx.value + theta;
            q = gamma - dx.value + gamma + dp;
            r = p / q;
            stpc = stx.value + r * (stp.value - stx.value);
            stpq = stx.value + dx.value / ((fx.value - fp) / (stp.value - stx.value) + dx.value) / 2 * (stp.value - stx.value);
            if (Math.abs(stpc - stx.value) < Math.abs(stpq - stx.value)) {
                stpf = stpc;
            } else {
                stpf = stpc + (stpq - stpc) / 2;
            }
            brackt.value = true;
        } else {
            if (sgnd < 0) {
                info.value = 2;
                bound = false;
                theta = 3 * (fx.value - fp) / (stp.value - stx.value) + dx.value + dp;
                s = Math.max(Math.abs(theta), Math.max(Math.abs(dx.value), Math.abs(dp)));
                gamma = s * Math.sqrt(Math.pow(theta / s, 2.0) - dx.value / s * (dp / s));
                if (stp.value > stx.value) {
                    gamma = -gamma;
                }
                p = gamma - dp + theta;
                q = gamma - dp + gamma + dx.value;
                r = p / q;
                stpc = stp.value + r * (stx.value - stp.value);
                stpq = stp.value + dp / (dp - dx.value) * (stx.value - stp.value);
                if (Math.abs(stpc - stp.value) > Math.abs(stpq - stp.value)) {
                    stpf = stpc;
                } else {
                    stpf = stpq;
                }
                brackt.value = true;
            } else {
                if (Math.abs(dp) < Math.abs(dx.value)) {
                    info.value = 3;
                    bound = true;
                    theta = 3 * (fx.value - fp) / (stp.value - stx.value) + dx.value + dp;
                    s = Math.max(Math.abs(theta), Math.max(Math.abs(dx.value), Math.abs(dp)));
                    gamma = s * Math.sqrt(Math.max(0, Math.pow(theta / s, 2.0) - dx.value / s * (dp / s)));
                    if (stp.value > stx.value) {
                        gamma = -gamma;
                    }
                    p = gamma - dp + theta;
                    q = gamma + (dx.value - dp) + gamma;
                    r = p / q;
                    if (r < 0 & gamma != 0) {
                        stpc = stp.value + r * (stx.value - stp.value);
                    } else {
                        if (stp.value > stx.value) {
                            stpc = stmax;
                        } else {
                            stpc = stmin;
                        }
                    }
                    stpq = stp.value + dp / (dp - dx.value) * (stx.value - stp.value);
                    if (brackt.value) {
                        if (Math.abs(stp.value - stpc) < Math.abs(stp.value - stpq)) {
                            stpf = stpc;
                        } else {
                            stpf = stpq;
                        }
                    } else {
                        if (Math.abs(stp.value - stpc) > Math.abs(stp.value - stpq)) {
                            stpf = stpc;
                        } else {
                            stpf = stpq;
                        }
                    }
                } else {
                    info.value = 4;
                    bound = false;
                    if (brackt.value) {
                        theta = 3 * (fp - fy.value) / (sty.value - stp.value) + dy.value + dp;
                        s = Math.max(Math.abs(theta), Math.max(Math.abs(dy.value), Math.abs(dp)));
                        gamma = s * Math.sqrt(Math.pow(theta / s, 2.0) - dy.value / s * (dp / s));
                        if (stp.value > sty.value) {
                            gamma = -gamma;
                        }
                        p = gamma - dp + theta;
                        q = gamma - dp + gamma + dy.value;
                        r = p / q;
                        stpc = stp.value + r * (sty.value - stp.value);
                        stpf = stpc;
                    } else {
                        if (stp.value > stx.value) {
                            stpf = stmax;
                        } else {
                            stpf = stmin;
                        }
                    }
                }
            }
        }
        if (fp > fx.value) {
            sty.value = stp.value;
            fy.value = fp;
            dy.value = dp;
        } else {
            if (sgnd < 0.0) {
                sty.value = stx.value;
                fy.value = fx.value;
                dy.value = dx.value;
            }
            stx.value = stp.value;
            fx.value = fp;
            dx.value = dp;
        }
        stpf = Math.min(stmax, stpf);
        stpf = Math.max(stmin, stpf);
        stp.value = stpf;
        if (brackt.value & bound) {
            if (sty.value > stx.value) {
                stp.value = Math.min(stx.value + 0.66 * (sty.value - stx.value), stp.value);
            } else {
                stp.value = Math.max(stx.value + 0.66 * (sty.value - stx.value), stp.value);
            }
        }
    }
