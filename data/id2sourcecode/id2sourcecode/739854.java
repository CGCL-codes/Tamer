    public void setRightScale(DesignBounds shape) {
        if (fits(shape)) {
            return;
        }
        double smallestFailure = shape.getScale();
        double largestSuccess = 0;
        for (int i = 0; i < 15; i++) {
            double newScale = (smallestFailure + largestSuccess) / 2;
            shape.setScale(newScale);
            if (fits(shape)) {
                largestSuccess = newScale;
            } else {
                smallestFailure = newScale;
            }
        }
        shape.setScale(largestSuccess);
    }
