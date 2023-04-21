    private Dimension calcHexArrayDimension() {
        int heightHalves = 2 * numOfLogicalRows + 1;
        heightHalves -= (logicalTrimLow ? 1 : 0);
        heightHalves -= (logicalTrimHigh ? 1 : 0);
        int height = ((cellBoundingDimension.height * heightHalves) + 1) / 2;
        int numWholeWidths = (numOfLogicalCols + 1) / 2;
        int numSmallWidth = numOfLogicalCols - numWholeWidths;
        int width = (numWholeWidths * cellBoundingDimension.width) + (numSmallWidth * controlLineLength);
        return new Dimension(width, height);
    }
