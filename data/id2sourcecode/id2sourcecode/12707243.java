    private void drawAnchorBox(Graphics2D g2) {
        Rectangle r = getAnchorRect();
        int x = r.x;
        int y = r.y;
        int width = r.x + r.width - 6;
        int height = r.y + r.height - 6;
        int centerx = (x + width) / 2;
        int centery = (y + height) / 2;
        Vector<Point> selPts = new Vector<Point>();
        selPts.add(new Point(x, y));
        selPts.add(new Point(x, height));
        selPts.add(new Point(width, y));
        selPts.add(new Point(width, height));
        selPts.add(new Point(centerx, y));
        selPts.add(new Point(centerx, height));
        selPts.add(new Point(x, centery));
        selPts.add(new Point(width, centery));
        g2.setColor(Color.green);
        for (Point kPkt : selPts) {
            g2.fillRect(kPkt.x, kPkt.y, 6, 6);
        }
    }
