    private void renderTriangleEdge() {
        if (!g3d.checkTranslucent(false)) return;
        int mag2d = (int) Math.sqrt(dx * dx + dy * dy);
        int wideWidthPixels = width * 9;
        int dxWide, dyWide;
        if (mag2d == 0) {
            dxWide = 0;
            dyWide = wideWidthPixels;
        } else {
            dxWide = wideWidthPixels / 3 * -dy / mag2d;
            dyWide = wideWidthPixels / 3 * dx / mag2d;
        }
        int xWideUp = xA + dxWide / 2;
        int xWideDn = xWideUp - dxWide;
        int yWideUp = yA + dyWide / 2;
        int yWideDn = yWideUp - dyWide;
        g3d.setColix(colixA);
        if (colixA == colixB) {
            g3d.drawfillTriangle(xB, yB, zB, xWideUp, yWideUp, zA, xWideDn, yWideDn, zA);
        } else {
            int xMidUp = (xB + xWideUp) / 2;
            int yMidUp = (yB + yWideUp) / 2;
            int zMid = (zB + zA) / 2;
            int xMidDn = (xB + xWideDn) / 2;
            int yMidDn = (yB + yWideDn) / 2;
            g3d.drawfillTriangle(xB, yB, zB, xMidUp, yMidUp, zMid, xMidDn, yMidDn, zMid);
            g3d.setColix(colixA);
            g3d.drawfillTriangle(xMidUp, yMidUp, zMid, xMidDn, yMidDn, zMid, xWideDn, yWideDn, zA);
            g3d.drawfillTriangle(xMidUp, yMidUp, zMid, xWideUp, yWideUp, zA, xWideDn, yWideDn, zA);
        }
    }
