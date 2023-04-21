    private BufferedImage grabBufferedImage(Rectangle rect) {
        BufferedImage bi = robot.createScreenCapture(rect);
        drawVirtualCursor(bi, rect);
        return bi;
    }
