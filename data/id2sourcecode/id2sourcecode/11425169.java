    public void updateEdgeView() {
        targetPoint = targetLocator.locatePoint(targetPoint);
        sourcePoint = sourceLocator.locatePoint(sourcePoint);
        targetPoint = target.localToGlobal(targetPoint);
        sourcePoint = source.localToGlobal(sourcePoint);
        if (selfEdge) {
            float width = (float) target.getWidth();
            float height = (float) target.getHeight();
            setPathToEllipse((float) (sourcePoint.getX() - .25 * width), (float) (sourcePoint.getY() - .25 * height), width * (float) .5, height * (float) .5);
            return;
        }
        updateTargetPointView();
        updateSourcePointView();
        FEdge[] same_edges = new FEdge[0];
        if (same_edges == null) same_edges = new FEdge[0];
        if (same_edges.length > 1) {
            System.out.println("Same Edges: " + same_edges.length);
            int count = 0;
            if (same_edges.length % 2 == 0) count++;
            if (count > 0) {
                double a = (sourcePoint.getX() + targetPoint.getX()) / 2;
                double b = (sourcePoint.getY() + targetPoint.getY()) / 2;
                double x;
                double y;
                boolean invertX = false;
                boolean invertY = false;
                if (true) {
                    x = sourcePoint.getX();
                    y = sourcePoint.getY();
                    if (y > targetPoint.getY()) {
                        invertX = true;
                    }
                    if (x < targetPoint.getX()) {
                        invertY = true;
                    }
                } else {
                    x = targetPoint.getX();
                    y = targetPoint.getY();
                    if (y > sourcePoint.getY()) {
                        invertX = true;
                    }
                    if (x < sourcePoint.getX()) {
                        invertY = true;
                    }
                }
                double H = Math.sqrt((x - a) * (x - a) + (y - b) * (y - b));
                double O = Math.abs(y - b);
                double theta = (Math.PI / 2) - Math.asin(O / H);
                double y_step = 10.0 * Math.sin(theta);
                double x_step = 10.0 * Math.cos(theta);
                if (invertX) {
                    x_step = -x_step;
                }
                if (invertY) {
                    y_step = -y_step;
                }
                int num = (count + 1) / 2;
                if (count % 2 == 0) {
                    num = -num;
                }
                x_step *= num;
                y_step *= num;
                a += x_step;
                b += y_step;
                bend.moveHandle_internal(0, new Point2D.Double(a, b));
            }
        }
        if (!inLargeGraph) {
            updateTargetArrow();
            updateSourceArrow();
        }
        updateLine();
    }
