    protected int findCorner(int x, int y) {
        if (hasSelection()) {
            int xc = (x2 + x1) / 2;
            int yc = (y2 + y1) / 2;
            if (cornerMatch(x, y, x1, y1)) return 1;
            if (cornerMatch(x, y, x2, y1) && mode != MODE_LINE) return 2;
            if (cornerMatch(x, y, x2, y2)) return 3;
            if (cornerMatch(x, y, x1, y2) && mode != MODE_LINE) return 4;
            if (cornerMatch(x, y, xc, yc)) return 5;
        }
        return 0;
    }
