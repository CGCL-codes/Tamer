    public static void clip(Line line, Rect rect) {
        double x1 = line.a.x;
        double y1 = line.a.y;
        double x2 = line.b.x;
        double y2 = line.b.y;
        int code1 = encode(x1, y1, rect);
        int code2 = encode(x2, y2, rect);
        if ((code1 | code2) == 0 || (code1 & code2) != 0) return; else if (code1 == 0 || code2 == 0) {
            if (code1 == 0) clip(line, line.a.x, line.a.y, line.b.x, line.b.y, rect, true); else clip(line, line.b.x, line.b.y, line.a.x, line.a.y, rect, false);
        } else {
            double x = (x1 + x2) / 2;
            double y = (y1 + y2) / 2;
            int code = encode(x, y, rect);
            while (code != 0) {
                if ((code1 & code) == 0) {
                    x2 = x;
                    y2 = y;
                    code2 = code;
                    line.b.x = (int) x;
                    line.b.y = (int) y;
                } else {
                    x1 = x;
                    y1 = y;
                    code1 = code;
                    line.a.x = (int) x;
                    line.a.y = (int) y;
                }
                x = (x1 + x2) / 2;
                y = (y1 + y2) / 2;
                code = encode(x, y, rect);
            }
            clip(line, x, y, x1, y1, rect, false);
            clip(line, x, y, x2, y2, rect, true);
        }
    }
