    protected static double[] binarySearch(double start, double end, int varIndex, BinarySearchFunction func) {
        double startVal = func.function(start, varIndex);
        double endVal = func.function(end, varIndex);
        double middle = (start + end) / 2;
        double middleVal = func.function(middle, varIndex);
        double startDiff = Math.abs(startVal - middleVal);
        double endDiff = Math.abs(endVal - middleVal);
        if (startDiff < endDiff) {
            return new double[] { start, middle, startVal, middleVal, middle, middleVal };
        } else if (startDiff > endDiff) {
            return new double[] { middle, end, middleVal, endVal, middle, middleVal };
        } else {
            if (startVal < endVal) {
                return new double[] { start, middle, startVal, middleVal, middle, middleVal };
            } else {
                return new double[] { middle, end, middleVal, endVal, middle, middleVal };
            }
        }
    }
