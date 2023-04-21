    @Override
    protected void paintComponent(Graphics g) {
        int cw = getWidth();
        int ch = getHeight();
        Rectangle bounds = getContentArea();
        int bw = bgImg.getWidth(this);
        int bh = bgImg.getHeight(this);
        double ratio = (double) bw / bh;
        int nx, ny, nw, nh;
        nx = bounds.x;
        ny = bounds.y;
        nw = bounds.width;
        nh = bounds.height;
        g.drawImage(bgImg, nx, ny, nw, nh, this);
        if (today != null) {
            ratio = (double) nw / bw;
            Image sImg = today.getStartImage();
            int sw = (int) (sImg.getWidth(this) * ratio);
            int sh = (int) (sImg.getHeight(this) * ratio);
            int sx = nx + (nw / 2 - sw) / 2;
            int sy = ny + (nh - sh) / 2;
            g.drawImage(sImg, sx, sy, sw, sh, this);
            FontMetrics fm = g.getFontMetrics();
            String txt = today.getCity() + " " + today.getDate();
            int x = (cw - fm.stringWidth(txt)) / 2;
            int y = ny + fm.getHeight();
            g.drawString(txt, x, y);
            txt = today.getStatus();
            x = (cw - fm.stringWidth(txt)) / 2;
            y = (ch - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(txt, x, y);
            txt = today.getTemperature();
            x = (cw - fm.stringWidth(txt)) / 2;
            y += fm.getHeight();
            g.drawString(txt, x, y);
            txt = today.getDescription();
            x = (cw - fm.stringWidth(txt)) / 2;
            y += fm.getHeight();
            g.drawString(txt, x, y);
            Image tImg = today.getToImage();
            int tw = (int) (tImg.getWidth(this) * ratio);
            int th = (int) (tImg.getHeight(this) * ratio);
            int tx = nx + (nw / 2 - tw) / 2 + nw / 2;
            int ty = ny + (nh - th) / 2;
            g.drawImage(tImg, tx, ty, tw, th, this);
        } else if (error != null) {
            FontMetrics fm = g.getFontMetrics();
            int x = (cw - fm.stringWidth(error)) / 2;
            int y = bounds.y + fm.getHeight();
            g.drawString(error, x, y);
        }
    }
