    protected void drawConnector(Graphics g, int index, int dx, int dy, Node node) {
        if (node == null) return;
        int level = node.getLevel() - 1;
        Node prev = node.getPreviousSibling();
        Node next = node.getNextSibling();
        int x = dx;
        if (level == 0) x += w1 / 2; else if (level == 1) x += w1 + hline + gap + w2 / 2; else x += w1 + hline + gap + w2 / 2 + (w1 / 2 + hline + gap + w2 / 2 + 1) * (level - 1);
        int ystart;
        int yend;
        if (level == 0 && next == null) {
            if (prev != null && items.items[index] == node) {
                ystart = dy - (fmH - h1) / 2;
                yend = dy + (fmH - h1) / 2;
                g.drawLine(x, ystart, x, yend);
            }
        }
        if (node.isLeaf(allowsChildren) || node.getChildCount() == 0) {
            ystart = dy - (fmH - h2) / 2;
            yend = dy + (fmH / 2);
            g.drawLine(x, ystart, x, yend);
            if (next != null) {
                ystart = yend;
                yend += (fmH / 2);
                g.drawLine(x, ystart, x, yend);
            }
        } else {
            if (next == null && node == items.items[index]) {
                ystart = dy - (fmH - h1) / 2;
                yend = dy + (fmH - h1) / 2;
                g.drawLine(x, ystart, x, yend);
            }
            if (next != null) {
                ystart = dy - (fmH - h1) / 2;
                yend = dy + fmH;
                g.drawLine(x, ystart, x, yend);
            }
        }
        drawConnector(g, index, dx, dy, node.getParent());
    }
