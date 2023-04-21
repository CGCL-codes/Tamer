    private int getColorOfSignalStrength(double value, double lowBorder, double highBorder) {
        double upperLimit = highBorder;
        double lowerLimit = lowBorder;
        double intervalSize = (upperLimit - lowerLimit) / 2;
        double middleValue = lowerLimit + (upperLimit - lowerLimit) / 2;
        if (value > highBorder) {
            return 0xCC00FF00;
        }
        if (value < lowerLimit) {
            return 0xCCFF0000;
        }
        int red = 0, green = 0, blue = 0, alpha = 0xCC;
        if (value > upperLimit - intervalSize) {
            green = (int) (255 - 255 * (upperLimit - value) / intervalSize);
        }
        if (value > middleValue - intervalSize && value < middleValue + intervalSize) {
            blue = (int) (255 - 255 * Math.abs(middleValue - value) / intervalSize);
        }
        if (value < lowerLimit + intervalSize) {
            red = (int) (255 - 255 * (value - lowerLimit) / intervalSize);
        }
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
