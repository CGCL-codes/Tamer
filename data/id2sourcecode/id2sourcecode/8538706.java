    public static Polygon computeHexagon(Rectangle r) {
        Polygon poly = new Polygon();
        int delta = Math.min(15, r.width / 4);
        final int x1 = r.x, x2 = r.x + delta, x3 = r.x + r.width - delta, x4 = r.x + r.width, y1 = r.y, y3 = r.y + r.height, y2 = (y1 + y3) / 2;
        poly.addPoint(x1, y2);
        poly.addPoint(x2, y1);
        poly.addPoint(x3, y1);
        poly.addPoint(x4, y2);
        poly.addPoint(x3, y3);
        poly.addPoint(x2, y3);
        return poly;
    }
