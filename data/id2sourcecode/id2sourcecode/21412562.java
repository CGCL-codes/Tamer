    protected void drawBackgroundSlice(final Graphics2D g2, final float pos, final int x1, final int y1, final int x2, final int y2, final int absPos, final SidebarEdge edge, final float v) {
        g2.setColor(Color.white);
        g2.drawLine(x1, y1, x2, y2);
        final int xm = (x1 + x2) / 2;
        final int ym = (y1 + y2) / 2;
        int across = Math.max(Math.abs(xm - x1), Math.abs(ym - y1));
        int s = (int) (across * pos);
        int dx = edge.isHorizontal() ? 1 : 0;
        int dy = edge.isHorizontal() ? 0 : 1;
        int x1_ = x1 + dx * s;
        int y1_ = y1 + dy * s;
        int x2_ = x2 - dx * s;
        int y2_ = y2 - dy * s;
        g2.setColor(Color.black);
        g2.drawLine(x1_, y1_, x2_, y2_);
    }
