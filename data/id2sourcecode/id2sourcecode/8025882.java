    public boolean pointInFigure(Point p) {
        int x = p.x;
        int y = p.y;
        int x1 = getX1(), x2 = getX2();
        int y1 = getY1(), y2 = getY2();
        int h = (x1 + x2) / 2, k = (y1 + y2) / 2;
        int a = Math.abs(x2 - x1) / 2, b = Math.abs(y2 - y1) / 2;
        double m1 = Math.pow((x - h) / a, 2), m2 = Math.pow((y - k) / b, 2);
        double equ = m1 + m2;
        if (equ < 1) return true; else return false;
    }
