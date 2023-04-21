    double getFeretBreadth(Shape shape, double angle, double x1, double y1, double x2, double y2) {
        double cx = x1 + (x2 - x1) / 2;
        double cy = y1 + (y2 - y1) / 2;
        AffineTransform at = new AffineTransform();
        at.rotate(angle * Math.PI / 180.0, cx, cy);
        Shape s = at.createTransformedShape(shape);
        Rectangle2D r = s.getBounds2D();
        return Math.min(r.getWidth(), r.getHeight());
    }
