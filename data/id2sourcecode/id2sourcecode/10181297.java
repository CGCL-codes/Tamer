    private void renderTriangle(Bond bond) {
        int mag2d = (int) Math.sqrt(dx * dx + dy * dy);
        int wideWidthPixels = (int) viewer.scaleToScreen(zB, wideWidthAngstroms);
        int dxWide, dyWide;
        if (mag2d == 0) {
            dxWide = 0;
            dyWide = wideWidthPixels;
        } else {
            dxWide = wideWidthPixels * -dy / mag2d;
            dyWide = wideWidthPixels * dx / mag2d;
        }
        int xWideUp = xB + dxWide / 2;
        int xWideDn = xWideUp - dxWide;
        int yWideUp = yB + dyWide / 2;
        int yWideDn = yWideUp - dyWide;
        if (colixA == colixB) {
            g3d.drawfillTriangle(colixA, xA, yA, zA, xWideUp, yWideUp, zB, xWideDn, yWideDn, zB);
        } else {
            int xMidUp = (xA + xWideUp) / 2;
            int yMidUp = (yA + yWideUp) / 2;
            int zMid = (zA + zB) / 2;
            int xMidDn = (xA + xWideDn) / 2;
            int yMidDn = (yA + yWideDn) / 2;
            g3d.drawfillTriangle(colixA, xA, yA, zA, xMidUp, yMidUp, zMid, xMidDn, yMidDn, zMid);
            g3d.drawfillTriangle(colixB, xMidUp, yMidUp, zMid, xMidDn, yMidDn, zMid, xWideDn, yWideDn, zB);
            g3d.drawfillTriangle(colixB, xMidUp, yMidUp, zMid, xWideUp, yWideUp, zB, xWideDn, yWideDn, zB);
        }
    }
