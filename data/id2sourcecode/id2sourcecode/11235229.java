    private int draw(Graphics g, int h, int x, int ystart, Cell c, Hashtable cHash, int rootStart) {
        iCellsDrawn.add(c);
        boolean done = false;
        int lastTime = c.iEndTime;
        int lateTime = iLateTime;
        if (c.iEndTime > lateTime) {
            done = true;
            lastTime = lateTime;
        }
        int length = (int) ((lastTime - c.iTimeIndex) * ysc + .5);
        c.yStartUse = (int) ((c.iTimeIndex - iTimeIndex) * ysc) + START1;
        if (c.getChildCount() == 0 || done) {
            if (x < iXmax) x = iXmax + xsc;
            drawColoredLine(g, c, x, c.yStartUse, x, c.yStartUse + length);
            g.setColor(Color.black);
            drawRotatedText(g, c.getName(), x, c.yStartUse + length + 5, Math.PI / 2);
            if (x > iXmax) iXmax = x;
            c.xUse = x;
            fillInHash(c, cHash);
            g.fillOval(c.xUse - 2, c.yStartUse - 2, 4, 4);
            return x;
        } else {
            Cell cLeft = (Cell) c.getChildAt(0);
            Cell cRite = (Cell) c.getChildAt(1);
            int nl = cLeft.getChildCount() / 2;
            if (nl == 0) nl = 1;
            int nr = cRite.getChildCount() + 1;
            int x1 = draw(g, h, x, yStartUse + length, cLeft, cHash, rootStart);
            cLeft.xUse = x1;
            if (!isLeaf(cLeft)) {
                g.fillOval(cLeft.xUse - 2, cLeft.yStartUse - 2, 4, 4);
                fillInHash(cLeft, cHash);
                drawRotatedText(g, cLeft.getName(), cLeft.xUse, cLeft.yStartUse - 5, -Math.PI / 8);
            }
            int xx = x1 + xsc * nl;
            int x2 = draw(g, h, xx, yStartUse + length, cRite, cHash, rootStart);
            cRite.xUse = x2;
            if (!isLeaf(cRite)) {
                g.fillOval(cRite.xUse - 2, cRite.yStartUse - 2, 4, 4);
                fillInHash(cRite, cHash);
                drawRotatedText(g, cRite.getName(), cRite.xUse, cRite.yStartUse - 5, -Math.PI / 8);
            }
            drawColoredLine(g, c, cLeft.xUse, cLeft.yStartUse, cRite.xUse, cRite.yStartUse);
            x = (x1 + x2) / 2;
            drawColoredLine(g, c, x, c.yStartUse, x, cLeft.yStartUse);
            return x;
        }
    }
