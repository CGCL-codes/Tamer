    protected void calculate() {
        polygon.reset();
        mps = new ArrayList<Point>();
        for (int i = 0; i < n; i++) {
            double angle1 = Math.toRadians(i * 360 / n + startangle);
            double angle2 = Math.toRadians((i + 1) * 360 / n + startangle);
            Vector2D OA = new Vector2D(x + length * Math.cos(angle1), y + length * Math.sin(angle1));
            Vector2D OB = new Vector2D(x + length * Math.cos(angle2), y + length * Math.sin(angle2));
            Vector2D OC = Vector2D.mitte(OA, OB);
            Vector2D AB = Vector2D.strecke(OA, OB);
            Vector2D nAB = AB.normal();
            Vector2D nAB0 = nAB.einheit();
            Vector2D CD = nAB0.verlaengern(height);
            Vector2D OD = Vector2D.add(OC, CD);
            polygon.addPoint(OA.toPoint().x, OA.toPoint().y);
            polygon.addPoint(OD.toPoint().x, OD.toPoint().y);
            mps.add(OD.toPoint());
        }
        minx = maxx = polygon.xpoints[0];
        miny = maxy = polygon.ypoints[0];
        for (int i = 1; i < polygon.npoints; i++) {
            if (minx > polygon.xpoints[i]) minx = polygon.xpoints[i];
            if (maxx < polygon.xpoints[i]) maxx = polygon.xpoints[i];
            if (miny > polygon.ypoints[i]) miny = polygon.ypoints[i];
            if (maxy < polygon.ypoints[i]) maxy = polygon.ypoints[i];
        }
        S.x = (maxx + minx) / 2;
        S.y = (maxy + miny) / 2;
    }
