    protected void paintRings(Graphics g, final int xCenter, final int yCenter, final int radiusInnerRim, final int radiusOuterRim, final int symmetry, final float phase, final float twist, final float angle) {
        final int radiusMiddle = (radiusInnerRim + radiusOuterRim) / 2;
        final int diameterRing = radiusOuterRim - radiusInnerRim;
        final int radiusRing = diameterRing / 2;
        for (double i = 0; i < symmetry; i++) {
            final double phi = 2 * Math.PI * ((i + phase) / symmetry + angle);
            final int x0 = xCenter + (int) (radiusMiddle * Math.sin(phi)) - radiusRing;
            final int y0 = yCenter - (int) (radiusMiddle * Math.cos(phi)) - radiusRing;
            g.drawOval(x0, y0, diameterRing, diameterRing);
        }
    }
