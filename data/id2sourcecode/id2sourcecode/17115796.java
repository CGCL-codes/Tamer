    @Override
    public void draw(Graphics g, boolean coordsChanged) {
        if (function == null || coords == null) return;
        int ct = function.getPointCount();
        if (ct == 0) return;
        int startPt;
        int endPt;
        double xmin = coords.pixelToX(coords.getLeft());
        double xmax = coords.pixelToX(coords.getLeft() + coords.getWidth());
        if (function.getX(0) > xmax || function.getX(ct - 1) < xmin) return;
        startPt = 0;
        while (startPt < ct - 1 && function.getX(startPt + 1) <= xmin) startPt++;
        endPt = ct - 1;
        while (endPt > 1 && function.getX(endPt - 1) >= xmax) endPt--;
        double x, y, a, b;
        int xInt, yInt, aInt, bInt;
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints oldHints = g2.getRenderingHints();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke oldStroke = g2.getStroke();
        if (lineStyle == DASHED_STYLE) {
            BasicStroke bs = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dash, 0.0f);
            g2.setStroke(bs);
        } else {
            g2.setStroke(new BasicStroke(lineWidth));
        }
        switch(function.getStyle()) {
            case TableFunction.SMOOTH:
                {
                    if (endPt > startPt) {
                        x = function.getX(startPt);
                        y = function.getVal(x);
                        xInt = coords.xToPixel(x);
                        yInt = coords.yToPixel(y);
                        double limit = xmax;
                        if (function.getX(endPt) < limit) limit = function.getX(endPt);
                        coords.xToPixel(function.getX(ct - 1));
                        aInt = xInt;
                        while (x < limit) {
                            aInt += 3;
                            a = coords.pixelToX(aInt);
                            if (a > limit) a = limit;
                            b = function.getVal(a);
                            bInt = coords.yToPixel(b);
                            g2.drawLine(xInt, yInt, aInt, bInt);
                            x = a;
                            xInt = aInt;
                            yInt = bInt;
                        }
                    }
                    break;
                }
            case TableFunction.PIECEWISE_LINEAR:
                {
                    x = function.getX(startPt);
                    xInt = coords.xToPixel(x);
                    y = function.getY(startPt);
                    yInt = coords.yToPixel(y);
                    for (int i = startPt + 1; i <= endPt; i++) {
                        a = function.getX(i);
                        aInt = coords.xToPixel(a);
                        b = function.getY(i);
                        bInt = coords.yToPixel(b);
                        g2.drawLine(xInt, yInt, aInt, bInt);
                        xInt = aInt;
                        yInt = bInt;
                    }
                    break;
                }
            case TableFunction.STEP:
                {
                    x = function.getX(startPt);
                    xInt = coords.xToPixel(x);
                    for (int i = startPt; i <= endPt; i++) {
                        if (i < endPt) {
                            double nextX = function.getX(i + 1);
                            a = (x + nextX) / 2;
                            x = nextX;
                        } else a = x;
                        aInt = coords.xToPixel(a);
                        y = function.getY(i);
                        yInt = coords.yToPixel(y);
                        g2.drawLine(xInt, yInt, aInt, yInt);
                        xInt = aInt;
                    }
                    break;
                }
            case TableFunction.STEP_LEFT:
                {
                    x = function.getX(startPt);
                    xInt = coords.xToPixel(x);
                    for (int i = startPt + 1; i <= endPt; i++) {
                        a = function.getX(i);
                        aInt = coords.xToPixel(a);
                        y = function.getY(i - 1);
                        yInt = coords.yToPixel(y);
                        g2.drawLine(xInt, yInt, aInt, yInt);
                        xInt = aInt;
                    }
                    break;
                }
            case TableFunction.STEP_RIGHT:
                {
                    x = function.getX(startPt);
                    xInt = coords.xToPixel(x);
                    for (int i = startPt + 1; i <= endPt; i++) {
                        a = function.getX(i);
                        aInt = coords.xToPixel(a);
                        y = function.getY(i);
                        yInt = coords.yToPixel(y);
                        g2.drawLine(xInt, yInt, aInt, yInt);
                        xInt = aInt;
                    }
                    break;
                }
        }
        if (!showPoints) {
            g2.setRenderingHints(oldHints);
            g2.setStroke(oldStroke);
            return;
        }
        for (int i = startPt; i <= endPt; i++) {
            x = function.getX(i);
            y = function.getY(i);
            xInt = coords.xToPixel(x);
            yInt = coords.yToPixel(y);
            g2.fillOval(xInt - 2, yInt - 2, lineWidth + 4, lineWidth + 4);
        }
        g2.setRenderingHints(oldHints);
        g2.setStroke(oldStroke);
    }
