    public BufferedImage getVideoSurfaceContents() {
        Logger.debug("getVideoSurfaceContents()");
        try {
            Rectangle bounds = videoSurface.getBounds();
            bounds.setLocation(videoSurface.getLocationOnScreen());
            return new Robot().createScreenCapture(bounds);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get video surface contents", e);
        }
    }
