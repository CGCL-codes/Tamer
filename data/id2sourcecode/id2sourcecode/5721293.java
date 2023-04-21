    public int distance(int x0, int y0, int x1, int y1) {
        int d = 0;
        if (getTileShape() == SQUARE) {
            int x = Math.abs(x0 - x1);
            int y = Math.abs(y0 - y1);
            d = x + Math.abs(x - y) + y;
            d = (d + 1) / 2;
        } else if (getTileShape() == HEXAGONAL) {
            y0 = y0 - (x0) / 2;
            y1 = y1 - (x1) / 2;
            if (x1 <= x0) {
                int x = x0;
                int y = y0;
                x0 = x1;
                y0 = y1;
                x1 = x;
                y1 = y;
            }
            if (y1 > y0) {
                d = x1 - x0 + y1 - y0;
            } else if (x0 + y0 > x1 + y1) {
                d = y0 - y1;
            } else {
                d = x1 - x0;
            }
        }
        return d;
    }
