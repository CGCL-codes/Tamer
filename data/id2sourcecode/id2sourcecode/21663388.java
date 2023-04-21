    public BufferedImage getScreenshot() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        return robot.createScreenCapture(screenRect);
    }
