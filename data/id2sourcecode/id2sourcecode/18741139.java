    public synchronized BufferedImage createScreenCapture(boolean drawPointer, Rectangle area) {
        if (!isScreenCaptureInitialized()) {
            initializeScreenCapture();
        }
        if (colorQuality == SAW_COLOR_QUALITY_HIGH) {
            return createHighQualityScreenCapture(drawPointer, area);
        } else if (colorQuality == SAW_COLOR_QUALITY_MEDIUM) {
            return createMediumQualityScreenCapture(drawPointer, area);
        } else {
            return createLowQualityScreenCapture(drawPointer, area);
        }
    }
