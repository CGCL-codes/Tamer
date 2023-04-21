    public BufferedImage getScreenImage(Rectangle screenRect) {
        if (bounds == null) bounds = qtDevice.getDefaultConfiguration().getBounds();
        screenRect = screenRect.intersection(bounds);
        QtImage image = new QtImage(screenRect.width, screenRect.height, (QtGraphicsConfiguration) qtDevice.getDefaultConfiguration());
        pCreateScreenCapture(qtDevice.psd, image.psd, screenRect.x, screenRect.y, screenRect.width, screenRect.height);
        return image.getSubimage(0, 0, screenRect.width, screenRect.height);
    }
