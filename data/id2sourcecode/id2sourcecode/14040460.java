    public void drawGraphic(Graphics2D g, Point focusPoint, int zoomMode, Dimension screenSize) {
        int xPoint1 = transilatePoint(fromGraphic.getXPos(), zoomMode, (int) focusPoint.getX(), (int) screenSize.getWidth());
        int yPoint1 = transilatePoint(fromGraphic.getYPos(), zoomMode, (int) focusPoint.getY(), (int) screenSize.getHeight());
        int xPoint2 = transilatePoint(toGraphic.getXPos(), zoomMode, (int) focusPoint.getX(), (int) screenSize.getWidth());
        int yPoint2 = transilatePoint(toGraphic.getYPos(), zoomMode, (int) focusPoint.getY(), (int) screenSize.getHeight());
        int fromX = xPoint1 + scale(zoomMode, fromGraphic.getWidth()) / 2;
        int fromY = yPoint1 + scale(zoomMode, fromGraphic.getHeight()) / 2;
        int toX = xPoint2 + scale(zoomMode, toGraphic.getWidth()) / 2;
        int toY = yPoint2 + scale(zoomMode, toGraphic.getHeight()) / 2;
        if (this.good) {
            g.setColor(GOOD_COLOR);
        } else {
            g.setColor(BAD_COLOR);
        }
        g.drawLine(fromX, fromY, toX, toY);
        if (zoomMode == DETAIL_ZOOM) {
            int midX = (fromX + toX) / 2;
            int midY = (fromY + toY) / 2;
            int xDiff = fromX - toX;
            int yDiff = fromY - toY;
            double angle;
            angle = Math.atan((double) (yDiff) / (double) (xDiff));
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];
            if (xDiff < 0) {
                xPoints[0] = midX + (int) (Math.cos(angle) * AHEAD_LENGTH);
                yPoints[0] = midY + (int) (Math.sin(angle) * AHEAD_LENGTH);
            } else {
                xPoints[0] = midX - (int) (Math.cos(angle) * AHEAD_LENGTH);
                yPoints[0] = midY - (int) (Math.sin(angle) * AHEAD_LENGTH);
            }
            int awidth = (int) (Math.sin(angle) * AHEAD_WIDTH);
            int aheight = (int) (Math.cos(angle) * AHEAD_WIDTH);
            xPoints[1] = midX + awidth;
            yPoints[1] = midY - aheight;
            xPoints[2] = midX - awidth;
            yPoints[2] = midY + aheight;
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }
