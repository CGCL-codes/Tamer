    public void searchSize(Point ePoint, double incX, double incY, double minXVal, double minYVal, double maxXVal, double maxYVal) {
        double Xv, Yv, Xm, Ym;
        Xm = (minXVal + maxXVal) / 2;
        Ym = (minYVal + maxYVal) / 2;
        Xv = ePoint.getX() - incX;
        Yv = ePoint.getY() - incY;
        if (Xv >= Xm && incY == 0) {
            setSize(2);
        } else if (Xv <= Xm && incY == 0) {
            setSize(0);
        } else if (Yv >= Ym && incX == 0) {
            setSize(1);
        } else if (Yv <= Ym && incX == 0) {
            setSize(3);
        }
    }
