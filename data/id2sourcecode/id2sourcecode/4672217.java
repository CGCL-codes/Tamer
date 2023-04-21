    public synchronized BufferedImage createScreenCapture(Rectangle screenRect) {
        if (screenRect.width <= 0 || screenRect.height <= 0) {
            throw new IllegalArgumentException("Rectangle width and height must be > 0");
        }
        return robotHelper.getScreenImage(screenRect);
    }
