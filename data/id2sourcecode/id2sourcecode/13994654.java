    public int get(double value) {
        if (value < 0.0 || value > 1.0) throw new IllegalArgumentException("value must be in the range [0,1].");
        int minCell = 0;
        int maxCell = lo_end.length - 1;
        while (maxCell - 1 > minCell) {
            int checkCell = (maxCell + minCell) / 2;
            double cellValue = lo_end[checkCell];
            if (value < cellValue) {
                maxCell = checkCell;
            } else {
                minCell = checkCell;
            }
        }
        return minCell;
    }
