    public BufferedImage getCapture() {
        if (rt == null) throw new RuntimeException("Robot not initialized!");
        BufferedImage original = rt.createScreenCapture(captureDimension);
        if (scaledHeight == captureHeight && scaledWidth == captureWidth) {
            return convertImage(original, imageType);
        } else {
            BufferedImage scaled = scaleImage(original, true);
            return convertImage(scaled, imageType);
        }
    }
