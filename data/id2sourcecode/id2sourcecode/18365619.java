    @SuppressWarnings("unchecked")
    void becomesUndefined(Point2D.Float p1, double t1, Point2D.Float p2, double t2, int depth, Function xFunc, Function yFunc) {
        if (depth >= MAXDEPTH) {
            if (points.elementAt(points.size() - 1) != p1) points.addElement(p1);
            points.addElement(new Point2D.Float(Float.MIN_VALUE, 0));
            return;
        }
        double t = (t1 + t2) / 2;
        Point2D.Float p = eval(t, null, null, xFunc, yFunc);
        if (p == null) becomesUndefined(p1, t1, p, t, depth + 1, xFunc, yFunc); else becomesUndefined(p, t, p2, t2, depth + 1, xFunc, yFunc);
    }
