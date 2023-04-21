    public static BufferedImage take(Rectangle region) throws IllegalArgumentException, AWTException, SecurityException {
        return new Robot().createScreenCapture(region);
    }
