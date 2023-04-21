    public BufferedImage captureScreen(ViewerData viewerData) {
        Rectangle screenRect = new Rectangle(viewerData.getScreenRect());
        BufferedImage screen = rt.createScreenCapture(screenRect);
        float screenScale = viewerData.getScreenScale();
        screenRect.width = (int) (screenRect.width * screenScale);
        screenRect.height = (int) (screenRect.height * screenScale);
        BufferedImage bimage = new BufferedImage(screenRect.width, screenRect.height, viewerData.getColorQuality());
        Graphics2D g2d = bimage.createGraphics();
        g2d.drawImage(screen, 0, 0, screenRect.width, screenRect.height, null);
        g2d.dispose();
        return bimage;
    }
