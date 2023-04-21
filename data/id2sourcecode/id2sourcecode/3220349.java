    public void paintInner(Graphics g) {
        try {
            Rectangle b = getBounds();
            g.setColor(Color.white);
            g.fillRect(hTrans, vTrans, b.width, b.height);
            g.setColor(Color.black);
            FontMetrics fm = g.getFontMetrics();
            int ascent = fm.getMaxAscent();
            int newRowHeight = fm.getMaxDescent() + ascent + 2;
            if (rowHeight != newRowHeight) {
                rowHeight = newRowHeight;
                getVScroll(1, 1).setBlockIncrement(rowHeight);
            }
            int leftMargin = 2;
            int width = b.width - 4;
            int offset = 8 + leftMargin;
            int y = leftMargin;
            for (int i = 0; i < users.size(); i++) {
                ChatUser s = (ChatUser) users.elementAt(i);
                if (i == selectedIdx) {
                    g.fillRect(offset + 2, y, width, rowHeight);
                    g.setColor(Color.white);
                    g.drawString(s.getName(), offset + 4, y + ascent);
                    g.setColor(Color.black);
                } else {
                    g.drawString(s.getName(), offset + 4, y + ascent);
                }
                if (s.equals(user)) {
                    int my = y + (rowHeight) / 2;
                    int[] xs = { 0, offset, 0 };
                    int[] ys = { my - offset / 2, my, my + offset / 2 };
                    g.setColor(Color.gray);
                    g.fillPolygon(xs, ys, xs.length);
                    g.setColor(Color.black);
                } else if (s.getIgnored()) {
                    int by = y + (rowHeight - offset) / 2;
                    g.drawLine(leftMargin, by, offset, by + offset);
                    g.drawLine(offset, by, leftMargin, by + offset);
                }
                y = y + rowHeight;
            }
            GuiUtils.paintBorder(g, GuiUtils.BORDER_SUNKEN, b.width, b.height);
            g.setColor(Color.black);
            g.drawLine(offset + 1, 0, offset + 1, 2000);
        } catch (Throwable exc) {
            System.err.println("CAUGHT: " + exc);
        }
    }
