    private void shrink(int[][] building, double amount) {
        int xMin = building[0][0];
        int xMax = building[0][0];
        int yMin = building[0][1];
        int yMax = building[0][1];
        for (int i = 1; i < building.length; ++i) {
            xMin = Math.min(xMin, building[i][0]);
            xMax = Math.max(xMax, building[i][0]);
            yMin = Math.min(yMin, building[i][1]);
            yMax = Math.max(yMax, building[i][1]);
        }
        int xCenter = (xMin + xMax) / 2;
        int yCenter = (yMin + yMax) / 2;
        for (int i = 0; i < building.length; ++i) {
            double dx = building[i][0] - xCenter;
            double dy = building[i][1] - yCenter;
            building[i][0] -= (int) (dx * amount);
            building[i][1] -= (int) (dy * amount);
        }
    }
