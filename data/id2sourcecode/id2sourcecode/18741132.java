    private BufferedImage createHighQualityScreenCapture(boolean drawPointer, Rectangle area) {
        if (changedHighSettings()) {
            refreshHighSettings();
        }
        Rectangle trueArea = new Rectangle(area.x, area.y, Math.min(area.width, highWidth - area.x), Math.min(area.height, highHeight - area.y));
        BufferedImage screenCapture = screenCaptureRobot.createScreenCapture(trueArea);
        if (sectionPixelBufferInt != null && sectionPixelBufferInt.length >= (screenCapture.getWidth() * screenCapture.getHeight())) {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels(sectionPixelBufferInt);
        } else {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels();
        }
        highQualityImage.getRaster().setDataElements(trueArea.x, trueArea.y, trueArea.width, trueArea.height, sectionPixelBufferInt);
        screenCapture.flush();
        screenCapture = null;
        highQualityPixelBufferInt = null;
        if (changedHighSettings()) {
            refreshHighSettings();
            return null;
        } else {
            if (drawPointer) {
                drawPointer(highQualityImage, trueArea);
            }
            return highQualityImage;
        }
    }
