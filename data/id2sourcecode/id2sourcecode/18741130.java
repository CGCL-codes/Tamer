    private BufferedImage createMediumQualityScreenCapture(boolean drawPointer, Rectangle area) {
        if (changedMediumSettings()) {
            refreshMediumSettings();
        }
        Rectangle trueArea = new Rectangle(area.x, area.y, Math.min(area.width, mediumWidth - area.x), Math.min(area.height, mediumHeight - area.y));
        BufferedImage screenCapture = screenCaptureRobot.createScreenCapture(trueArea);
        int pixelDataLength = (screenCapture.getWidth() * screenCapture.getHeight());
        if (sectionPixelBufferInt != null && sectionPixelBufferInt.length >= pixelDataLength) {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels(sectionPixelBufferInt);
        } else {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels();
        }
        mediumQualityPixelBufferShort = ((DataBufferUShort) mediumQualityImage.getRaster().getDataBuffer()).getData();
        int startOffset = trueArea.x + mediumQualityImage.getWidth() * trueArea.y;
        int currentWidth = 0;
        int currentHeight = 0;
        for (j = 0; j < pixelDataLength; j++, currentWidth++) {
            if (currentWidth == trueArea.getWidth()) {
                currentWidth = 0;
                currentHeight += mediumQualityImage.getWidth();
            }
            mediumRed = ((((sectionPixelBufferInt[j] >> 16) & 0xFF) >>> 3) << 10);
            mediumGreen = ((((sectionPixelBufferInt[j] >> 8) & 0xFF) >>> 3) << 5);
            mediumBlue = ((((sectionPixelBufferInt[j]) & 0xFF) >>> 3));
            mediumQualityPixelBufferShort[startOffset + currentWidth + currentHeight] = (short) (mediumRed | mediumGreen | mediumBlue);
        }
        screenCapture.flush();
        screenCapture = null;
        mediumQualityPixelBufferShort = null;
        if (changedMediumSettings()) {
            refreshMediumSettings();
            return null;
        } else {
            if (drawPointer) {
                drawPointer(mediumQualityImage, trueArea);
            }
            return mediumQualityImage;
        }
    }
