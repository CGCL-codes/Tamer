    private double calculateExactMass(MzPeak currentCandidate) {
        double xRight = -1, xLeft = -1;
        double halfIntensity = currentCandidate.getIntensity() / 2;
        DataPoint[] rangeDataPoints = currentCandidate.getRawDataPoints();
        for (int i = 0; i < rangeDataPoints.length - 1; i++) {
            if ((rangeDataPoints[i].getIntensity() <= halfIntensity) && (rangeDataPoints[i].getMZ() < currentCandidate.getMZ()) && (rangeDataPoints[i + 1].getIntensity() >= halfIntensity)) {
                double leftY1 = rangeDataPoints[i].getIntensity();
                double leftX1 = rangeDataPoints[i].getMZ();
                double leftY2 = rangeDataPoints[i + 1].getIntensity();
                double leftX2 = rangeDataPoints[i + 1].getMZ();
                double mLeft = (leftY1 - leftY2) / (leftX1 - leftX2);
                xLeft = leftX1 + (((halfIntensity) - leftY1) / mLeft);
                continue;
            }
            if ((rangeDataPoints[i].getIntensity() >= halfIntensity) && (rangeDataPoints[i].getMZ() > currentCandidate.getMZ()) && (rangeDataPoints[i + 1].getIntensity() <= halfIntensity)) {
                double rightY1 = rangeDataPoints[i].getIntensity();
                double rightX1 = rangeDataPoints[i].getMZ();
                double rightY2 = rangeDataPoints[i + 1].getIntensity();
                double rightX2 = rangeDataPoints[i + 1].getMZ();
                double mRight = (rightY1 - rightY2) / (rightX1 - rightX2);
                xRight = rightX1 + (((halfIntensity) - rightY1) / mRight);
                break;
            }
        }
        if ((xRight == -1) || (xLeft == -1)) return currentCandidate.getMZ();
        double exactMass = (xLeft + xRight) / 2;
        return exactMass;
    }
