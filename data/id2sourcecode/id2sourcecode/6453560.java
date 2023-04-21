    private int[][] compute() {
        int x0, y0, x1, y1, x2, y2, x3, y3;
        int o0, o1;
        updateXYO();
        x0 = xbegin_;
        y0 = ybegin_;
        o0 = obegin_;
        x1 = xend_;
        y1 = yend_;
        o1 = oend_;
        x2 = x3 = (x0 + x1) / 2;
        y2 = y3 = (y0 + y1) / 2;
        if ((o0 == NORTH) || (o0 == SOUTH)) x2 = x0;
        if ((o0 == EAST) || (o0 == WEST)) y2 = y0;
        if ((o1 == NORTH) || (o1 == SOUTH)) x3 = x1;
        if ((o1 == EAST) || (o1 == WEST)) y3 = y1;
        if ((o0 == NORTH) && (o1 == NORTH)) y3 = y2 = Math.min(y0 - 2 * deltaY, y1 - 2 * deltaY);
        if ((o0 == SOUTH) && (o1 == SOUTH)) y3 = y2 = Math.max(y0 + 2 * deltaY, y1 + 2 * deltaY);
        if ((o0 == EAST) && (o1 == EAST)) x3 = x2 = Math.max(x0 + 2 * deltaX, x1 + 2 * deltaX);
        if ((o0 == WEST) && (o1 == WEST)) x3 = x2 = Math.min(x0 - 2 * deltaX, x1 - 2 * deltaX);
        if ((o0 == NORTH) && (o1 == WEST)) {
            x3 = x2;
            y2 = y3;
        }
        if ((o0 == NORTH) && (o1 == EAST)) {
            x3 = x2;
            y2 = y3;
        }
        if ((o0 == SOUTH) && (o1 == WEST)) {
            x3 = x2;
            y2 = y3;
        }
        if ((o0 == SOUTH) && (o1 == EAST)) {
            x3 = x2;
            y2 = y3;
        }
        if ((o0 == EAST) && (o1 == NORTH)) {
            x2 = x3;
            y3 = y2;
        }
        if ((o0 == WEST) && (o1 == NORTH)) {
            x2 = x3;
            y3 = y2;
        }
        if ((o0 == EAST) && (o1 == SOUTH)) {
            x2 = x3;
            y3 = y2;
        }
        if ((o0 == WEST) && (o1 == SOUTH)) {
            x2 = x3;
            y3 = y2;
        }
        int rounded = 0;
        try {
            rounded = Integer.parseInt(getProperty("arrondi"));
        } catch (Exception ex) {
        }
        if ((rounded > 0) && (x2 == x3) && (y2 == y3)) {
            if (x0 == x2) {
                x3 += (x1 > x0 ? rounded : -rounded);
                y2 += (y0 > y1 ? rounded : -rounded);
            } else {
                y3 += (y1 > y0 ? rounded : -rounded);
                x2 += (x0 > x1 ? rounded : -rounded);
            }
        }
        return new int[][] { { x0, x1, x2, x3, o0 }, { y0, y1, y2, y3, o1 } };
    }
