    public Image getStamp() {
        dx1 = 0;
        dy1 = 0;
        int hs = 0;
        int ws = 0;
        dx2 = 0;
        dy2 = 0;
        if (w >= h) {
            hs = stamp * h / w;
            ws = stamp;
            dy1 = (stamp - hs) / 2;
            dy2 = (stamp + hs) / 2;
            dx2 = stamp;
        } else if (h > w) {
            ws = stamp * w / h;
            hs = stamp;
            dx1 = (stamp - ws) / 2;
            dx2 = (stamp + ws) / 2;
            dy2 = stamp;
        }
        Image scaled = c.createImage(stamp, stamp);
        Graphics g = scaled.getGraphics();
        g.drawImage(image, dx1, dy1, dx2, dy2, 0, 0, w, h, c);
        g.setColor(c.getBackground());
        new Rectang(stamp).paintInside(g);
        return scaled;
    }
