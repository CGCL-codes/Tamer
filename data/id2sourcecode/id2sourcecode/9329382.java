    protected void drawArrow(Graphics g, int x, int y, int x2, int y2, boolean isForward) {
        int mx, my, aX1, aX2, aY1, aY2;
        mx = (x + x2) / 2;
        my = (y + y2) / 2;
        if (x2 - x == 0) {
            if (isForward) {
                aX1 = x - 5;
                aX2 = x + 5;
                my += 5;
                aY1 = my;
                aY2 = my;
            } else {
                aX1 = x - 5;
                aX2 = x + 5;
                my -= 5;
                aY1 = my;
                aY2 = my;
            }
        } else if (y2 - y == 0) {
            if (isForward) {
                mx += 5;
                aX1 = mx;
                aX2 = mx;
                aY1 = y + 5;
                aY2 = y - 5;
            } else {
                mx -= 5;
                aX1 = mx;
                aX2 = mx;
                aY1 = y + 5;
                aY2 = y - 5;
            }
        } else {
            double delta = (double) (y2 - y) / (double) (x2 - x);
            double nDelta = -1.0 / delta;
            double dX = 10;
            double nDX = 20 / Math.sqrt(nDelta * nDelta + 1);
            aX1 = (int) (mx + nDX);
            aY1 = (int) (nDelta * (aX1 - mx) + my);
            aX2 = (int) (mx - nDX);
            aY2 = (int) (nDelta * (aX2 - mx) + my);
            if (isForward) {
                if (x2 > x) {
                    mx += dX;
                    if (mx > x2) mx = x2;
                } else {
                    mx -= dX;
                    if (mx < x2) mx = x2;
                }
            } else {
                if (x2 > x) {
                    mx -= dX;
                    if (mx < x) mx = x;
                } else {
                    mx += dX;
                    if (mx > x) mx = x;
                }
            }
            my = (int) (delta * (mx - x) + y);
        }
        g.drawLine(mx, my, aX1, aY1);
        g.drawLine(mx, my, aX2, aY2);
    }
