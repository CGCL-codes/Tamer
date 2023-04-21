    public BufferedImage captureScreen(ViewerOptions viewerOptions) {
        updateScreenRect(viewerOptions);
        oldScreenRect = screenRect;
        BufferedImage screen = rt.createScreenCapture(screenRect);
        BufferedImage bimage = new BufferedImage(screenRect.width, screenRect.height, viewerOptions.getColorQuality());
        Graphics2D g2d = bimage.createGraphics();
        g2d.drawImage(screen, 0, 0, screenRect.width, screenRect.height, null);
        return bimage;
    }
