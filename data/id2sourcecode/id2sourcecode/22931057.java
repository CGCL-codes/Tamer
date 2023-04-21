    static BufferedImage getScreenImage() throws AWTException {
        final Toolkit kit = Toolkit.getDefaultToolkit();
        final Rectangle rec = new Rectangle(kit.getScreenSize());
        final Robot rob = new Robot();
        return rob.createScreenCapture(rec);
    }
