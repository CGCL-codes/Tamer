        private void right2Top() {
            if (x2 > x1 + SEP) {
                if (y2 - SEP > y1) {
                    addXL(x1, y1, x2, y2);
                } else if (toMinX > x1 + 2 * SEP) {
                    double xm1 = (toMinX + x1) / 2;
                    double ym2 = y2 - SEP;
                    double ym1 = (2 * ym2 + y1) / 3.0;
                    double xm2 = (xm1 + x2) / 2.0;
                    addXL(x1, y1, xm1, ym1);
                    addYL(xm1, ym1, xm2, ym2);
                    addXL(xm2, y2 - SEP, x2, y2);
                } else if (x2 > x1) {
                    double ym = (fromMaxY + toMinY) * 0.5;
                    double xm = (fromMaxX + toMinX) * 0.5;
                    addXU(x1, y1, xm, ym, false);
                    addXU(xm, ym, toMinX, y2 - SEP, true);
                    addXL(x1, y2 - SEP, x2, y2);
                }
            } else if (y2 - SEP < fromMaxY + SEP) {
                double xm1 = Math.max(fromMaxX, toMaxX) + 2 * SEP;
                double ym1 = Math.min((y1 + y2) * 0.5, fromMinY);
                double xm2 = Math.max((x1 + x2) * 0.5, toMaxX);
                double ym2 = Math.min(fromMinY, toMinY) - 2 * SEP;
                addXL(x1, y1, xm1, ym1);
                addYL(xm1, ym1, xm2, ym2);
                addXL(xm2, ym2, x2, y2);
            } else {
                double ym1 = (2.0 * fromMinY + 1.0 * toMaxY) / 3.0;
                double ym2 = (ym1 + y2) / 2.0;
                double xm1 = fromMaxX + 2 * SEP;
                double xm2 = (xm1 + x2) / 2.0;
                addXL(x1, y1, xm1, ym1);
                addYL(xm1, ym1, xm2, ym2);
                addXL(xm2, ym2, x2, y2);
            }
        }
