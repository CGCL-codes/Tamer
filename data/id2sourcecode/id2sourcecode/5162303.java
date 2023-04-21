    public BufferedImage grabScreenshot() {
        LOG.info("Grabbing screenshot...");
        try {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(screenSize));
            return image;
        } catch (Throwable ex) {
            LOG.log(Level.SEVERE, "Problem during grabbing screenshot: " + ex.getMessage(), ex);
        }
        return null;
    }
