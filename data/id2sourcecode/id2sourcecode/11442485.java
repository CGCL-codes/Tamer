    void drawLargeCircleArc(final double x, final double y, final double r, final double a, final double b) {
        final double dw = Math.sqrt((W + H) / r / 10);
        double w = a;
        double x0 = x + r * Math.cos(w / 180 * Math.PI);
        double y0 = y - r * Math.sin(w / 180 * Math.PI);
        w = w + dw;
        while (w < a + b + dw) {
            if (w > a + b) {
                w = a + b;
            }
            final double x1 = x + r * Math.cos(w / 180 * Math.PI);
            final double y1 = y - r * Math.sin(w / 180 * Math.PI);
            final double dx = (x0 + x1) / 2, dy = (y0 + y1) / 2;
            if (Math.sqrt(dx * dx + dy * dy) <= 10 * (W + H)) {
                G.draw(new Line2D.Double(x0, y0, x1, y1));
            }
            x0 = x1;
            y0 = y1;
            w += dw;
        }
    }
