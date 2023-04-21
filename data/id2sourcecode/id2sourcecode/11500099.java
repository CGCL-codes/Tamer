    public static BufferedImage captureScreen() {
        BufferedImage screenShotImage = null;
        try {
            Robot robot = new Robot();
            Rectangle mainFrameRectangle = mainFrame.getBounds();
            screenShotImage = robot.createScreenCapture(mainFrameRectangle);
        } catch (Exception e) {
            System.out.println("Exception creating screenshot: " + e.getMessage());
        }
        return screenShotImage;
    }
