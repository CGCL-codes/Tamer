    public void paint(Graphics2D g2) {
        g2.setColor(colors[0]);
        g2.setStroke(new BasicStroke((float) 1));
        Line2D l;
        if (listOfNextRoadSegments != null) {
            for (RoadSegment roadSeg : listOfNextRoadSegments) {
                l = new Line2D.Double(xf, yf, roadSeg.getX0(), roadSeg.getY0());
                g2.draw(l);
            }
        }
        if (destination != null) {
            l = new Line2D.Double(xf, yf, destination.getX0(), destination.getY0());
            g2.draw(l);
        }
        g2.setStroke(new BasicStroke((float) 0.1));
        Rectangle2D.Double rect = new Rectangle2D.Double(0, 0 - width / 2, Point2D.distance(x0, y0, xf, yf), width);
        if ((showRuler) && (setOfConflictingRoads != null)) {
            g2.setColor(Color.RED);
            for (RoadSegment road : setOfConflictingRoads) {
                double finalX = (road.getX0() + road.getXf()) / 2;
                double finalY = (road.getY0() + road.getYf()) / 2;
                double initialx = (x0 + xf) / 2;
                double initialy = (y0 + yf) / 2;
                l = new Line2D.Double(initialx, initialy, finalX, finalY);
                Ellipse2D ellipse = new Ellipse2D.Double(finalX - 1, finalY - 1, 2.0, 2.0);
                g2.draw(l);
                g2.draw(ellipse);
            }
        }
        if (selected) {
            double size = width / 2;
            double center = size / 2;
            Rectangle2D rect1 = new Rectangle2D.Double(x0 - center, y0 - center, size, size);
            g2.fill(rect1);
            rect1.setFrame(xf - center, yf - center, size, size);
            g2.fill(rect1);
            g2.setColor(colors[1]);
        } else {
            g2.setColor(colors[0]);
        }
        AffineTransform originalTransform = g2.getTransform();
        g2.transform(affineTransform);
        g2.draw(rect);
        Line2D.Double l1 = new Line2D.Double(0, width / 2, width, 0);
        Line2D.Double l2 = new Line2D.Double(0, -width / 2, width, 0);
        g2.draw(l1);
        g2.draw(l2);
        if (showRuler) {
            Font font = new Font("Dialog", Font.PLAIN, 6);
            g2.setFont(font);
            for (int i = 100; i <= length; i += 100) {
                l1.setLine(i, 0 - width / 2 - 2, i, 0 - width / 2 + 2);
                g2.draw(l1);
                g2.drawString(Integer.toString(i), i, (int) (0 - width / 2 - 3));
            }
            for (int j = 10; j <= length; j += 10) {
                l1.setLine(j, 0 - width / 2 - 1, j, 0 - width / 2 + 1);
                g2.draw(l1);
            }
        }
        for (int i = listOfVehicles.size() - 1; i >= 0; i--) {
            Vehicle vehicle = listOfVehicles.get(i);
            if (this.equals(vehicle.getCurrentRoadSegment())) {
                vehicle.paint(g2);
            }
        }
        boolean show;
        g2.setStroke(new BasicStroke(1));
        for (IEntity entity : listOfEntities) {
            show = false;
            if (entity instanceof SignalHead) {
                g2.setColor(Color.black);
                show = true;
            } else {
                if (entity instanceof ReportMeasuresStart) {
                    g2.setColor(Color.gray);
                    show = true;
                } else {
                    if (entity instanceof Detector) {
                        g2.setColor(Color.green);
                        show = true;
                    }
                }
            }
            if (show) {
                l1.setLine(entity.getBackPosition(), 0, entity.getFrontPosition(), 0);
                g2.draw(l1);
                l1.setLine(entity.getBackPosition(), 0 - width / 2, entity.getBackPosition(), 0 + width / 2);
                g2.draw(l1);
                l1.setLine(entity.getFrontPosition(), 0 - width / 2, entity.getFrontPosition(), 0 + width / 2);
                g2.draw(l1);
            }
        }
        g2.setTransform(originalTransform);
    }
