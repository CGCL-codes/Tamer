    private long findZero(long tl, long tr) {
        float fl = tideDerivDate(tl, 1);
        float fr = tideDerivDate(tr, 1);
        float scale = 1;
        long dt = 0;
        long t = 0;
        float fp = 0, ft = 0, f_thresh = 0;
        if (fl > 0) {
            scale = -1;
            fl = -fl;
            fr = -fr;
        }
        while (tr - tl > 15) {
            if (t == 0) {
                dt = 0;
            } else if (Math.abs(ft) > f_thresh || (ft > 0 ? (fp <= ft / (t - tl)) : (fp <= -ft / (tr - t)))) {
                dt = 0;
            } else {
                dt = Math.round(-ft / fp);
                if (Math.abs(dt) < 15) {
                    dt = (ft < 0 ? 15 : -15);
                }
                t += dt;
                if (t >= tr || t <= tl) {
                    dt = 0;
                }
                f_thresh = Math.abs(ft) / (float) 2.0;
            }
            if (dt == 0) {
                t = tl + (tr - tl) / 2;
                f_thresh = fr > -fl ? fr : -fl;
            }
            if ((ft = scale * tideDerivDate(t, 1)) == 0.0) {
                return t;
            } else if (ft > 0.0) {
                tr = t;
                fr = ft;
            } else {
                tl = t;
                fl = ft;
            }
            fp = scale * tideDerivDate(t, 2);
        }
        return tr;
    }
