    public static int rectCrossingsForQuad(int crossings, double rxmin, double rymin, double rxmax, double rymax, double x0, double y0, double xc, double yc, double x1, double y1, int level) {
        if (y0 >= rymax && yc >= rymax && y1 >= rymax) return crossings;
        if (y0 <= rymin && yc <= rymin && y1 <= rymin) return crossings;
        if (x0 <= rxmin && xc <= rxmin && x1 <= rxmin) return crossings;
        if (x0 >= rxmax && xc >= rxmax && x1 >= rxmax) {
            if (y0 < y1) {
                if (y0 <= rymin && y1 > rymin) crossings++;
                if (y0 < rymax && y1 >= rymax) crossings++;
            } else if (y1 < y0) {
                if (y1 <= rymin && y0 > rymin) crossings--;
                if (y1 < rymax && y0 >= rymax) crossings--;
            }
            return crossings;
        }
        if ((x0 < rxmax && x0 > rxmin && y0 < rymax && y0 > rymin) || (x1 < rxmax && x1 > rxmin && y1 < rymax && y1 > rymin)) {
            return RECT_INTERSECTS;
        }
        if (level > 52) {
            return rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x1, y1);
        }
        double x0c = (x0 + xc) / 2;
        double y0c = (y0 + yc) / 2;
        double xc1 = (xc + x1) / 2;
        double yc1 = (yc + y1) / 2;
        xc = (x0c + xc1) / 2;
        yc = (y0c + yc1) / 2;
        if (Double.isNaN(xc) || Double.isNaN(yc)) {
            return 0;
        }
        crossings = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, x0, y0, x0c, y0c, xc, yc, level + 1);
        if (crossings != RECT_INTERSECTS) {
            crossings = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, xc, yc, xc1, yc1, x1, y1, level + 1);
        }
        return crossings;
    }
