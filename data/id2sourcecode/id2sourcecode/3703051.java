    private void drawAnchorBox(Graphics2D g2) {
        Rectangle r = getAnchorRect();
        int x = r.x;
        int y = r.y;
        int width = r.x + r.width - 6;
        int height = r.y + r.height - 6;
        int centerx = (x + width) / 2;
        int centery = (y + height) / 2;
        Vector<Point> selPts = new Vector<Point>();
        selPts.add(new Point(centerx, centery));
        VGArrow vgArrow = (VGArrow) refBase;
        int quart = vgArrow.getArrowHeadQuarter();
        if (quart == 0 | quart == 4) {
            selPts.add(new Point(x, y));
            selPts.add(new Point(width, height));
        } else if (quart == 2 | quart == 6) {
            selPts.add(new Point(x, height));
            selPts.add(new Point(width, y));
        }
        if (quart == 1 | quart == 5) {
            selPts.add(new Point(x, height / 2));
            selPts.add(new Point(width, height / 2));
        }
        if (quart == 3 | quart == 7) {
            selPts.add(new Point(width / 2, 0));
            selPts.add(new Point(width / 2, height));
        }
        g2.setColor(Color.green);
        for (Point kPkt : selPts) {
            g2.fillRect(kPkt.x, kPkt.y, 6, 6);
        }
    }
