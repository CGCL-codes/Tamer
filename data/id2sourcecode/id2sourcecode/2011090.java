    public void paint(Graphics g, TGPanel tgPanel) {
        if (!intersects(tgPanel.getSize())) {
            return;
        }
        Stroke old = null;
        if (highlighted) {
            old = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(Edge.HIGHLIGHT_WIDTH));
        }
        int x1 = (int) getFromDrawX();
        int y1 = (int) getFromDrawY();
        int x2 = (int) getToDrawX();
        int y2 = (int) getToDrawY();
        boolean isOver = tgPanel.getMouseOverE() == this;
        g.setColor((highlighted) ? HIGHLIGHT_COLOR : (isOver) ? MOUSE_OVER_COLOR : col);
        paintToEnd(g, x1, y1, x2, y2);
        paintFromEnd(g, x1, y1, x2, y2);
        if (isOver) {
            int x3;
            int y3;
            Point point = getOverPoint();
            if (point != null && Math.abs(y1 - y2) > 40) {
                y3 = (int) point.getY() - 5;
                x3 = x1 - (x1 - x2) * (y1 - y3) / (y1 - y2);
            } else {
                x3 = (x1 + x2) / 2;
                y3 = (y1 + y2) / 2;
            }
            y3 += 3;
            String desc = getDescription();
            if (desc != null) {
                g.setColor(Color.black);
                g.drawString(desc, x3 + 6, y3);
            }
            if (isDependency()) {
                Font oldFont = g.getFont();
                g.setFont(Node.SMALL_TAG_FONT);
                g.setColor(col);
                final String str = Integer.toString(this.numberOfDeps);
                final int stringWidth = g.getFontMetrics().stringWidth(str);
                final int ovalWidth = stringWidth + 5;
                g.fillOval(x3 - ovalWidth / 2, y3 - 9, ovalWidth, 11);
                g.setColor(Color.white);
                g.drawString(str, x3 - stringWidth / 2, y3);
                g.setFont(oldFont);
            }
        }
        if (old != null) {
            ((Graphics2D) g).setStroke(old);
        }
    }
