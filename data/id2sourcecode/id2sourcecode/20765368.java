    public void acceptFloatingPoint(boolean finished) {
        pointList.add(floatingPoint);
        if (finished == true) {
            floatingPoint = null;
            double xMax = pointList.get(0).x;
            double yMax = pointList.get(0).y;
            double xMin = xMax;
            double yMin = yMax;
            for (int i = 1; i < pointList.size(); i++) {
                Point p = pointList.get(i);
                xMax = Math.max(p.x, xMax);
                yMax = Math.max(p.y, yMax);
                xMin = Math.min(p.x, xMin);
                yMin = Math.min(p.y, yMin);
            }
            super.centerX = (xMin + xMax) / 2;
            super.centerY = (yMin + yMax) / 2;
            for (Point p : pointList) {
                p.x -= centerX;
                p.y -= centerY;
            }
            super.width = xMax - xMin;
            super.height = yMax - yMin;
        }
    }
