    private BufferedImage createLowQualityScreenCapture(boolean drawPointer, Rectangle area) {
        if (changedLowSettings()) {
            refreshLowSettings();
        }
        Rectangle trueArea = new Rectangle(area.x, area.y, Math.min(area.width, lowWidth - area.x), Math.min(area.height, lowHeight - area.y));
        BufferedImage screenCapture = screenCaptureRobot.createScreenCapture(trueArea);
        int pixelDataLength = (screenCapture.getWidth() * screenCapture.getHeight());
        if (sectionPixelBufferInt != null && sectionPixelBufferInt.length >= pixelDataLength) {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels(sectionPixelBufferInt);
        } else {
            sectionPixelBufferInt = new SAWARGBPixelGrabber(screenCapture).getPixels();
        }
        lowQualityPixelBufferByte = ((DataBufferByte) lowQualityImage.getRaster().getDataBuffer()).getData();
        int startOffset = trueArea.x + lowQualityImage.getWidth() * trueArea.y;
        int currentWidth = 0;
        int currentHeight = 0;
        for (i = 0; i < pixelDataLength; i++, currentWidth++) {
            if (currentWidth == trueArea.getWidth()) {
                currentWidth = 0;
                currentHeight += lowQualityImage.getWidth();
            }
            lowRed = (((((sectionPixelBufferInt[i] >> 16) & 0xFF) + 26) / 51) * 36);
            lowGreen = (((((sectionPixelBufferInt[i] >> 8) & 0xFF) + 26) / 51) * 6);
            lowBlue = ((((sectionPixelBufferInt[i]) & 0xFF) + 26) / 51);
            lowQualityPixelBufferByte[startOffset + currentWidth + currentHeight] = (byte) (lowRed + lowGreen + lowBlue);
        }
        screenCapture.flush();
        screenCapture = null;
        lowQualityPixelBufferByte = null;
        if (changedLowSettings()) {
            refreshLowSettings();
            return null;
        } else {
            if (drawPointer) {
                drawPointer(lowQualityImage, trueArea);
            }
            return lowQualityImage;
        }
    }
