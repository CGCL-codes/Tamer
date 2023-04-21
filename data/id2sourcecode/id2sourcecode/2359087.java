    public static final Screenshot capture(Rectangle screenRect) throws AWTException {
        return new Screenshot(getRobot().createScreenCapture(screenRect));
    }
