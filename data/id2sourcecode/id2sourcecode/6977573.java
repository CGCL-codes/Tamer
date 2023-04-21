    public void drawLine(double x1, double y1, double x2, double y2, boolean drawArrow) {
        double gx1 = coords_.xToScreen(x1);
        double gy1 = coords_.yToScreen(y1);
        double gx2 = coords_.xToScreen(x2);
        double gy2 = coords_.yToScreen(y2);
        int gx1i = (int) gx1;
        int gy1i = (int) gy1;
        int gx2i = (int) gx2;
        int gy2i = (int) gy2;
        if (g2d_ != null) {
            g2d_.drawLine(gx1i, gy1i, gx2i, gy2i);
        }
        if (drawArrow) {
            double mx = (gx1 + gx2) / 2, my = (gy1 + gy2) / 2;
            double theta = Math.acos((gx2 - gx1) / FPUtil.magnitude(gx1, gy1, gx2, gy2));
            if (gx1 < gx2 && gy2 <= gy1 || gx2 <= gx1 && gy2 < gy1) {
                theta = 2 * Math.PI - theta;
            }
            double sintheta = Math.sin(theta);
            double costheta = Math.cos(theta);
            double len = FPUtil.magnitude(gx1, gy1, gx2, gy2) * .375;
            if (len > 8) {
                len = 8;
            }
            double tx1 = mx + len * costheta, ty1 = my + len * sintheta;
            double txi = mx - len * costheta, tyi = my - len * sintheta;
            double tx2 = txi + (len / 2) * Math.cos(theta + Math.PI / 2), ty2 = tyi + (len / 2) * Math.sin(theta + Math.PI / 2);
            double tx3 = txi + (len / 2) * Math.cos(theta - Math.PI / 2), ty3 = tyi + (len / 2) * Math.sin(theta - Math.PI / 2);
            int[] xArr = { (int) tx1, (int) tx2, (int) tx3 };
            int[] yArr = { (int) ty1, (int) ty2, (int) ty3 };
            g2d_.fillPolygon(xArr, yArr, 3);
        }
    }
