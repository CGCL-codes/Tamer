    private void renderBond() {
        dx = x2 - x1;
        dx2 = dx * dx;
        dy = y2 - y1;
        dy2 = dy * dy;
        dz = z2 - z1;
        dz2 = dz * dz;
        mag2d2 = dx2 + dy2;
        mag3d2 = mag2d2 + dz2;
        if (mag2d2 <= 2 || mag2d2 <= 49 && fastRendering) return;
        if (showAtoms && (mag2d2 <= 16)) return;
        if (!showAtoms && bondOrder == 1 && (fastRendering || styleBond == control.WIREFRAME)) {
            g.setColor(color1);
            if (sameColor) {
                drawLineInside(g, x1, y1, x2, y2);
            } else {
                int xMid = (x1 + x2) / 2;
                int yMid = (y1 + y2) / 2;
                drawLineInside(g, x1, y1, xMid, yMid);
                g.setColor(color2);
                drawLineInside(g, xMid, yMid, x2, y2);
            }
            return;
        }
        radius1 = diameter1 >> 1;
        radius2 = diameter2 >> 1;
        mag2d = (int) Math.sqrt(mag2d2);
        if (radius1 >= mag2d) return;
        halfMag2d = mag2d / 2;
        mag3d = (int) Math.sqrt(mag3d2);
        int radius1Bond = radius1 * mag2d / mag3d;
        int radius2Bond = radius2 * mag2d / mag3d;
        outline1 = control.getColorAtomOutline(styleBond, color1);
        outline2 = control.getColorAtomOutline(styleBond, color2);
        this.bondOrder = bondOrder;
        boolean lineBond = styleBond == control.WIREFRAME || fastRendering;
        if (!lineBond && width1 < 2) {
            color1 = outline1;
            color2 = outline2;
            lineBond = true;
        }
        resetAxisCoordinates(lineBond);
        while (true) {
            if (lineBond) lineBond(); else polyBond(styleBond);
            if (--bondOrder == 0) break;
            stepAxisCoordinates();
        }
        if (showAxis) {
            g.setColor(control.transparentGreen());
            g.drawLine(x1 + 5, y1, x1 - 5, y1);
            g.drawLine(x1, y1 + 5, x1, y1 - 5);
            g.drawOval(x1 - 5, y1 - 5, 10, 10);
            g.drawLine(x2 + 5, y2, x2 - 5, y2);
            g.drawLine(x2, y2 + 5, x2, y2 - 5);
            g.drawOval(x2 - 5, y2 - 5, 10, 10);
        }
    }
