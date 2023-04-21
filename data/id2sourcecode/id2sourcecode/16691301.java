    static void clip(Line line, double x1, double x2, double y1, double y2, Rect rect, boolean flag) {
        double x = (x1 + x2) / 2;
        double y = (y1 + y2) / 2;
        int code = encode(x, y, rect);
        while (Math.abs(x - x1) > EPS || Math.abs(y - y1) > EPS) {
            if (code == 0) {
                x1 = x;
                y1 = y;
            } else {
                x2 = x;
                y2 = y;
            }
            x = (x1 + x2) / 2;
            y = (y1 + y2) / 2;
            code = encode(x, y, rect);
        }
        if (flag) {
            line.b.x = (int) x;
            line.b.y = (int) y;
        } else {
            line.a.x = (int) x;
            line.a.y = (int) y;
        }
    }
