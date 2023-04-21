    public void press3(ViewPanel v, int mod, int jpx, int jpy, MouseEvent e) {
        if (!application.sessionStarted) {
            return;
        }
        if (application.noLocationSpecifiedYet) {
            selectionRect.setVisible(true);
        }
        x1 = v.getMouse().vx;
        y1 = v.getMouse().vy;
        x2 = x1 + 1;
        y2 = y1 + 1;
        nwx = Math.min(x1, x2);
        nwy = Math.max(y1, y2);
        sex = Math.max(x1, x2);
        sey = Math.min(y1, y2);
        selectionRect.vx = (nwx + sex) / 2;
        selectionRect.vy = (nwy + sey) / 2;
        selectionRect.setWidth((sex - nwx) / 2);
        selectionRect.setHeight((nwy - sey) / 2);
    }
