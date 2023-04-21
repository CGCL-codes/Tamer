    @SuppressWarnings("unchecked")
    void discontinuity(Point2D.Float p1, double t1, Point2D.Float p2, double t2, int depth, Function xFunc, Function yFunc) {
        if (depth >= MAXDEPTH || (Math.abs(p1.x - p2.x) < 2 && Math.abs(p1.y - p2.y) < 2)) {
            if (points.elementAt(points.size() - 1) != p1) points.addElement(p1);
            if (depth >= MAXDEPTH) points.addElement(new Point2D.Float(Float.MIN_VALUE, 0));
            points.addElement(p2);
            return;
        }
        double t = (t1 + t2) / 2;
        Point2D.Float p = eval(t, case3x, case3y, xFunc, yFunc);
        if (p == null) {
            becomesUndefined(p1, t1, p, t, depth + 1, xFunc, yFunc);
            becomesDefined(p, t, p2, t2, depth + 1, xFunc, yFunc);
        } else if (case3x.equals(case1x) && case3y.equals(case1y)) {
            discontinuity(p, t, p2, t2, depth + 1, xFunc, yFunc);
        } else if (case3x.equals(case2x) && case3y.equals(case2y)) {
            discontinuity(p1, t1, p, t, depth + 1, xFunc, yFunc);
        } else {
            discontinuity(p1, t1, p, t, depth + 2, xFunc, yFunc);
            discontinuity(p, t, p2, t2, depth + 2, xFunc, yFunc);
        }
    }
