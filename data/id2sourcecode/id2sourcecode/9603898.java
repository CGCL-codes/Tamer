    public void drawArrow(BLink link, double maxLen, boolean revDir, Color color, int borderWidth, Color borderColor) {
        double x1 = link.getX1(), y1 = link.getY1(), x2 = link.getX2(), y2 = link.getY2();
        Iterator<Point2D.Double> shpPts = link.getShapePoints();
        if (revDir) {
            x1 = link.getX2();
            y1 = link.getY2();
            x2 = link.getX1();
            y2 = link.getY1();
            List<Point2D.Double> revSP = new LinkedList<Point2D.Double>();
            while (shpPts.hasNext()) {
                revSP.add(0, shpPts.next());
            }
            shpPts = revSP.iterator();
        }
        double gx1 = coords_.xToScreen(x1);
        double gy1 = coords_.yToScreen(y1);
        double gx2 = coords_.xToScreen(x2);
        double gy2 = coords_.yToScreen(y2);
        double len = FPUtil.magnitude(gx1, gy1, gx2, gy2) * .25;
        if (len > maxLen) {
            len = maxLen;
        }
        if (shpPts.hasNext()) {
            double ix1 = x1, iy1 = y1, ix2 = 0, iy2 = 0;
            double fx1 = 0, fy1 = 0, fx2 = 0, fy2 = 0;
            double wlen = link.getLengthFeet(), wlenCovered = 0;
            while (shpPts.hasNext()) {
                java.awt.geom.Point2D.Double shpPt = shpPts.next();
                ix2 = shpPt.getX();
                iy2 = shpPt.getY();
                wlenCovered += FPUtil.magnitude(ix1, iy1, ix2, iy2);
                if (wlenCovered > wlen / 2) {
                    fx1 = ix1;
                    fy1 = iy1;
                    fx2 = ix2;
                    fy2 = iy2;
                    break;
                }
                ix1 = ix2;
                iy1 = iy2;
            }
            if (fx1 == 0) {
                fx1 = ix1;
                fy1 = iy1;
                fx2 = x2;
                fy2 = y2;
            }
            gx1 = coords_.xToScreen(fx1);
            gy1 = coords_.yToScreen(fy1);
            gx2 = coords_.xToScreen(fx2);
            gy2 = coords_.yToScreen(fy2);
        }
        double mx = (gx1 + gx2) / 2, my = (gy1 + gy2) / 2;
        double theta = Math.acos((gx2 - gx1) / FPUtil.magnitude(gx1, gy1, gx2, gy2));
        if (gx1 < gx2 && gy2 <= gy1 || gx2 <= gx1 && gy2 < gy1) {
            theta = 2 * Math.PI - theta;
        }
        double sintheta = Math.sin(theta);
        double costheta = Math.cos(theta);
        double tx1 = mx + len * costheta, ty1 = my + len * sintheta;
        double txi = mx - len * costheta, tyi = my - len * sintheta;
        double width = len * .6;
        double tx2 = txi + (len / 2) * Math.cos(theta + Math.PI / 2), ty2 = tyi + (len / 2) * Math.sin(theta + Math.PI / 2);
        double tx3 = txi + (len / 2) * Math.cos(theta - Math.PI / 2), ty3 = tyi + (len / 2) * Math.sin(theta - Math.PI / 2);
        int[] xArr = { (int) tx1 + 1, (int) tx2 + 1, (int) tx3 + 1 };
        int[] yArr = { (int) ty1 + 1, (int) ty2 + 1, (int) ty3 + 1 };
        if (color != null) {
            g2d_.setColor(color);
        }
        g2d_.fillPolygon(xArr, yArr, 3);
        if (borderWidth > 0) {
            g2d_.setStroke(new BasicStroke(borderWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            if (borderColor != null) {
                g2d_.setColor(borderColor);
            }
            g2d_.drawPolygon(xArr, yArr, 3);
        }
    }
