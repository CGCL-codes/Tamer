    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int sx = rcStart.x + rcStart.width / 2;
        int sy = rcStart.y + rcStart.height / 2;
        int ex = rcEnd.x + rcEnd.width / 2;
        int ey = rcEnd.y + rcEnd.height / 2;
        int dx = sx - ex;
        int dy = sy - ey;
        int lineLen = (int) Math.sqrt(dx * dx + dy * dy);
        int minW = Math.min(rcStart.width, rcStart.height);
        int minH = Math.min(rcEnd.width, rcEnd.height);
        if (lineLen < (minW + minH) / 2) return;
        calcIntersection(rcStart, sx, sy, ex, ey, ipStart);
        calcIntersection(rcEnd, ex, ey, sx, sy, ipEnd);
        int stImgX = 0;
        int stImgY = 0;
        int enImgX = 0;
        int enImgY = 0;
        if (iStartingShape == SHAPE_NONE) {
            sx = ipStart.x;
            sy = ipStart.y;
        } else {
            stImgX = ipStart.x + (SHAPE_SIZE * ipStart.dX) / 2;
            stImgY = ipStart.y + (SHAPE_SIZE * ipStart.dY) / 2;
            sx = stImgX + SHAPE_SIZE / 2;
            sy = stImgY + SHAPE_SIZE / 2;
        }
        if (iEndingShape == SHAPE_NONE) {
            ex = ipEnd.x;
            ey = ipEnd.y;
        } else {
            enImgX = ipEnd.x + (SHAPE_SIZE * ipEnd.dX) / 2;
            enImgY = ipEnd.y + (SHAPE_SIZE * ipEnd.dY) / 2;
            ex = enImgX + SHAPE_SIZE / 2;
            ey = enImgY + SHAPE_SIZE / 2;
        }
        g.setColor(Color.black);
        if (iLineStyle == STYLE_NORMAL) {
            g.drawLine(sx, sy, ex, ey);
        } else {
            dx = ex - sx;
            dy = ey - sy;
            lineLen = ((int) Math.sqrt(dx * dx + dy * dy)) >> 3;
            float t1;
            float t2;
            float x1;
            float y1;
            float x2;
            float y2;
            Line2D.Float line = new Line2D.Float();
            for (int i = 0; i < lineLen; i++) {
                t1 = i / ((float) lineLen);
                t2 = (i + 1) / ((float) lineLen);
                x1 = sx + t1 * dx;
                y1 = sy + t1 * dy;
                x2 = sx + t2 * dx;
                y2 = sy + t2 * dy;
                line.x1 = x1;
                line.y1 = y1;
                line.x2 = (x1 + x2) / 2;
                line.y2 = (y1 + y2) / 2;
                g2d.draw(line);
            }
        }
        drawShape(g2d, iStartingShape, stImgX, stImgY);
        drawShape(g2d, iEndingShape, enImgX, enImgY);
    }
