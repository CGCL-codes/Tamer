    private void drawGate(Graphics2D g2, double vx1, double vy1, double vx2, double vy2, Constants.HexsideGates gateType) {
        double x0;
        double y0;
        double x1;
        double y1;
        double x2;
        double y2;
        double theta;
        double[] x = new double[4];
        double[] y = new double[4];
        x0 = vx1 + (vx2 - vx1) / 6;
        y0 = vy1 + (vy2 - vy1) / 6;
        x1 = vx1 + (vx2 - vx1) / 3;
        y1 = vy1 + (vy2 - vy1) / 3;
        theta = Math.atan2(vy2 - vy1, vx2 - vx1);
        switch(gateType) {
            case BLOCK:
                x = getWallOrSlopePositionXArray(0, vx1, vx2, theta, 1);
                y = getWallOrSlopePositionYArray(0, vy1, vy2, theta, 1);
                GeneralPath polygon = makePolygon(4, x, y, false);
                g2.setColor(Color.white);
                g2.fill(polygon);
                g2.setColor(Color.black);
                g2.draw(polygon);
                break;
            case ARCH:
                x = getWallOrSlopePositionXArray(0, vx1, vx2, theta, 1);
                y = getWallOrSlopePositionYArray(0, vy1, vy2, theta, 1);
                x2 = (x0 + x1) / 2;
                y2 = (y0 + y1) / 2;
                Rectangle2D.Double rect = new Rectangle2D.Double();
                rect.x = x2 - len;
                rect.y = y2 - len;
                rect.width = 2 * len;
                rect.height = 2 * len;
                Arc2D.Double arc = new Arc2D.Double(rect.x, rect.y, rect.width, rect.height, Math.toDegrees(-theta), 180, Arc2D.OPEN);
                g2.setColor(Color.white);
                g2.fill(arc);
                g2.setColor(Color.black);
                g2.draw(arc);
                x[2] = x[0];
                y[2] = y[0];
                x[0] = x1;
                y[0] = y1;
                x[1] = x[3];
                y[1] = y[3];
                x[3] = x0;
                y[3] = y0;
                polygon = makePolygon(4, x, y, false);
                g2.setColor(Color.white);
                g2.fill(polygon);
                g2.draw(new Line2D.Double(x0, y0, x1, y1));
                g2.setColor(Color.black);
                g2.draw(new Line2D.Double(x1, y1, x[1], y[1]));
                g2.draw(new Line2D.Double(x[2], y[2], x0, y0));
                break;
            case ARROW:
                x[0] = x0 - len * Math.sin(theta);
                y[0] = y0 + len * Math.cos(theta);
                x[1] = (x0 + x1) / 2 + len * Math.sin(theta);
                y[1] = (y0 + y1) / 2 - len * Math.cos(theta);
                x[2] = x1 - len * Math.sin(theta);
                y[2] = y1 + len * Math.cos(theta);
                polygon = makePolygon(3, x, y, false);
                g2.setColor(Color.white);
                g2.fill(polygon);
                g2.setColor(Color.black);
                g2.draw(polygon);
                break;
            case ARROWS:
                for (int j = 0; j < 3; j++) {
                    x = getCliffOrArrowsPositionXArray(j, vx1, vx2, theta);
                    y = getCliffOrArrowsPositionYArray(j, vy1, vy2, theta);
                    polygon = makePolygon(3, x, y, false);
                    g2.setColor(Color.white);
                    g2.fill(polygon);
                    g2.setColor(Color.black);
                    g2.draw(polygon);
                }
                break;
            case NONE:
                LOGGER.log(Level.WARNING, "Drawing code called for gate type NONE");
        }
    }
