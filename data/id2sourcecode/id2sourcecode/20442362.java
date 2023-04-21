    public GPoint getCenter() {
        GPoint first = vertices.get(0);
        double xMin = first.getX(), xMax = first.getX(), yMin = first.getY(), yMax = first.getY();
        for (int i = 1; i < vertices.size(); i++) {
            GPoint v = vertices.get(i);
            xMin = Math.min(xMin, v.getX());
            xMax = Math.max(xMax, v.getX());
            yMin = Math.min(yMin, v.getY());
            yMax = Math.max(yMax, v.getX());
        }
        double xc = (xMin + xMax) / 2, yc = (yMin + yMax) / 2;
        return new GPoint(xc, yc);
    }
