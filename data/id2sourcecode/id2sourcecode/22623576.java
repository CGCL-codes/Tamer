    public void capture() {
        try {
            GraphicsConfiguration conf = getGraphicsConfiguration();
            GraphicsDevice device = conf.getDevice();
            wholeScreenCapture = null;
            wholeScreenCapture = new Robot(device).createScreenCapture(conf.getBounds());
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
