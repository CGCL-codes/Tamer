    public BufferedImage createScreenCapture(Rectangle screenRect) {
        if (screenRect.width <= 0) throw new IllegalArgumentException("Robot: capture width is <= 0");
        if (screenRect.height <= 0) throw new IllegalArgumentException("Robot: capture height is <= 0");
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(new AWTPermission("readDisplayPixels"));
        int[] pixels = peer.getRGBPixels(screenRect);
        BufferedImage bufferedImage = new BufferedImage(screenRect.width, screenRect.height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, screenRect.width, screenRect.height, pixels, 0, screenRect.width);
        return bufferedImage;
    }
