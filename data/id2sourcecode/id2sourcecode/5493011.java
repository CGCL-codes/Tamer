    public synchronized BufferedImage getScreen(int monitor) {
        return systems[monitor].createScreenCapture(screenRects[monitor]);
    }
