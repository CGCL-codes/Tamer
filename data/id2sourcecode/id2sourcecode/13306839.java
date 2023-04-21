    public void searchVertex(Point ePoint, double incX, double incY, double minXVal, double minYVal, double maxXVal, double maxYVal) {
        double Xv, Yv, Xm, Ym;
        Xm = (minXVal + maxXVal) / 2;
        Ym = (minYVal + maxYVal) / 2;
        Xv = ePoint.getX() - incX;
        Yv = ePoint.getY() - incY;
        if (Xv >= Xm && Yv <= Ym) {
            setVertex(2);
        } else if (Xv <= Xm && Yv <= Ym) {
            setVertex(3);
        } else if (Xv >= Xm && Yv >= Ym) {
            setVertex(1);
        } else if (Xv <= Xm && Yv >= Ym) {
            setVertex(0);
        }
    }
