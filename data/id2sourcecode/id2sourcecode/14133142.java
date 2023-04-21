    private double[][] cubicToQuad(double controlX1, double controlY1, double controlX2, double controlY2, double anchorX, double anchorY) {
        try {
            Point2D p0 = new Point2D.Double(lastX, lastY);
            Point2D p1 = new Point2D.Double(controlX1, controlY1);
            Point2D p2 = new Point2D.Double(controlX2, controlY2);
            Point2D p3 = new Point2D.Double(anchorX, anchorY);
            Point2D p01 = getSubPoint(p0, p1);
            Point2D p12 = getSubPoint(p1, p2);
            Point2D p23 = getSubPoint(p2, p3);
            Point2D r1 = getSubPoint(p01, p12);
            Point2D r2 = getSubPoint(p12, p23);
            Point2D r12 = getSubPoint(r1, r2);
            Point2D c01 = getCrossPoint(r1, r2, p0, p1);
            Point2D c23 = getCrossPoint(r1, r2, p2, p3);
            return new double[][] { new double[] { c01.getX(), c01.getY(), r12.getX(), r12.getY() }, new double[] { c23.getX(), c23.getY(), p3.getX(), p3.getY() } };
        } catch (Exception e) {
            double cx = (controlX1 + controlX2) / 2;
            double cy = (controlY1 + controlY2) / 2;
            return new double[][] { new double[] { controlX1, controlY1, cx, cy }, new double[] { controlX2, controlY2, anchorX, anchorY } };
        }
    }
