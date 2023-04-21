    private void drawControlPoints(Graphics2D g) {
        if (parent.getRow() != DasRow.NULL && parent.getColumn() != DasColumn.NULL) {
            int xLeft = parent.getColumn().getDMinimum();
            int xRight = parent.getColumn().getDMaximum();
            int xMid = (xLeft + xRight) / 2;
            int yTop = parent.getRow().getDMinimum();
            int yBottom = parent.getRow().getDMaximum();
            int yMid = (yTop + yBottom) / 2;
            Graphics2D gg = (Graphics2D) g.create();
            gg.setColor(new Color(0, 0, 0, 255));
            int ss = 9;
            gg.fillRect(xLeft + 1, yTop + 1, ss - 2, ss - 2);
            gg.fillRect(xRight - ss + 1, yTop + 1, ss - 2, ss - 2);
            gg.fillRect(xLeft + 1, yBottom - ss + 1, ss - 2, ss - 2);
            gg.fillRect(xRight - ss + 1, yBottom - ss + 1, ss - 2, ss - 2);
            gg.fillRect(xMid + 1 - ss / 2, yTop + 1, ss - 2, ss - 2);
            gg.fillRect(xRight - ss + 1, yMid + 1 - ss / 2, ss - 2, ss - 2);
            gg.fillRect(xMid + 1 - ss / 2, yBottom - ss + 1, ss - 2, ss - 2);
            gg.fillRect(xLeft + 1, yMid - ss / 2 + 1, ss - 2, ss - 2);
            gg.setColor(new Color(255, 255, 255, 100));
            gg.drawRect(xLeft, yTop, ss, ss);
            gg.drawRect(xRight - ss, yTop, ss, ss);
            gg.drawRect(xLeft, yBottom - ss, ss, ss);
            gg.drawRect(xRight - ss, yBottom - ss, ss, ss);
            gg.drawRect(xMid - ss / 2, yTop + 1, ss, ss);
            gg.drawRect(xRight - ss, yMid - ss / 2, ss, ss);
            gg.drawRect(xMid - ss / 2, yBottom - ss, ss, ss);
            gg.drawRect(xLeft, yMid - ss / 2, ss, ss);
            int xmid = (xLeft + xRight) / 2;
            int ymid = (yTop + yBottom) / 2;
            int rr = 4;
            g.setColor(new Color(255, 255, 255, 100));
            gg.fillOval(xmid - rr - 1, ymid - rr - 1, rr * 2 + 3, rr * 2 + 3);
            gg.setColor(new Color(0, 0, 0, 255));
            gg.drawOval(xmid - rr, ymid - rr, rr * 2, rr * 2);
            gg.fillOval(xmid - 1, ymid - 1, 3, 3);
            gg.dispose();
        }
    }
