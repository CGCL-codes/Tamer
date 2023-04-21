    public Point calculateBlobCenter() {
        int minX = 0x7FFFFFFF;
        int maxX = -0x7FFFFFFF;
        int minY = 0x7FFFFFFF;
        int maxY = -0x7FFFFFFF;
        Iterator<Point> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point tempPoint = iterator.next();
            int tempX = tempPoint.x;
            int tempY = tempPoint.y;
            if (tempX > maxX) maxX = tempX;
            if (tempX < minX) minX = tempX;
            if (tempY > maxY) maxY = tempY;
            if (tempY < minY) minY = tempY;
        }
        int centerX = (maxX + minX) / 2;
        int centerY = (maxY + minY) / 2;
        this.center = new Point(centerX, centerY);
        boolean centerFound = false;
        double minDistance = 0x7FFFFFFF;
        Point closestPoint = new Point(0, 0);
        iterator = points.iterator();
        while (iterator.hasNext() && centerFound == false) {
            Point tempPoint = iterator.next();
            if (tempPoint.x == center.x && tempPoint.y == center.y) return this.center; else {
                double tempDistance = Point2D.distance(tempPoint.x, tempPoint.y, center.x, center.y);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    closestPoint = new Point(tempPoint.x, tempPoint.y);
                }
            }
        }
        this.center = closestPoint;
        return this.center;
    }
