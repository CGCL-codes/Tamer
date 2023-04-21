        private void addXU(double x1, double y1, double x2, double y2, boolean left) {
            double xm = left ? min(x1, x2) - SEP * 2 : max(x1, x2) + SEP * 2;
            double ym = (y1 + y2) / 2;
            double f = 0.8;
            addPoint(new Point2D.Double(xm * f + x1 * (1 - f), y1));
            addPoint(new Point2D.Double(xm, y1 * f + ym * (1 - f)));
            addPoint(new Point2D.Double(xm, ym));
            addPoint(new Point2D.Double(xm, y2 * f + ym * (1 - f)));
            addPoint(new Point2D.Double(xm * f + x2 * (1 - f), y2));
            addPoint(new Point2D.Double(x2, y2));
        }
