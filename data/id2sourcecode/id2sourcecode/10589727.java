    public static BufferedImage createImage(Rectangle region) throws AWTException {
        BufferedImage image = new Robot().createScreenCapture(region);
        return image;
    }
