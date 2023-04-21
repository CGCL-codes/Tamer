    private BufferedImage createHighQualityScreenCapture(boolean drawPointer) {
        if (changedHighSettings()) {
            refreshHighSettings();
        }
        BufferedImage screenCapture = screenCaptureRobot.createScreenCapture(new Rectangle(0, 0, highWidth, highHeight));
        highQualityPixelBufferInt = ((DataBufferInt) highQualityImage.getRaster().getDataBuffer()).getData();
        highQualityPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels(highQualityPixelBufferInt);
        screenCapture.flush();
        screenCapture = null;
        highQualityPixelBufferInt = null;
        if (changedHighSettings()) {
            refreshHighSettings();
            return null;
        } else {
            if (drawPointer) {
                drawPointer(highQualityImage);
            }
            return highQualityImage;
        }
    }
