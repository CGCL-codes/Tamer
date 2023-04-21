    private void fitToScreenUsingQuader(int screenX, int screenZ) {
        findMin();
        findMax();
        double center_x = (xmax + xmin) / 2;
        double center_y = (ymax + ymin) / 2;
        double center_z = (zmax + zmin) / 2;
        for (int i = 0; i < this.points.size(); i++) {
            ((Point) points.get(i)).x -= center_x;
            ((Point) points.get(i)).y -= center_y;
            ((Point) points.get(i)).z -= center_z;
        }
        double currentMax = 0;
        for (int i = 0; i < this.points.size(); i++) {
            Point p1 = (Point) points.get(i);
            double current = p1.dotProduct(p1);
            if (current > currentMax) {
                currentMax = current;
            }
        }
        double diagonal = (Math.sqrt(currentMax)) * 2;
        diagonal *= 1.10D;
        double faktor = 0;
        if (screenX >= screenZ) faktor = screenZ / diagonal; else faktor = screenX / diagonal;
        for (int i = 0; i < this.points.size(); i++) {
            ((Point) points.get(i)).x *= faktor;
            ((Point) points.get(i)).y *= faktor;
            ((Point) points.get(i)).z *= faktor;
        }
    }
