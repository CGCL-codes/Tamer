    private void paintAngleLine(Graphics g, DisplayControl control) {
        int x1 = atom1.getScreenX(), y1 = atom1.getScreenY();
        int x2 = atom2.getScreenX(), y2 = atom2.getScreenY();
        int x3 = atom3.getScreenX(), y3 = atom3.getScreenY();
        int xa = (x1 + x2) / 2;
        int ya = (y1 + y2) / 2;
        int xb = (x3 + x2) / 2;
        int yb = (y3 + y2) / 2;
        control.maybeDottedStroke(g);
        g.setColor(control.getColorAngle());
        g.drawLine(xa, ya, xb, yb);
    }
