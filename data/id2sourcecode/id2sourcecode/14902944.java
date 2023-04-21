    void drawHexside(Graphics2D g2, double vx1, double vy1, double vx2, double vy2, char hexsideType) {
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
        switch(hexsideType) {
            case 'c':
                for (int j = 0; j < 3; j++) {
                    x = getCliffOrArrowsPositionXArray(j, vx1, vx2, theta);
                    y = getCliffOrArrowsPositionYArray(j, vy1, vy2, theta);
                    GeneralPath polygon = makePolygon(3, x, y, false);
                    g2.setColor(Color.white);
                    g2.fill(polygon);
                    g2.setColor(Color.black);
                    g2.draw(polygon);
                }
                break;
            case 'd':
                for (int j = 0; j < 3; j++) {
                    x0 = vx1 + (vx2 - vx1) * (2 + 3 * j) / 12;
                    y0 = vy1 + (vy2 - vy1) * (2 + 3 * j) / 12;
                    x1 = vx1 + (vx2 - vx1) * (4 + 3 * j) / 12;
                    y1 = vy1 + (vy2 - vy1) * (4 + 3 * j) / 12;
                    x2 = (x0 + x1) / 2;
                    y2 = (y0 + y1) / 2;
                    Rectangle2D.Double rect = new Rectangle2D.Double();
                    rect.x = x2 - len;
                    rect.y = y2 - len;
                    rect.width = 2 * len;
                    rect.height = 2 * len;
                    g2.setColor(Color.white);
                    Arc2D.Double arc = new Arc2D.Double(rect.x, rect.y, rect.width, rect.height, Math.toDegrees(2 * Math.PI - theta), 180, Arc2D.OPEN);
                    g2.fill(arc);
                    g2.setColor(Color.black);
                    g2.draw(arc);
                }
                break;
            case 's':
                for (int j = 0; j < 3; j++) {
                    x = getWallOrSlopePositionXArray(j, vx1, vx2, theta, 3);
                    y = getWallOrSlopePositionYArray(j, vy1, vy2, theta, 3);
                    g2.setColor(Color.black);
                    g2.draw(new Line2D.Double(x[0], y[0], x[1], y[1]));
                    g2.draw(new Line2D.Double(x[2], y[2], x[3], y[3]));
                }
                break;
            case 'w':
                for (int j = 0; j < 3; j++) {
                    x = getWallOrSlopePositionXArray(j, vx1, vx2, theta, 2);
                    y = getWallOrSlopePositionYArray(j, vy1, vy2, theta, 2);
                    GeneralPath polygon = makePolygon(4, x, y, false);
                    g2.setColor(Color.white);
                    g2.fill(polygon);
                    g2.setColor(Color.black);
                    g2.draw(polygon);
                }
                break;
            case 'r':
                g2.setColor(HTMLColor.skyBlue);
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke((float) 5.));
                g2.draw(new Line2D.Double(vx1, vy1, vx2, vy2));
                g2.setColor(Color.black);
                g2.setStroke(oldStroke);
                break;
            default:
                LOGGER.log(Level.SEVERE, "Bogus hexside type");
        }
    }
