    private Icon snapshot(Rectangle rect) throws AWTException {
        Robot r = new Robot();
        BufferedImage img = r.createScreenCapture(rect);
        Icon icon = new ImageIcon(img);
        return icon;
    }
