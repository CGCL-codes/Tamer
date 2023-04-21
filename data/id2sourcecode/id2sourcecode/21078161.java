    private void recalculateVisibility() {
        if (displayWidth <= 0 || displayHeight <= 0) {
            throw new IllegalStateException("Must set display with and height to > 0");
        }
        if (displayWidth < displayHeight) {
            visibleCentimetersX = visibleCentimeters;
            visibleCentimetersY = (displayHeight * visibleCentimetersX) / displayWidth;
        } else {
            visibleCentimetersY = visibleCentimeters;
            visibleCentimetersX = (displayWidth * visibleCentimetersY) / displayHeight;
        }
        visiblePixelsX = (displayWidth + 1) / 2;
        visiblePixelsY = (displayHeight + 1) / 2;
    }
