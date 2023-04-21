    private void drawTransparentBackgroundGridPattern(final Graphics2D g2, final int x1, final int y1, final int x2, final int y2, final int absPos) {
        final int xm = (x1 + x2) / 2;
        final int ym = (y1 + y2) / 2;
        Color c1 = CHECKERS_BRIGHT;
        Color c2 = CHECKERS_DARK;
        if (isCheckboardPatternInverted(x1, y1, absPos, xm, ym)) {
            c1 = CHECKERS_DARK;
            c2 = CHECKERS_BRIGHT;
        }
        g2.setColor(c1);
        g2.drawLine(x1, y1, xm, ym);
        g2.setColor(c2);
        g2.drawLine(xm, ym, x2, y2);
    }
