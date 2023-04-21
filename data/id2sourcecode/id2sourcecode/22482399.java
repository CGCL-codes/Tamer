    public int getGridCellID(double value) {
        if (value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("value must be in the range [0,1].");
        }
        int minCell = -1;
        int maxCell = lastCell;
        while (maxCell - 1 > minCell) {
            int checkCell = (maxCell + minCell) / 2;
            int row = getGridCellRow(checkCell);
            int col = getGridCellColumn(checkCell, row);
            double cellValue = getCellValue(row, col);
            if (value < cellValue) {
                maxCell = checkCell;
            } else {
                minCell = checkCell;
            }
        }
        return maxCell;
    }
