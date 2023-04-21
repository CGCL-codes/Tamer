    public synchronized BufferedImage createScreenCapture(boolean drawPointer) {
        if (!isScreenCaptureInitialized()) {
            initializeScreenCapture();
        }
        if (colorQuality == SAW_COLOR_QUALITY_HIGH) {
            return createHighQualityScreenCapture(drawPointer);
        } else if (colorQuality == SAW_COLOR_QUALITY_MEDIUM) {
            return createMediumQualityScreenCapture(drawPointer);
        } else {
            return createLowQualityScreenCapture(drawPointer);
        }
    }
