    public boolean findIntersect(Curve that, double yrange[], double ymin, int slevel, int tlevel, double s0, double xs0, double ys0, double s1, double xs1, double ys1, double t0, double xt0, double yt0, double t1, double xt1, double yt1) {
        if (ys0 > yt1 || yt0 > ys1) {
            return false;
        }
        if (Math.min(xs0, xs1) > Math.max(xt0, xt1) || Math.max(xs0, xs1) < Math.min(xt0, xt1)) {
            return false;
        }
        if (s1 - s0 > TMIN) {
            double s = (s0 + s1) / 2;
            double xs = this.XforT(s);
            double ys = this.YforT(s);
            if (s == s0 || s == s1) {
                System.out.println("s0 = " + s0);
                System.out.println("s1 = " + s1);
                throw new InternalError("no s progress!");
            }
            if (t1 - t0 > TMIN) {
                double t = (t0 + t1) / 2;
                double xt = that.XforT(t);
                double yt = that.YforT(t);
                if (t == t0 || t == t1) {
                    System.out.println("t0 = " + t0);
                    System.out.println("t1 = " + t1);
                    throw new InternalError("no t progress!");
                }
                if (ys >= yt0 && yt >= ys0) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s0, xs0, ys0, s, xs, ys, t0, xt0, yt0, t, xt, yt)) {
                        return true;
                    }
                }
                if (ys >= yt) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s0, xs0, ys0, s, xs, ys, t, xt, yt, t1, xt1, yt1)) {
                        return true;
                    }
                }
                if (yt >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s, xs, ys, s1, xs1, ys1, t0, xt0, yt0, t, xt, yt)) {
                        return true;
                    }
                }
                if (ys1 >= yt && yt1 >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s, xs, ys, s1, xs1, ys1, t, xt, yt, t1, xt1, yt1)) {
                        return true;
                    }
                }
            } else {
                if (ys >= yt0) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel, s0, xs0, ys0, s, xs, ys, t0, xt0, yt0, t1, xt1, yt1)) {
                        return true;
                    }
                }
                if (yt1 >= ys) {
                    if (findIntersect(that, yrange, ymin, slevel + 1, tlevel, s, xs, ys, s1, xs1, ys1, t0, xt0, yt0, t1, xt1, yt1)) {
                        return true;
                    }
                }
            }
        } else if (t1 - t0 > TMIN) {
            double t = (t0 + t1) / 2;
            double xt = that.XforT(t);
            double yt = that.YforT(t);
            if (t == t0 || t == t1) {
                System.out.println("t0 = " + t0);
                System.out.println("t1 = " + t1);
                throw new InternalError("no t progress!");
            }
            if (yt >= ys0) {
                if (findIntersect(that, yrange, ymin, slevel, tlevel + 1, s0, xs0, ys0, s1, xs1, ys1, t0, xt0, yt0, t, xt, yt)) {
                    return true;
                }
            }
            if (ys1 >= yt) {
                if (findIntersect(that, yrange, ymin, slevel, tlevel + 1, s0, xs0, ys0, s1, xs1, ys1, t, xt, yt, t1, xt1, yt1)) {
                    return true;
                }
            }
        } else {
            double xlk = xs1 - xs0;
            double ylk = ys1 - ys0;
            double xnm = xt1 - xt0;
            double ynm = yt1 - yt0;
            double xmk = xt0 - xs0;
            double ymk = yt0 - ys0;
            double det = xnm * ylk - ynm * xlk;
            if (det != 0) {
                double detinv = 1 / det;
                double s = (xnm * ymk - ynm * xmk) * detinv;
                double t = (xlk * ymk - ylk * xmk) * detinv;
                if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
                    s = s0 + s * (s1 - s0);
                    t = t0 + t * (t1 - t0);
                    if (s < 0 || s > 1 || t < 0 || t > 1) {
                        System.out.println("Uh oh!");
                    }
                    double y = (this.YforT(s) + that.YforT(t)) / 2;
                    if (y <= yrange[1] && y > yrange[0]) {
                        yrange[1] = y;
                        return true;
                    }
                }
            }
        }
        return false;
    }
