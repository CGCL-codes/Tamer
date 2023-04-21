    private BufferedImage createMediumQualityScreenCapture(boolean drawPointer) {
        if (changedMediumSettings()) {
            refreshMediumSettings();
        }
        BufferedImage screenCapture = screenCaptureRobot.createScreenCapture(new Rectangle(0, 0, mediumWidth, mediumHeight));
        int pixelDataLength = (screenCapture.getWidth() * screenCapture.getHeight());
        if (sectionPixelBufferInt != null && sectionPixelBufferInt.length >= pixelDataLength) {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels(sectionPixelBufferInt);
        } else {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels();
        }
        mediumQualityPixelBufferShort = ((DataBufferUShort) mediumQualityImage.getRaster().getDataBuffer()).getData();
        for (j = 0; j < pixelDataLength; j++) {
            mediumRed = ((((sectionPixelBufferInt[j] >> 16) & 0xFF) >>> 3) << 10);
            mediumGreen = ((((sectionPixelBufferInt[j] >> 8) & 0xFF) >>> 3) << 5);
            mediumBlue = ((((sectionPixelBufferInt[j]) & 0xFF) >>> 3));
            mediumQualityPixelBufferShort[j] = (short) (mediumRed | mediumGreen | mediumBlue);
        }
        screenCapture.flush();
        screenCapture = null;
        mediumQualityPixelBufferShort = null;
        if (changedMediumSettings()) {
            refreshMediumSettings();
            return null;
        } else {
            if (drawPointer) {
                drawPointer(mediumQualityImage);
            }
            return mediumQualityImage;
        }
    }
