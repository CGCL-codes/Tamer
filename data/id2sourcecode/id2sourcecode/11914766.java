    protected void calculate() {
        polygon.reset();
        for (int i = 0; i < n; i++) {
            if (n == null) {
                System.out.println("lala1");
            }
            if (startangle == null) {
                System.out.println("lala2");
            }
            double angle = Math.toRadians(i * 360 / n + startangle);
            polygon.addPoint((int) (x + length * Math.cos(angle)), (int) (y + length * Math.sin(angle)));
        }
        minx = maxx = polygon.xpoints[0];
        miny = maxy = polygon.ypoints[0];
        for (int i = 1; i < n; i++) {
            if (minx > polygon.xpoints[i]) minx = polygon.xpoints[i];
            if (maxx < polygon.xpoints[i]) maxx = polygon.xpoints[i];
            if (miny > polygon.ypoints[i]) miny = polygon.ypoints[i];
            if (maxy < polygon.ypoints[i]) maxy = polygon.ypoints[i];
        }
        S.x = (maxx + minx) / 2;
        S.y = (maxy + miny) / 2;
    }
