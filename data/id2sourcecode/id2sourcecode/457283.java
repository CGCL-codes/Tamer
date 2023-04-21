    public BufferedImage ambilDrawing(Rectangle region) throws IllegalArgumentException, AWTException, SecurityException {
        if (region == null) throw new IllegalArgumentException("region == null");
        return new Robot().createScreenCapture(region);
    }
