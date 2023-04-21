    private void drawImage(Image image, int w, int h) {
        Graphics g = image.getGraphics();
        g.setColor(bg);
        g.fillRect(0, 0, w, h);
        int fonth = fm.getHeight();
        int scale = (int) ((h - 2 * fonth) / dmax);
        int xl;
        if (doScale) {
            xl = w / 2 - w / 8;
        } else {
            xl = w / 8;
        }
        int xr = w - w / 8;
        int yt = fonth;
        int yb = fonth + (int) (scale * dmax);
        int levelw = xr - xl;
        int levelh = yb - yt;
        g.setColor(lbg);
        g.fillRect(xl, yt, levelw, levelh);
        double dvalue = 0.0;
        if (value != 0.0) {
            dvalue = Math.log(value) / Math.log(10);
        }
        int l = (int) (scale * dvalue);
        g.setColor(lfg);
        g.fillRect(xl + 2, yb - l, levelw - 4, l);
        g.setColor(fg);
        if (doScale) {
            drawScale(g, xl, xr, yt, yb, scale);
        }
        int sw = fm.stringWidth(value + " B/s");
        int p = xl + (levelw - sw) / 2;
        g.drawString(value + " B/s", p, yb - l - 2);
    }
