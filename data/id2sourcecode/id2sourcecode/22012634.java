    public void mouseDragged(ViewPanel v, int mod, int buttonNumber, int jpx, int jpy, MouseEvent e) {
        if (buttonNumber == 3) {
            x2 = v.getMouse().vx;
            y2 = v.getMouse().vy;
            nwx = Math.min(x1, x2);
            nwy = Math.max(y1, y2);
            sex = Math.max(x1, x2);
            sey = Math.min(y1, y2);
            selectionRect.vx = (nwx + sex) / 2;
            selectionRect.vy = (nwy + sey) / 2;
            selectionRect.setWidth((sex - nwx) / 2);
            selectionRect.setHeight((nwy - sey) / 2);
        }
    }
