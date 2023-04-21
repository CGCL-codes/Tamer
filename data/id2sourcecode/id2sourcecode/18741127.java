    private BufferedImage createLowQualityScreenCapture(boolean drawPointer) {
        if (changedLowSettings()) {
            refreshLowSettings();
        }
        BufferedImage screenCapture = screenCaptureRobot.createScreenCapture(new Rectangle(0, 0, lowWidth, lowHeight));
        int pixelDataLength = (screenCapture.getWidth() * screenCapture.getHeight());
        if (sectionPixelBufferInt != null && sectionPixelBufferInt.length >= pixelDataLength) {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels(sectionPixelBufferInt);
        } else {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels();
        }
        lowQualityPixelBufferByte = ((DataBufferByte) lowQualityImage.getRaster().getDataBuffer()).getData();
        for (i = 0; i < pixelDataLength; i++) {
            lowRed = (((((sectionPixelBufferInt[i] >> 16) & 0xFF) + 26) / 51) * 36);
            lowGreen = (((((sectionPixelBufferInt[i] >> 8) & 0xFF) + 26) / 51) * 6);
            lowBlue = ((((sectionPixelBufferInt[i]) & 0xFF) + 26) / 51);
            lowQualityPixelBufferByte[i] = (byte) (lowRed + lowGreen + lowBlue);
        }
        screenCapture.flush();
        screenCapture = null;
        lowQualityPixelBufferByte = null;
        if (changedLowSettings()) {
            refreshLowSettings();
            return null;
        } else {
            if (drawPointer) {
                drawPointer(lowQualityImage);
            }
            return lowQualityImage;
        }
    }
