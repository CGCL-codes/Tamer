    public static Coordinate[] rectangleFromGeometry(Geometry rectangle, int sideOne) {
        if ((rectangle.getNumGeometries() > 1) || (rectangle instanceof MultiPolygon)) rectangle = rectangle.getGeometryN(0);
        Coordinate[] p;
        if (rectangle instanceof Polygon) {
            p = ((Polygon) rectangle).getExteriorRing().getCoordinates();
        } else p = rectangle.getCoordinates();
        if (!(p.length == 5)) return null;
        if (sideOne != 1) {
            sideOne = Math.max(1, Math.min(4, sideOne)) - 1;
            for (int j = 0; j < sideOne; j++) {
                int n = p.length - 2;
                Coordinate t = p[0];
                for (int i = 0; i < n; i++) {
                    p[i] = p[i + 1];
                }
                p[n] = t;
            }
            p[p.length - 1] = p[0];
        }
        Coordinate p2 = perpendicularVector(p[1], p[0], p[1].distance(p[2]), true);
        Coordinate p3 = perpendicularVector(p2, p[1], p[0].distance(p[1]), true);
        Coordinate[] rectangleCoords = { p[0], p[1], p2, p3, p[0] };
        return rectangleCoords;
    }
